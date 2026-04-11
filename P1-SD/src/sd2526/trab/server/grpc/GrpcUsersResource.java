package sd2526.trab.server.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import sd2526.trab.api.User;
import sd2526.trab.api.grpc.GrpcUsersGrpc;
import sd2526.trab.api.grpc.Users.DeleteUserArgs;
import sd2526.trab.api.grpc.Users.DeleteUserResult;
import sd2526.trab.api.grpc.Users.GetUserArgs;
import sd2526.trab.api.grpc.Users.GetUserInternalArgs;
import sd2526.trab.api.grpc.Users.GetUserResult;
import sd2526.trab.api.grpc.Users.GrpcUser;
import sd2526.trab.api.grpc.Users.PostUserResult;
import sd2526.trab.api.grpc.Users.SearchUsersArgs;
import sd2526.trab.api.grpc.Users.UpdateUserArgs;
import sd2526.trab.api.grpc.Users.UpdateUserResult;
import sd2526.trab.api.java.Result;
import sd2526.trab.server.UsersService;

public class GrpcUsersResource extends GrpcUsersGrpc.GrpcUsersImplBase {

  private final UsersService usersService;

  public GrpcUsersResource(UsersService usersService) {
    this.usersService = usersService;
  }

  @Override
  public void postUser(GrpcUser request, StreamObserver<PostUserResult> responseObserver) {
    Result<String> result = usersService.postUser(fromGrpcUser(request));
    if (!result.isOK()) {
      fail(responseObserver, result.error());
      return;
    }

    responseObserver.onNext(PostUserResult.newBuilder()
        .setUserAddress(result.value())
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void getUser(GetUserArgs request, StreamObserver<GetUserResult> responseObserver) {
    Result<User> result = usersService.getUser(request.getName(), request.getPwd());
    replyWithUser(result, responseObserver);
  }

  @Override
  public void getUserInternal(GetUserInternalArgs request, StreamObserver<GetUserResult> responseObserver) {
    Result<User> result = usersService.getUserInternalNoAuth(request.getName());
    replyWithUser(result, responseObserver);
  }

  @Override
  public void updateUser(UpdateUserArgs request, StreamObserver<UpdateUserResult> responseObserver) {
    Result<User> result = usersService.updateUser(
        request.getName(),
        request.getPwd(),
        fromGrpcUser(request.getInfo()));
    if (!result.isOK()) {
      fail(responseObserver, result.error());
      return;
    }

    responseObserver.onNext(UpdateUserResult.newBuilder()
        .setUser(toGrpcUser(result.value()))
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void deleteUser(DeleteUserArgs request, StreamObserver<DeleteUserResult> responseObserver) {
    Result<User> result = usersService.deleteUser(request.getName(), request.getPwd());
    if (!result.isOK()) {
      fail(responseObserver, result.error());
      return;
    }

    responseObserver.onNext(DeleteUserResult.newBuilder()
        .setUser(toGrpcUser(result.value()))
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void searchUsers(SearchUsersArgs request, StreamObserver<GrpcUser> responseObserver) {
    Result<java.util.List<User>> result = usersService.searchUsers(
        request.getName(),
        request.getPwd(),
        request.getQuery());

    if (!result.isOK()) {
      fail(responseObserver, result.error());
      return;
    }

    for (User user : result.value()) {
      responseObserver.onNext(toGrpcUser(user));
    }
    responseObserver.onCompleted();
  }

  private void replyWithUser(Result<User> result, StreamObserver<GetUserResult> responseObserver) {
    if (!result.isOK()) {
      fail(responseObserver, result.error());
      return;
    }

    responseObserver.onNext(GetUserResult.newBuilder()
        .setUser(toGrpcUser(result.value()))
        .build());
    responseObserver.onCompleted();
  }

  private void fail(StreamObserver<?> responseObserver, Result.ErrorCode error) {
    Status status = switch (error) {
      case BAD_REQUEST -> Status.INVALID_ARGUMENT;
      case FORBIDDEN -> Status.PERMISSION_DENIED;
      case NOT_FOUND -> Status.NOT_FOUND;
      case CONFLICT -> Status.ALREADY_EXISTS;
      case TIMEOUT -> Status.DEADLINE_EXCEEDED;
      case NOT_IMPLEMENTED -> Status.UNIMPLEMENTED;
      case INTERNAL_ERROR, OK -> Status.INTERNAL;
    };

    responseObserver.onError(status.asRuntimeException());
  }

  private GrpcUser toGrpcUser(User user) {
    var builder = GrpcUser.newBuilder()
        .setName(user.getName());

    if (user.getPwd() != null) {
      builder.setPwd(user.getPwd());
    }
    if (user.getDisplayName() != null) {
      builder.setDisplayName(user.getDisplayName());
    }
    if (user.getDomain() != null) {
      builder.setDomain(user.getDomain());
    }

    return builder.build();
  }

  private User fromGrpcUser(GrpcUser user) {
    User result = new User();
    result.setName(user.getName().isEmpty() ? null : user.getName());
    result.setPwd(user.hasPwd() ? user.getPwd() : null);
    result.setDisplayName(user.hasDisplayName() ? user.getDisplayName() : null);
    result.setDomain(user.hasDomain() ? user.getDomain() : null);
    return result;
  }
}
