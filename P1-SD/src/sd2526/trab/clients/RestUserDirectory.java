package sd2526.trab.clients;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import sd2526.trab.api.User;
import sd2526.trab.api.grpc.GrpcUsersGrpc;
import sd2526.trab.api.grpc.Users.GetUserArgs;
import sd2526.trab.api.grpc.Users.GetUserInternalArgs;
import sd2526.trab.api.java.Result;
import sd2526.trab.server.Discovery;

public class RestUserDirectory implements UserDirectory {

  private static final int CONNECT_TIMEOUT_MS = 500;
  private static final int READ_TIMEOUT_MS = 500;
  private static final int RETRY_SLEEP_MS = 250;
  private static final int MAX_RETRIES = 12;

  private final Discovery discovery;
  private final String domain;
  private final boolean preferGrpc;
  private final Client client;
  private final java.util.concurrent.ConcurrentHashMap<String, ManagedChannel> grpcChannels;

  public RestUserDirectory(Discovery discovery, String domain) {
    this(discovery, domain, false);
  }

  public RestUserDirectory(Discovery discovery, String domain, boolean preferGrpc) {
    this.discovery = discovery;
    this.domain = domain;
    this.preferGrpc = preferGrpc;
    this.client = ClientBuilder.newBuilder()
        .connectTimeout(CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .readTimeout(READ_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .build();
    this.grpcChannels = new java.util.concurrent.ConcurrentHashMap<>();
  }

  @Override
  public Result<User> getUser(String name, String pwd) {
    for (String base : lookupUsersServiceUris()) {
      Result<User> result = base.startsWith("grpc://")
          ? getUserGrpc(base, name, pwd)
          : getUserRest(base, name, pwd);
      if (result.error() != Result.ErrorCode.TIMEOUT) {
        return result;
      }
    }

    return Result.error(Result.ErrorCode.TIMEOUT);
  }

  @Override
  public Result<User> getUserNoAuth(String name) {
    for (String base : lookupUsersServiceUris()) {
      Result<User> result = base.startsWith("grpc://")
          ? getUserNoAuthGrpc(base, name)
          : getUserNoAuthRest(base, name);
      if (result.error() != Result.ErrorCode.TIMEOUT) {
        return result;
      }
    }

    return Result.error(Result.ErrorCode.TIMEOUT);
  }

  private Result<User> getUserRest(String base, String name, String pwd) {
    for (int i = 0; i < MAX_RETRIES; i++) {
      try {
        Response r = client.target(base)
            .path("users")
            .path(name)
            .queryParam("pwd", pwd)
            .request()
            .get();

        try {
          if (isRetryableStatus(r.getStatus())) {
            sleepQuietly(RETRY_SLEEP_MS);
            continue;
          }
          return mapUserResponse(r);
        } finally {
          r.close();
        }
      } catch (ProcessingException e) {
        sleepQuietly(RETRY_SLEEP_MS);
      }
    }

    return Result.error(Result.ErrorCode.TIMEOUT);
  }

  private Result<User> getUserNoAuthRest(String base, String name) {
    for (int i = 0; i < MAX_RETRIES; i++) {
      try {
        Response r = client.target(base)
            .path("users")
            .path("internal")
            .path(name)
            .request()
            .get();

        try {
          if (isRetryableStatus(r.getStatus())) {
            sleepQuietly(RETRY_SLEEP_MS);
            continue;
          }
          return mapUserResponse(r);
        } finally {
          r.close();
        }
      } catch (ProcessingException e) {
        sleepQuietly(RETRY_SLEEP_MS);
      }
    }

    return Result.error(Result.ErrorCode.TIMEOUT);
  }

  private Result<User> mapUserResponse(Response r) {
    return switch (r.getStatus()) {
      case 200 -> Result.ok(r.readEntity(User.class));
      case 400 -> Result.error(Result.ErrorCode.BAD_REQUEST);
      case 403 -> Result.error(Result.ErrorCode.FORBIDDEN);
      case 404 -> Result.error(Result.ErrorCode.NOT_FOUND);
      case 409 -> Result.error(Result.ErrorCode.CONFLICT);
      default -> Result.error(Result.ErrorCode.INTERNAL_ERROR);
    };
  }

  private boolean isRetryableStatus(int status) {
    return status == 408 || status == 429 || status >= 500;
  }

  private Result<User> getUserGrpc(String uri, String name, String pwd) {
    for (int i = 0; i < MAX_RETRIES; i++) {
      try {
        var result = grpcStub(uri)
            .withDeadlineAfter(READ_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .getUser(GetUserArgs.newBuilder()
                .setName(name)
                .setPwd(pwd)
                .build());

        return Result.ok(fromGrpcUser(result.getUser()));
      } catch (StatusRuntimeException e) {
        Result<User> mapped = mapGrpcException(e);
        if (mapped.error() != Result.ErrorCode.TIMEOUT) {
          return mapped;
        }
        sleepQuietly(RETRY_SLEEP_MS);
      }
    }

    return Result.error(Result.ErrorCode.TIMEOUT);
  }

  private Result<User> getUserNoAuthGrpc(String uri, String name) {
    for (int i = 0; i < MAX_RETRIES; i++) {
      try {
        var result = grpcStub(uri)
            .withDeadlineAfter(READ_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .getUserInternal(GetUserInternalArgs.newBuilder()
                .setName(name)
                .build());

        return Result.ok(fromGrpcUser(result.getUser()));
      } catch (StatusRuntimeException e) {
        Result<User> mapped = mapGrpcException(e);
        if (mapped.error() != Result.ErrorCode.TIMEOUT) {
          return mapped;
        }
        sleepQuietly(RETRY_SLEEP_MS);
      }
    }

    return Result.error(Result.ErrorCode.TIMEOUT);
  }

  private GrpcUsersGrpc.GrpcUsersBlockingStub grpcStub(String uri) {
    String target = uri.substring("grpc://".length());
    int slash = target.indexOf('/');
    if (slash >= 0) {
      target = target.substring(0, slash);
    }
    ManagedChannel channel = grpcChannels.computeIfAbsent(target,
        key -> ManagedChannelBuilder.forTarget(key).usePlaintext().build());
    return GrpcUsersGrpc.newBlockingStub(channel);
  }

  private Result<User> mapGrpcException(StatusRuntimeException e) {
    return switch (e.getStatus().getCode()) {
      case INVALID_ARGUMENT -> Result.error(Result.ErrorCode.BAD_REQUEST);
      case PERMISSION_DENIED -> Result.error(Result.ErrorCode.FORBIDDEN);
      case NOT_FOUND -> Result.error(Result.ErrorCode.NOT_FOUND);
      case ALREADY_EXISTS -> Result.error(Result.ErrorCode.CONFLICT);
      case DEADLINE_EXCEEDED, UNAVAILABLE -> Result.error(Result.ErrorCode.TIMEOUT);
      case UNIMPLEMENTED -> Result.error(Result.ErrorCode.NOT_IMPLEMENTED);
      default -> Result.error(Result.ErrorCode.INTERNAL_ERROR);
    };
  }

  private User fromGrpcUser(sd2526.trab.api.grpc.Users.GrpcUser user) {
    return new User(
        user.getName(),
        user.getPwd(),
        user.getDisplayName(),
        user.getDomain());
  }

  private void sleepQuietly(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private List<String> lookupUsersServiceUris() {
    LinkedHashSet<String> uris = new LinkedHashSet<>();

    discovery.getServiceURI("Users", domain).ifPresent(uris::add);

    List<String> fallbacks = new ArrayList<>(2);
    if (preferGrpc) {
      fallbacks.add("grpc://users0." + domain + ":8082/grpc");
      fallbacks.add("http://users0." + domain + ":8081/rest");
    } else {
      fallbacks.add("http://users0." + domain + ":8081/rest");
      fallbacks.add("grpc://users0." + domain + ":8082/grpc");
    }

    uris.addAll(fallbacks);
    return new ArrayList<>(uris);
  }
}
