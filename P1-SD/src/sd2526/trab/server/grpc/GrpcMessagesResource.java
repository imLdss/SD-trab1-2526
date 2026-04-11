package sd2526.trab.server.grpc;

import com.google.protobuf.Empty;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.util.LinkedHashSet;
import sd2526.trab.api.Message;
import sd2526.trab.api.grpc.GrpcMessagesGrpc;
import sd2526.trab.api.grpc.Messages.GetAllInboxMessagesArgs;
import sd2526.trab.api.grpc.Messages.GetAllInboxMessagesResult;
import sd2526.trab.api.grpc.Messages.GetInboxMessageArgs;
import sd2526.trab.api.grpc.Messages.GrpcMessage;
import sd2526.trab.api.grpc.Messages.InternalDeliverMessageArgs;
import sd2526.trab.api.grpc.Messages.InternalRemoveDeliveredMessageArgs;
import sd2526.trab.api.grpc.Messages.PostMessageArgs;
import sd2526.trab.api.grpc.Messages.PostMessageResult;
import sd2526.trab.api.grpc.Messages.RemoveInboxMessageArgs;
import sd2526.trab.api.grpc.Messages.DeleteMessageArgs;
import sd2526.trab.api.grpc.Messages.SearchInboxArgs;
import sd2526.trab.api.grpc.Messages.SearchInboxResult;
import sd2526.trab.api.java.Result;
import sd2526.trab.server.MessagesService;

public class GrpcMessagesResource extends GrpcMessagesGrpc.GrpcMessagesImplBase {

  private final MessagesService messagesService;

  public GrpcMessagesResource(MessagesService messagesService) {
    this.messagesService = messagesService;
  }

  @Override
  public void postMessage(PostMessageArgs request, StreamObserver<PostMessageResult> responseObserver) {
    Result<String> result = messagesService.postMessage(request.getPwd(), fromGrpcMessage(request.getMessage()));
    if (!result.isOK()) {
      fail(responseObserver, result.error());
      return;
    }

    responseObserver.onNext(PostMessageResult.newBuilder()
        .setMid(result.value())
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void getInboxMessage(GetInboxMessageArgs request, StreamObserver<GrpcMessage> responseObserver) {
    Result<Message> result = messagesService.getInboxMessage(request.getName(), request.getMid(), request.getPwd());
    if (!result.isOK()) {
      fail(responseObserver, result.error());
      return;
    }

    responseObserver.onNext(toGrpcMessage(result.value()));
    responseObserver.onCompleted();
  }

  @Override
  public void getAllInboxMessages(GetAllInboxMessagesArgs request,
      StreamObserver<GetAllInboxMessagesResult> responseObserver) {
    Result<java.util.List<String>> result = messagesService.getAllInboxMessages(request.getName(), request.getPwd());
    if (!result.isOK()) {
      fail(responseObserver, result.error());
      return;
    }

    responseObserver.onNext(GetAllInboxMessagesResult.newBuilder()
        .addAllMids(result.value())
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void removeInboxMessage(RemoveInboxMessageArgs request, StreamObserver<Empty> responseObserver) {
    Result<Void> result = messagesService.removeInboxMessage(request.getName(), request.getMid(), request.getPwd());
    if (!result.isOK()) {
      fail(responseObserver, result.error());
      return;
    }

    responseObserver.onNext(Empty.getDefaultInstance());
    responseObserver.onCompleted();
  }

  @Override
  public void deleteMessage(DeleteMessageArgs request, StreamObserver<Empty> responseObserver) {
    Result<Void> result = messagesService.deleteMessage(request.getName(), request.getMid(), request.getPwd());
    if (!result.isOK()) {
      fail(responseObserver, result.error());
      return;
    }

    responseObserver.onNext(Empty.getDefaultInstance());
    responseObserver.onCompleted();
  }

  @Override
  public void searchInbox(SearchInboxArgs request, StreamObserver<SearchInboxResult> responseObserver) {
    Result<java.util.List<String>> result = messagesService.searchInbox(
        request.getName(),
        request.getPwd(),
        request.getQuery());

    if (!result.isOK()) {
      fail(responseObserver, result.error());
      return;
    }

    responseObserver.onNext(SearchInboxResult.newBuilder()
        .addAllMids(result.value())
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void internalDeliverMessage(InternalDeliverMessageArgs request, StreamObserver<Empty> responseObserver) {
    Result<Void> result = messagesService.deliverMessageToLocalInbox(
        request.getUser(),
        fromGrpcMessage(request.getMessage()));

    if (!result.isOK()) {
      fail(responseObserver, result.error());
      return;
    }

    responseObserver.onNext(Empty.getDefaultInstance());
    responseObserver.onCompleted();
  }

  @Override
  public void internalRemoveDeliveredMessage(InternalRemoveDeliveredMessageArgs request,
      StreamObserver<Empty> responseObserver) {
    Result<Void> result = messagesService.removeDeliveredMessageFromInbox(request.getUser(), request.getMid());
    if (!result.isOK()) {
      fail(responseObserver, result.error());
      return;
    }

    responseObserver.onNext(Empty.getDefaultInstance());
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

  private GrpcMessage toGrpcMessage(Message message) {
    return GrpcMessage.newBuilder()
        .setId(message.getId() == null ? "" : message.getId())
        .setSender(message.getSender() == null ? "" : message.getSender())
        .addAllDestination(message.getDestination() == null ? java.util.List.of() : message.getDestination())
        .setCreationTime(message.getCreationTime())
        .setSubject(message.getSubject() == null ? "" : message.getSubject())
        .setContents(message.getContents() == null ? "" : message.getContents())
        .build();
  }

  private Message fromGrpcMessage(GrpcMessage message) {
    Message result = new Message();
    result.setId(message.getId().isEmpty() ? null : message.getId());
    result.setSender(message.getSender().isEmpty() ? null : message.getSender());
    result.setDestination(new LinkedHashSet<>(message.getDestinationList()));
    result.setCreationTime(message.getCreationTime());
    result.setSubject(message.getSubject());
    result.setContents(message.getContents());
    return result;
  }
}
