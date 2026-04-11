package sd2526.trab.server;

import java.util.List;
import java.util.concurrent.TimeUnit;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sd2526.trab.api.Message;
import sd2526.trab.api.User;
import sd2526.trab.api.java.Messages;
import sd2526.trab.api.java.Result;
import sd2526.trab.api.java.Users;

public class GatewayService {

  private static final int CONNECT_TIMEOUT_MS = 10000;
  private static final int READ_TIMEOUT_MS = 10000;
  private static final int RETRY_SLEEP_MS = 1000;
  private static final int MAX_RETRIES = 15;
  private static final GenericType<List<User>> USER_LIST_TYPE = new GenericType<>() {
  };
  private static final GenericType<List<String>> STRING_LIST_TYPE = new GenericType<>() {
  };

  private final Discovery discovery;
  private final String domain;
  private final Client client;

  public GatewayService(Discovery discovery, String domain) {
    this.discovery = discovery;
    this.domain = domain;
    this.client = ClientBuilder.newBuilder()
        .connectTimeout(CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .readTimeout(READ_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        .build();
  }

  public Result<String> postUser(User user) {
    return withUsersBase(base -> {
      Response r = client.target(base)
          .path("users")
          .request()
          .post(Entity.entity(user, MediaType.APPLICATION_JSON));
      return mapResponse(r, String.class);
    });
  }

  public Result<User> getUser(String name, String pwd) {
    return withUsersBase(base -> {
      Response r = client.target(base)
          .path("users")
          .path(name)
          .queryParam("pwd", pwd)
          .request()
          .get();
      return mapResponse(r, User.class);
    });
  }

  public Result<User> updateUser(String name, String pwd, User info) {
    return withUsersBase(base -> {
      Response r = client.target(base)
          .path("users")
          .path(name)
          .queryParam("pwd", pwd)
          .request()
          .put(Entity.entity(info, MediaType.APPLICATION_JSON));
      return mapResponse(r, User.class);
    });
  }

  public Result<User> deleteUser(String name, String pwd) {
    return withUsersBase(base -> {
      Response r = client.target(base)
          .path("users")
          .path(name)
          .queryParam("pwd", pwd)
          .request()
          .delete();
      return mapResponse(r, User.class);
    });
  }

  public Result<List<User>> searchUsers(String name, String pwd, String pattern) {
    return withUsersBase(base -> {
      Response r = client.target(base)
          .path("users")
          .queryParam("name", name)
          .queryParam("pwd", pwd)
          .queryParam("query", pattern)
          .request()
          .get();
      return mapResponse(r, USER_LIST_TYPE);
    });
  }

  public Result<String> postMessage(String pwd, Message msg) {
    return withMessagesBase(base -> {
      Response r = client.target(base)
          .path("messages")
          .queryParam("pwd", pwd)
          .request()
          .post(Entity.entity(msg, MediaType.APPLICATION_JSON));
      return mapResponse(r, String.class);
    });
  }

  public Result<Message> getInboxMessage(String name, String mid, String pwd) {
    return withMessagesBase(base -> {
      Response r = client.target(base)
          .path("messages")
          .path("mbox")
          .path(name)
          .path(mid)
          .queryParam("pwd", pwd)
          .request()
          .get();
      return mapResponse(r, Message.class);
    });
  }

  public Result<List<String>> getAllInboxMessages(String name, String pwd) {
    return withMessagesBase(base -> {
      Response r = client.target(base)
          .path("messages")
          .path("mbox")
          .path(name)
          .queryParam("pwd", pwd)
          .request()
          .get();
      return mapResponse(r, STRING_LIST_TYPE);
    });
  }

  public Result<List<String>> searchInbox(String name, String pwd, String query) {
    return withMessagesBase(base -> {
      Response r = client.target(base)
          .path("messages")
          .path("mbox")
          .path(name)
          .queryParam("pwd", pwd)
          .queryParam("query", query)
          .request()
          .get();
      return mapResponse(r, STRING_LIST_TYPE);
    });
  }

  public Result<Void> removeInboxMessage(String name, String mid, String pwd) {
    return withMessagesBase(base -> {
      Response r = client.target(base)
          .path("messages")
          .path("mbox")
          .path(name)
          .path(mid)
          .queryParam("pwd", pwd)
          .request()
          .delete();
      return mapVoidResponse(r);
    });
  }

  public Result<Void> deleteMessage(String name, String mid, String pwd) {
    return withMessagesBase(base -> {
      Response r = client.target(base)
          .path("messages")
          .path(name)
          .path(mid)
          .queryParam("pwd", pwd)
          .request()
          .delete();
      return mapVoidResponse(r);
    });
  }

  private <T> Result<T> withUsersBase(ServiceCall<T> call) {
    return withServiceBase(Users.SERVICE_NAME, "http://users0." + domain + ":8081/rest", call);
  }

  private <T> Result<T> withMessagesBase(ServiceCall<T> call) {
    return withServiceBase(Messages.SERVICE_NAME, "http://messages0." + domain + ":8080/rest", call);
  }

  private <T> Result<T> withServiceBase(String serviceName, String fallbackUri, ServiceCall<T> call) {
    for (int i = 0; i < MAX_RETRIES; i++) {
      String base = discovery.getServiceURI(serviceName, domain).orElse(null);
      if (base == null) {
        sleepQuietly(RETRY_SLEEP_MS);
        continue;
      }

      try {
        return call.execute(base);
      } catch (ProcessingException e) {
        sleepQuietly(RETRY_SLEEP_MS);
      }
    }

    try {
      return call.execute(fallbackUri);
    } catch (ProcessingException e) {
      return Result.error(Result.ErrorCode.TIMEOUT);
    }
  }

  private <T> Result<T> mapResponse(Response r, Class<T> type) {
    try {
      return switch (r.getStatus()) {
        case 200 -> Result.ok(r.readEntity(type));
        case 400 -> Result.error(Result.ErrorCode.BAD_REQUEST);
        case 403 -> Result.error(Result.ErrorCode.FORBIDDEN);
        case 404 -> Result.error(Result.ErrorCode.NOT_FOUND);
        case 408 -> Result.error(Result.ErrorCode.TIMEOUT);
        case 409 -> Result.error(Result.ErrorCode.CONFLICT);
        case 501 -> Result.error(Result.ErrorCode.NOT_IMPLEMENTED);
        default -> Result.error(Result.ErrorCode.INTERNAL_ERROR);
      };
    } finally {
      r.close();
    }
  }

  private <T> Result<T> mapResponse(Response r, GenericType<T> type) {
    try {
      return switch (r.getStatus()) {
        case 200 -> Result.ok(r.readEntity(type));
        case 400 -> Result.error(Result.ErrorCode.BAD_REQUEST);
        case 403 -> Result.error(Result.ErrorCode.FORBIDDEN);
        case 404 -> Result.error(Result.ErrorCode.NOT_FOUND);
        case 408 -> Result.error(Result.ErrorCode.TIMEOUT);
        case 409 -> Result.error(Result.ErrorCode.CONFLICT);
        case 501 -> Result.error(Result.ErrorCode.NOT_IMPLEMENTED);
        default -> Result.error(Result.ErrorCode.INTERNAL_ERROR);
      };
    } finally {
      r.close();
    }
  }

  private Result<Void> mapVoidResponse(Response r) {
    try {
      return switch (r.getStatus()) {
        case 200, 204 -> Result.ok();
        case 400 -> Result.error(Result.ErrorCode.BAD_REQUEST);
        case 403 -> Result.error(Result.ErrorCode.FORBIDDEN);
        case 404 -> Result.error(Result.ErrorCode.NOT_FOUND);
        case 408 -> Result.error(Result.ErrorCode.TIMEOUT);
        case 409 -> Result.error(Result.ErrorCode.CONFLICT);
        case 501 -> Result.error(Result.ErrorCode.NOT_IMPLEMENTED);
        default -> Result.error(Result.ErrorCode.INTERNAL_ERROR);
      };
    } finally {
      r.close();
    }
  }

  private void sleepQuietly(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  @FunctionalInterface
  private interface ServiceCall<T> {
    Result<T> execute(String baseUri);
  }
}
