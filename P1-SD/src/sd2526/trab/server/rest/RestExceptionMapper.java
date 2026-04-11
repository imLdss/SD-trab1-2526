package sd2526.trab.server.rest;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import sd2526.trab.api.java.Result;

public class RestExceptionMapper {

  private RestExceptionMapper() {
  }

  public static <T> T resultOrThrow(Result<T> result) {
    if (result.isOK()) {
      return result.value();
    }

    throw errorToException(result.error());
  }

  public static void voidResultOrThrow(Result<Void> result) {
    if (result.isOK()) {
      return;
    }

    throw errorToException(result.error());
  }

  public static WebApplicationException errorToException(Result.ErrorCode error) {
    Response.Status status = switch (error) {
      case BAD_REQUEST -> Response.Status.BAD_REQUEST;
      case FORBIDDEN -> Response.Status.FORBIDDEN;
      case NOT_FOUND -> Response.Status.NOT_FOUND;
      case CONFLICT -> Response.Status.CONFLICT;
      case NOT_IMPLEMENTED -> Response.Status.NOT_IMPLEMENTED;
      case TIMEOUT -> Response.Status.REQUEST_TIMEOUT;
      case INTERNAL_ERROR, OK -> Response.Status.INTERNAL_SERVER_ERROR;
    };

    return new WebApplicationException(status);
  }
}