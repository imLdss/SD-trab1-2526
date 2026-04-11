package sd2526.trab.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.google.protobuf.Empty;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import jakarta.ws.rs.core.MediaType;
import sd2526.trab.api.Message;
import sd2526.trab.api.User;
import sd2526.trab.api.grpc.GrpcMessagesGrpc;
import sd2526.trab.api.grpc.Messages.InternalDeliverMessageArgs;
import sd2526.trab.api.grpc.Messages.InternalRemoveDeliveredMessageArgs;
import sd2526.trab.api.java.Messages;
import sd2526.trab.api.java.Result;
import sd2526.trab.clients.UserDirectory;
import sd2526.trab.server.persistence.Hibernate;
import sd2526.trab.server.persistence.InboxMessageRecord;
import sd2526.trab.server.persistence.RemovedInboxMessageRecord;
import sd2526.trab.server.persistence.SentMessageRecord;

public class MessagesService implements Messages {

  private static final long DELETE_WINDOW_MS = 30_000L;
  private static final int REMOTE_RETRY_SLEEP_MS = 250;
  private static final int REMOTE_MAX_RETRIES = 15;
  private static final long REMOTE_DELIVERY_TIMEOUT_MS = 90_000L;
  private static final int REMOTE_CALL_TIMEOUT_MS = 500;

  private final String domain;
  private final UserDirectory userDirectory;
  private final Discovery discovery;
  private final boolean payloadPostIdempotence;
  private final Map<String, ManagedChannel> grpcChannels;
  private final Map<String, RemoteDomainQueue> remoteDomainQueues;
  private final Map<String, RecentPost> recentPostIds;

  public MessagesService(String domain, UserDirectory userDirectory, Discovery discovery) {
    this(domain, userDirectory, discovery, false);
  }

  public MessagesService(String domain, UserDirectory userDirectory, Discovery discovery, boolean payloadPostIdempotence) {
    this.domain = domain;
    this.userDirectory = userDirectory;
    this.discovery = discovery;
    this.payloadPostIdempotence = payloadPostIdempotence;
    this.grpcChannels = new java.util.concurrent.ConcurrentHashMap<>();
    this.remoteDomainQueues = new java.util.concurrent.ConcurrentHashMap<>();
    this.recentPostIds = new java.util.concurrent.ConcurrentHashMap<>();
  }

  @Override
  public Result<String> postMessage(String pwd, Message msg) {
    if (pwd == null || !isValidOutgoingMessage(msg)) {
      return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    String senderName = normalizeLocalUserName(msg.getSender());
    Result<User> senderRes = authenticate(senderName, pwd);
    if (!senderRes.isOK()) {
      return Result.error(senderRes.error());
    }
    User senderUser = senderRes.value();

    String formattedSender = senderUser.getDisplayName() + " <" + senderUser.getName() + "@" + domain + ">";
    String idempotencyKey = postIdempotencyKey(senderName, msg);
    if (idempotencyKey != null) {
      RecentPost existing = recentPostIds.get(idempotencyKey);
      if (existing != null) {
        return Result.ok(existing.mid);
      }
    }

    String mid = UUID.randomUUID().toString();

    Message canonical = copyMessage(msg);
    canonical.setId(mid);
    canonical.setSender(formattedSender);

    Set<String> destinations = canonical.getDestination();
    List<String> localDestinations = new ArrayList<>();
    List<String> remoteDestinations = new ArrayList<>();

    for (String dst : destinations) {
      ParsedAddress parsed = parseAddress(dst);
      if (parsed == null) {
        return Result.error(Result.ErrorCode.BAD_REQUEST);
      }

      if (domain.equals(parsed.domain)) {
        localDestinations.add(parsed.address());
      } else {
        remoteDestinations.add(parsed.address());
      }
    }

    for (String dst : localDestinations) {
      ParsedAddress parsed = parseAddress(dst);
      if (parsed == null) {
        continue;
      }

      Result<User> dstRes = userDirectory.getUserNoAuth(parsed.name);
      if (dstRes.isOK()) {
        Message delivered = copyMessage(canonical);
        putInInbox(parsed.name, delivered);
      } else {
        Message failed = createFailureMessage(canonical, dst, "UNKNOWN USER");
        putInInbox(senderName, failed);
      }
    }

    saveSentMessageInfo(new SentMessageInfo(
        mid,
        senderName,
        canonical.getCreationTime(),
        Collections.unmodifiableSet(canonical.getDestination())));

    for (String dst : remoteDestinations) {
      ParsedAddress parsed = parseAddress(dst);
      if (parsed == null) {
        continue;
      }

      enqueueRemoteTask(parsed.domain, new DeliverRemoteMessageTask(
          parsed.domain,
          parsed.name,
          dst,
          senderName,
          copyMessage(canonical)));
    }

    if (idempotencyKey != null) {
      recentPostIds.putIfAbsent(idempotencyKey, new RecentPost(mid));
    }
    recentPostIds.putIfAbsent(senderName + "\tID\t" + mid, new RecentPost(mid));

    return Result.ok(mid);
  }

  @Override
  public Result<Message> getInboxMessage(String name, String mid, String pwd) {
    if (isBlank(name) || isBlank(mid) || pwd == null) {
      return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    Result<User> authRes = authenticate(name, pwd);
    if (!authRes.isOK()) {
      return Result.error(authRes.error());
    }

    Message msg = findInboxMessage(name, mid);
    if (msg == null) {
      return Result.error(Result.ErrorCode.NOT_FOUND);
    }

    return Result.ok(copyMessage(msg));
  }

  @Override
  public Result<List<String>> getAllInboxMessages(String name, String pwd) {
    if (isBlank(name) || pwd == null) {
      return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    Result<User> authRes = authenticate(name, pwd);
    if (!authRes.isOK()) {
      return Result.error(authRes.error());
    }

    List<Message> messages = new ArrayList<>(listInboxMessages(name));
    if (messages.isEmpty()) {
      return Result.ok(new ArrayList<>());
    }
    messages.sort(Comparator.comparingLong(Message::getCreationTime));

    List<String> ids = new ArrayList<>(messages.size());
    for (Message m : messages) {
      ids.add(m.getId());
    }

    return Result.ok(ids);
  }

  @Override
  public Result<Void> removeInboxMessage(String name, String mid, String pwd) {
    if (isBlank(name) || isBlank(mid) || pwd == null) {
      return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    Result<User> authRes = authenticate(name, pwd);
    if (!authRes.isOK()) {
      return Result.error(authRes.error());
    }

    if (removeInboxEntryAndRemember(name, mid)) {
      return Result.ok();
    }

    return Result.error(Result.ErrorCode.NOT_FOUND);
  }

  @Override
  public Result<Void> deleteMessage(String name, String mid, String pwd) {
    if (isBlank(name) || isBlank(mid) || pwd == null) {
      return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    Result<User> authRes = authenticate(name, pwd);
    if (!authRes.isOK()) {
      return Result.error(authRes.error());
    }

    SentMessageInfo info = findSentMessageInfo(mid);

    if (info == null) {
      return Result.ok();
    }

    if (!name.equals(info.senderName)) {
      return Result.error(Result.ErrorCode.FORBIDDEN);
    }

    long now = System.currentTimeMillis();
    if (now - info.creationTime > DELETE_WINDOW_MS) {
      return Result.ok();
    }

    for (String dst : info.destinations) {
      ParsedAddress parsed = parseAddress(dst);
      if (parsed == null) {
        continue;
      }

      if (domain.equals(parsed.domain)) {
        removeInboxEntry(parsed.name, mid);
      } else {
        enqueueRemoteTask(parsed.domain, new DeleteRemoteMessageTask(parsed.domain, parsed.name, mid));
      }
    }

    return Result.ok();
  }

  @Override
  public Result<List<String>> searchInbox(String name, String pwd, String query) {
    if (isBlank(name) || pwd == null || query == null) {
      return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    Result<User> authRes = authenticate(name, pwd);
    if (!authRes.isOK()) {
      return Result.error(authRes.error());
    }

    List<Message> inbox = new ArrayList<>(listInboxMessages(name));
    if (inbox.isEmpty()) {
      return Result.ok(new ArrayList<>());
    }

    String q = query.toLowerCase(Locale.ROOT);
    List<Message> hits = new ArrayList<>();

    for (Message msg : inbox) {
      String subject = safeLower(msg.getSubject());
      String contents = safeLower(msg.getContents());

      if (subject.contains(q) || contents.contains(q)) {
        hits.add(msg);
      }
    }

    hits.sort(Comparator.comparingLong(Message::getCreationTime));

    List<String> ids = new ArrayList<>(hits.size());
    for (Message m : hits) {
      ids.add(m.getId());
    }

    return Result.ok(ids);
  }

  public Result<Void> deliverMessageToLocalInbox(String user, Message msg) {
    if (isBlank(user) || msg == null || isBlank(msg.getId())) {
      return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    Result<User> dstRes = userDirectory.getUserNoAuth(user);
    if (!dstRes.isOK()) {
      return Result.error(Result.ErrorCode.NOT_FOUND);
    }

    putInInbox(user, copyMessage(msg));
    return Result.ok();
  }

  public void deleteUserInbox(String user) {
    if (!isBlank(user)) {
      deleteInboxState(user);
    }
  }

  public Result<Void> removeDeliveredMessageFromInbox(String user, String mid) {
    if (isBlank(user) || isBlank(mid)) {
      return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    if (findInboxMessage(user, mid) == null) {
      return Result.error(Result.ErrorCode.NOT_FOUND);
    }

    return removeInboxEntryAndRemember(user, mid) ? Result.ok() : Result.error(Result.ErrorCode.NOT_FOUND);
  }

  private void putInInbox(String user, Message msg) {
    Hibernate.execute(session -> {
      String key = InboxMessageRecord.key(user, msg.getId());
      RemovedInboxMessageRecord removed = session.find(RemovedInboxMessageRecord.class,
          RemovedInboxMessageRecord.key(user, msg.getId()));
      if (removed != null) {
        return null;
      }
      if (session.find(InboxMessageRecord.class, key) == null) {
        session.persist(toInboxRecord(user, msg));
      }
      return null;
    });
  }

  private void rememberRemovedInboxMessage(String user, String mid) {
    Hibernate.execute(session -> {
      String key = RemovedInboxMessageRecord.key(user, mid);
      if (session.find(RemovedInboxMessageRecord.class, key) == null) {
        session.persist(new RemovedInboxMessageRecord(user, mid));
      }
      return null;
    });
  }

  private boolean wasInboxMessageRemoved(String user, String mid) {
    return Hibernate.execute(
        session -> session.find(RemovedInboxMessageRecord.class, RemovedInboxMessageRecord.key(user, mid)) != null);
  }

  private void forgetRemovedInboxMessage(String user, String mid) {
    Hibernate.execute(session -> {
      RemovedInboxMessageRecord removed = session.find(RemovedInboxMessageRecord.class,
          RemovedInboxMessageRecord.key(user, mid));
      if (removed != null) {
        session.remove(removed);
      }
      return null;
    });
  }

  private Message createFailureMessage(Message original, String failedUser, String reason) {
    Message m = new Message();
    m.setId(original.getId() + "." + failedUser);
    m.setSender(original.getSender());
    m.setDestination(Set.of(extractSenderAddress(original.getSender())));
    m.setCreationTime(original.getCreationTime());
    m.setSubject("FAILED TO SEND " + original.getId() + " TO " + failedUser + ": " + reason);
    m.setContents(original.getContents());
    return m;
  }

  private String extractSenderAddress(String formattedSender) {
    if (formattedSender == null) {
      return null;
    }

    int start = formattedSender.indexOf('<');
    int end = formattedSender.indexOf('>');
    if (start >= 0 && end > start) {
      return formattedSender.substring(start + 1, end).trim();
    }

    ParsedAddress parsed = parseAddress(formattedSender);
    return parsed == null ? formattedSender : parsed.address();
  }

  private boolean isValidOutgoingMessage(Message msg) {
    if (msg == null) {
      return false;
    }

    if (isBlank(msg.getSender())
        || msg.getDestination() == null
        || msg.getDestination().isEmpty()
        || msg.getSubject() == null
        || msg.getContents() == null) {
      return false;
    }

    for (String dst : msg.getDestination()) {
      if (isBlank(dst) || parseAddress(dst) == null) {
        return false;
      }
    }

    return parseAddress(msg.getSender()) != null;
  }

  private String normalizeLocalUserName(String sender) {
    ParsedAddress parsed = parseAddress(sender);
    if (parsed == null) {
      return null;
    }

    if (parsed.domain == null || parsed.domain.equals(domain)) {
      return parsed.name;
    }

    return null;
  }

  private ParsedAddress parseAddress(String address) {
    if (isBlank(address)) {
      return null;
    }

    String trimmed = address.trim();
    int at = trimmed.indexOf('@');

    if (at < 0) {
      return new ParsedAddress(trimmed, domain);
    }

    if (at == 0 || at == trimmed.length() - 1 || trimmed.indexOf('@', at + 1) >= 0) {
      return null;
    }

    String name = trimmed.substring(0, at).trim();
    String dom = trimmed.substring(at + 1).trim();

    if (isBlank(name) || isBlank(dom)) {
      return null;
    }

    return new ParsedAddress(name, dom);
  }

  private Message copyMessage(Message msg) {
    Message copy = new Message();
    copy.setId(msg.getId());
    copy.setSender(msg.getSender());
    copy.setDestination(msg.getDestination() == null ? Set.of() : new LinkedHashSet<>(msg.getDestination()));
    copy.setCreationTime(msg.getCreationTime());
    copy.setSubject(msg.getSubject());
    copy.setContents(msg.getContents());
    return copy;
  }

  private String safeLower(String s) {
    return s == null ? "" : s.toLowerCase(Locale.ROOT);
  }

  private boolean isBlank(String s) {
    return s == null || s.isBlank();
  }

  private String postIdempotencyKey(String senderName, Message msg) {
    if (hasExplicitPostId(msg)) {
      return senderName + "\tID\t" + msg.getId();
    }

    if (!payloadPostIdempotence) {
      return null;
    }

    Set<String> destinations = msg.getDestination() == null ? Set.of() : new java.util.TreeSet<>(msg.getDestination());
    return senderName
        + "\tSUB\t" + Objects.toString(msg.getSubject(), "")
        + "\tBODY\t" + Objects.toString(msg.getContents(), "")
        + "\tDST\t" + destinations;
  }

  private boolean hasExplicitPostId(Message msg) {
    return !isBlank(msg.getId());
  }

  private List<Message> listInboxMessages(String owner) {
    return new ArrayList<>(Hibernate.execute(session -> session
        .createQuery("from InboxMessageRecord where owner = :owner", InboxMessageRecord.class)
        .setParameter("owner", owner)
        .list()
        .stream()
        .map(this::fromInboxRecord)
        .toList()));
  }

  private Message findInboxMessage(String owner, String mid) {
    return Hibernate.execute(session -> fromInboxRecord(session.find(InboxMessageRecord.class,
        InboxMessageRecord.key(owner, mid))));
  }

  private boolean removeInboxEntry(String owner, String mid) {
    return Hibernate.execute(session -> {
      InboxMessageRecord record = session.find(InboxMessageRecord.class, InboxMessageRecord.key(owner, mid));
      if (record == null) {
        return false;
      }
      session.remove(record);
      return true;
    });
  }

  private boolean removeInboxEntryAndRemember(String owner, String mid) {
    return Hibernate.execute(session -> {
      String key = InboxMessageRecord.key(owner, mid);
      String removedKey = RemovedInboxMessageRecord.key(owner, mid);

      InboxMessageRecord record = session.find(InboxMessageRecord.class, key);
      RemovedInboxMessageRecord removed = session.find(RemovedInboxMessageRecord.class, removedKey);

      if (record == null) {
        return removed != null;
      }

      session.remove(record);
      if (removed == null) {
        session.persist(new RemovedInboxMessageRecord(owner, mid));
      }
      return true;
    });
  }

  private void deleteInboxState(String owner) {
    Hibernate.execute(session -> {
      session.createMutationQuery("delete from InboxMessageRecord where owner = :owner")
          .setParameter("owner", owner)
          .executeUpdate();
      session.createMutationQuery("delete from RemovedInboxMessageRecord where owner = :owner")
          .setParameter("owner", owner)
          .executeUpdate();
      return null;
    });
  }

  private void saveSentMessageInfo(SentMessageInfo info) {
    Hibernate.execute(session -> {
      if (session.find(SentMessageRecord.class, info.id) == null) {
        session.persist(new SentMessageRecord(info.id, info.senderName, info.creationTime, info.destinations));
      }
      return null;
    });
  }

  private SentMessageInfo findSentMessageInfo(String mid) {
    return Hibernate.execute(session -> fromSentRecord(session.find(SentMessageRecord.class, mid)));
  }

  private List<SentMessageInfo> listSentMessagesBySender(String senderName) {
    return Hibernate.execute(session -> session
        .createQuery("from SentMessageRecord where senderName = :senderName", SentMessageRecord.class)
        .setParameter("senderName", senderName)
        .list()
        .stream()
        .map(this::fromSentRecord)
        .toList());
  }

  private InboxMessageRecord toInboxRecord(String owner, Message msg) {
    return new InboxMessageRecord(
        owner,
        msg.getId(),
        msg.getSender(),
        msg.getDestination() == null ? Set.of() : new LinkedHashSet<>(msg.getDestination()),
        msg.getCreationTime(),
        msg.getSubject(),
        msg.getContents());
  }

  private Message fromInboxRecord(InboxMessageRecord record) {
    if (record == null) {
      return null;
    }

    Message msg = new Message();
    msg.setId(record.getMid());
    msg.setSender(record.getSender());
    msg.setDestination(new LinkedHashSet<>(record.getDestinations()));
    msg.setCreationTime(record.getCreationTime());
    msg.setSubject(record.getSubject());
    msg.setContents(record.getContents());
    return msg;
  }

  private SentMessageInfo fromSentRecord(SentMessageRecord record) {
    if (record == null) {
      return null;
    }

    return new SentMessageInfo(
        record.getId(),
        record.getSenderName(),
        record.getCreationTime(),
        Collections.unmodifiableSet(new LinkedHashSet<>(record.getDestinations())));
  }

  private boolean deliverRemoteMessage(String baseUri, String user, Message msg) {
    if (baseUri.startsWith("grpc://")) {
      try {
        grpcMessagesStub(baseUri)
            .withDeadlineAfter(REMOTE_CALL_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .internalDeliverMessage(InternalDeliverMessageArgs.newBuilder()
                .setUser(user)
                .setMessage(toGrpcMessage(msg))
                .build());
        return true;
      } catch (StatusRuntimeException e) {
        if (e.getStatus().getCode() == io.grpc.Status.Code.NOT_FOUND) {
          return false;
        }
        throw e;
      }
    }

    String target = baseUri + "/messages/internal/" + user;

    var client = jakarta.ws.rs.client.ClientBuilder.newBuilder()
        .connectTimeout(REMOTE_CALL_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .readTimeout(REMOTE_CALL_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .build();
    try {
      var response = client.target(target)
          .request()
          .post(jakarta.ws.rs.client.Entity.entity(msg, MediaType.APPLICATION_JSON));

      try {
        int status = response.getStatus();
        return switch (status) {
          case 200, 204 -> true;
          case 404 -> false;
          default -> throw new jakarta.ws.rs.ProcessingException("Remote delivery returned HTTP " + status);
        };
      } finally {
        response.close();
      }
    } finally {
      client.close();
    }
  }

  private void removeRemoteDeliveredMessage(String baseUri, String user, String mid) {
    if (baseUri.startsWith("grpc://")) {
      grpcMessagesStub(baseUri)
          .withDeadlineAfter(REMOTE_CALL_TIMEOUT_MS, TimeUnit.MILLISECONDS)
          .internalRemoveDeliveredMessage(InternalRemoveDeliveredMessageArgs.newBuilder()
              .setUser(user)
              .setMid(mid)
              .build());
      return;
    }

    String target = baseUri + "/messages/internal/" + user + "/" + mid;
    var client = jakarta.ws.rs.client.ClientBuilder.newBuilder()
        .connectTimeout(REMOTE_CALL_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .readTimeout(REMOTE_CALL_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .build();
    try {
      var response = client.target(target)
          .request()
          .delete();
      try {
        int status = response.getStatus();
        if (status != 200 && status != 204 && status != 404) {
          throw new jakarta.ws.rs.ProcessingException("Remote delete returned HTTP " + status);
        }
      } finally {
        response.close();
      }
    } finally {
      client.close();
    }
  }

  private String lookupRemoteServiceUri(String serviceName, String targetDomain) {
    var opt = discovery.getService(serviceName, targetDomain);
    if (opt.isPresent()) {
      return opt.get().getUri();
    }

    return fallbackServiceUri(serviceName, targetDomain);
  }

  private String fallbackServiceUri(String serviceName, String targetDomain) {
    var localOpt = discovery.getService(serviceName, domain);
    boolean grpc = localOpt.isPresent() && localOpt.get().getUri().startsWith("grpc://");

    if (Objects.equals(serviceName, Messages.SERVICE_NAME)) {
      return grpc
          ? "grpc://messages0." + targetDomain + ":8083/grpc"
          : "http://messages0." + targetDomain + ":8080/rest";
    }

    if (Objects.equals(serviceName, sd2526.trab.api.java.Users.SERVICE_NAME)) {
      return grpc
          ? "grpc://users0." + targetDomain + ":8082/grpc"
          : "http://users0." + targetDomain + ":8081/rest";
    }

    return null;
  }

  private GrpcMessagesGrpc.GrpcMessagesBlockingStub grpcMessagesStub(String uri) {
    String target = uri.substring("grpc://".length());
    int slash = target.indexOf('/');
    if (slash >= 0) {
      target = target.substring(0, slash);
    }

    ManagedChannel channel = grpcChannels.computeIfAbsent(target,
        key -> ManagedChannelBuilder.forTarget(key).usePlaintext().build());
    return GrpcMessagesGrpc.newBlockingStub(channel);
  }

  private void enqueueRemoteTask(String targetDomain, RemoteTask task) {
    remoteDomainQueues.computeIfAbsent(targetDomain, this::createRemoteDomainQueue)
        .enqueue(task);
  }

  private RemoteDomainQueue createRemoteDomainQueue(String targetDomain) {
    RemoteDomainQueue queue = new RemoteDomainQueue(targetDomain);
    queue.start();
    return queue;
  }

  private interface RemoteTask {
    boolean runStep();
  }

  private class RemoteDomainQueue {
    private final BlockingQueue<RemoteTask> queue;
    private final Thread worker;

    RemoteDomainQueue(String targetDomain) {
      this.queue = new LinkedBlockingQueue<>();
      this.worker = new Thread(() -> runRemoteQueue(), "messages-remote-" + targetDomain);
      this.worker.setDaemon(true);
    }

    void start() {
      worker.start();
    }

    void enqueue(RemoteTask task) {
      queue.offer(task);
    }

    private void runRemoteQueue() {
      while (true) {
        try {
          RemoteTask task = queue.take();
          while (!task.runStep()) {
            sleepQuietly(REMOTE_RETRY_SLEEP_MS);
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        } catch (RuntimeException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private class DeliverRemoteMessageTask implements RemoteTask {
    private final String targetDomain;
    private final String user;
    private final String destinationAddress;
    private final String senderName;
    private final Message message;
    private final long startedAt;

    DeliverRemoteMessageTask(String targetDomain, String user, String destinationAddress, String senderName,
        Message message) {
      this.targetDomain = targetDomain;
      this.user = user;
      this.destinationAddress = destinationAddress;
      this.senderName = senderName;
      this.message = message;
      this.startedAt = System.currentTimeMillis();
    }

    @Override
    public boolean runStep() {
      String baseUri = lookupRemoteServiceUri(Messages.SERVICE_NAME, targetDomain);
      if (baseUri == null) {
        return handleRetryTimeout();
      }

      try {
        boolean delivered = deliverRemoteMessage(baseUri, user, message);
        if (!delivered) {
          putInInbox(senderName, createFailureMessage(message, destinationAddress, "UNKNOWN USER"));
        }
        return true;
      } catch (StatusRuntimeException | jakarta.ws.rs.ProcessingException e) {
        return handleRetryTimeout();
      }
    }

    private boolean handleRetryTimeout() {
      if (System.currentTimeMillis() - startedAt < REMOTE_DELIVERY_TIMEOUT_MS) {
        return false;
      }

      putInInbox(senderName, createFailureMessage(message, destinationAddress, "TIMEOUT"));
      return true;
    }
  }

  private class DeleteRemoteMessageTask implements RemoteTask {
    private final String targetDomain;
    private final String user;
    private final String mid;

    DeleteRemoteMessageTask(String targetDomain, String user, String mid) {
      this.targetDomain = targetDomain;
      this.user = user;
      this.mid = mid;
    }

    @Override
    public boolean runStep() {
      String baseUri = lookupRemoteServiceUri(Messages.SERVICE_NAME, targetDomain);
      if (baseUri == null) {
        return false;
      }

      try {
        removeRemoteDeliveredMessage(baseUri, user, mid);
        return true;
      } catch (StatusRuntimeException | jakarta.ws.rs.ProcessingException e) {
        return false;
      }
    }
  }

  private sd2526.trab.api.grpc.Messages.GrpcMessage toGrpcMessage(Message message) {
    return sd2526.trab.api.grpc.Messages.GrpcMessage.newBuilder()
        .setId(message.getId() == null ? "" : message.getId())
        .setSender(message.getSender() == null ? "" : message.getSender())
        .addAllDestination(message.getDestination() == null ? java.util.List.of() : message.getDestination())
        .setCreationTime(message.getCreationTime())
        .setSubject(message.getSubject() == null ? "" : message.getSubject())
        .setContents(message.getContents() == null ? "" : message.getContents())
        .build();
  }

  private static class ParsedAddress {
    final String name;
    final String domain;

    ParsedAddress(String name, String domain) {
      this.name = name;
      this.domain = domain;
    }

    String address() {
      return name + "@" + domain;
    }
  }

  private static class SentMessageInfo {
    final String id;
    final String senderName;
    final long creationTime;
    final Set<String> destinations;

    SentMessageInfo(String id, String senderName, long creationTime, Set<String> destinations) {
      this.id = id;
      this.senderName = senderName;
      this.creationTime = creationTime;
      this.destinations = destinations;
    }
  }

  private static class RecentPost {
    final String mid;

    RecentPost(String mid) {
      this.mid = mid;
    }
  }

  private Result<User> authenticate(String name, String pwd) {
    Result<User> res = userDirectory.getUser(name, pwd);

    if (res.isOK()) {
      return res;
    }

    if (res.error() == Result.ErrorCode.FORBIDDEN) {
      return Result.error(Result.ErrorCode.FORBIDDEN);
    }

    return Result.error(Result.ErrorCode.TIMEOUT);
  }

  private void sleepQuietly(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

}
