package sd2526.trab.api.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class GrpcMessagesGrpc {

  private GrpcMessagesGrpc() {}

  public static final java.lang.String SERVICE_NAME = "sd2526.trab.api.grpc.GrpcMessages";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.PostMessageArgs,
      sd2526.trab.api.grpc.Messages.PostMessageResult> getPostMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "PostMessage",
      requestType = sd2526.trab.api.grpc.Messages.PostMessageArgs.class,
      responseType = sd2526.trab.api.grpc.Messages.PostMessageResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.PostMessageArgs,
      sd2526.trab.api.grpc.Messages.PostMessageResult> getPostMessageMethod() {
    io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.PostMessageArgs, sd2526.trab.api.grpc.Messages.PostMessageResult> getPostMessageMethod;
    if ((getPostMessageMethod = GrpcMessagesGrpc.getPostMessageMethod) == null) {
      synchronized (GrpcMessagesGrpc.class) {
        if ((getPostMessageMethod = GrpcMessagesGrpc.getPostMessageMethod) == null) {
          GrpcMessagesGrpc.getPostMessageMethod = getPostMessageMethod =
              io.grpc.MethodDescriptor.<sd2526.trab.api.grpc.Messages.PostMessageArgs, sd2526.trab.api.grpc.Messages.PostMessageResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "PostMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Messages.PostMessageArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Messages.PostMessageResult.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcMessagesMethodDescriptorSupplier("PostMessage"))
              .build();
        }
      }
    }
    return getPostMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.GetInboxMessageArgs,
      sd2526.trab.api.grpc.Messages.GrpcMessage> getGetInboxMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetInboxMessage",
      requestType = sd2526.trab.api.grpc.Messages.GetInboxMessageArgs.class,
      responseType = sd2526.trab.api.grpc.Messages.GrpcMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.GetInboxMessageArgs,
      sd2526.trab.api.grpc.Messages.GrpcMessage> getGetInboxMessageMethod() {
    io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.GetInboxMessageArgs, sd2526.trab.api.grpc.Messages.GrpcMessage> getGetInboxMessageMethod;
    if ((getGetInboxMessageMethod = GrpcMessagesGrpc.getGetInboxMessageMethod) == null) {
      synchronized (GrpcMessagesGrpc.class) {
        if ((getGetInboxMessageMethod = GrpcMessagesGrpc.getGetInboxMessageMethod) == null) {
          GrpcMessagesGrpc.getGetInboxMessageMethod = getGetInboxMessageMethod =
              io.grpc.MethodDescriptor.<sd2526.trab.api.grpc.Messages.GetInboxMessageArgs, sd2526.trab.api.grpc.Messages.GrpcMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetInboxMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Messages.GetInboxMessageArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Messages.GrpcMessage.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcMessagesMethodDescriptorSupplier("GetInboxMessage"))
              .build();
        }
      }
    }
    return getGetInboxMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.GetAllInboxMessagesArgs,
      sd2526.trab.api.grpc.Messages.GetAllInboxMessagesResult> getGetAllInboxMessagesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAllInboxMessages",
      requestType = sd2526.trab.api.grpc.Messages.GetAllInboxMessagesArgs.class,
      responseType = sd2526.trab.api.grpc.Messages.GetAllInboxMessagesResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.GetAllInboxMessagesArgs,
      sd2526.trab.api.grpc.Messages.GetAllInboxMessagesResult> getGetAllInboxMessagesMethod() {
    io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.GetAllInboxMessagesArgs, sd2526.trab.api.grpc.Messages.GetAllInboxMessagesResult> getGetAllInboxMessagesMethod;
    if ((getGetAllInboxMessagesMethod = GrpcMessagesGrpc.getGetAllInboxMessagesMethod) == null) {
      synchronized (GrpcMessagesGrpc.class) {
        if ((getGetAllInboxMessagesMethod = GrpcMessagesGrpc.getGetAllInboxMessagesMethod) == null) {
          GrpcMessagesGrpc.getGetAllInboxMessagesMethod = getGetAllInboxMessagesMethod =
              io.grpc.MethodDescriptor.<sd2526.trab.api.grpc.Messages.GetAllInboxMessagesArgs, sd2526.trab.api.grpc.Messages.GetAllInboxMessagesResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAllInboxMessages"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Messages.GetAllInboxMessagesArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Messages.GetAllInboxMessagesResult.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcMessagesMethodDescriptorSupplier("GetAllInboxMessages"))
              .build();
        }
      }
    }
    return getGetAllInboxMessagesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.RemoveInboxMessageArgs,
      com.google.protobuf.Empty> getRemoveInboxMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RemoveInboxMessage",
      requestType = sd2526.trab.api.grpc.Messages.RemoveInboxMessageArgs.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.RemoveInboxMessageArgs,
      com.google.protobuf.Empty> getRemoveInboxMessageMethod() {
    io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.RemoveInboxMessageArgs, com.google.protobuf.Empty> getRemoveInboxMessageMethod;
    if ((getRemoveInboxMessageMethod = GrpcMessagesGrpc.getRemoveInboxMessageMethod) == null) {
      synchronized (GrpcMessagesGrpc.class) {
        if ((getRemoveInboxMessageMethod = GrpcMessagesGrpc.getRemoveInboxMessageMethod) == null) {
          GrpcMessagesGrpc.getRemoveInboxMessageMethod = getRemoveInboxMessageMethod =
              io.grpc.MethodDescriptor.<sd2526.trab.api.grpc.Messages.RemoveInboxMessageArgs, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RemoveInboxMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Messages.RemoveInboxMessageArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcMessagesMethodDescriptorSupplier("RemoveInboxMessage"))
              .build();
        }
      }
    }
    return getRemoveInboxMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.DeleteMessageArgs,
      com.google.protobuf.Empty> getDeleteMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteMessage",
      requestType = sd2526.trab.api.grpc.Messages.DeleteMessageArgs.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.DeleteMessageArgs,
      com.google.protobuf.Empty> getDeleteMessageMethod() {
    io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.DeleteMessageArgs, com.google.protobuf.Empty> getDeleteMessageMethod;
    if ((getDeleteMessageMethod = GrpcMessagesGrpc.getDeleteMessageMethod) == null) {
      synchronized (GrpcMessagesGrpc.class) {
        if ((getDeleteMessageMethod = GrpcMessagesGrpc.getDeleteMessageMethod) == null) {
          GrpcMessagesGrpc.getDeleteMessageMethod = getDeleteMessageMethod =
              io.grpc.MethodDescriptor.<sd2526.trab.api.grpc.Messages.DeleteMessageArgs, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Messages.DeleteMessageArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcMessagesMethodDescriptorSupplier("DeleteMessage"))
              .build();
        }
      }
    }
    return getDeleteMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.SearchInboxArgs,
      sd2526.trab.api.grpc.Messages.SearchInboxResult> getSearchInboxMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SearchInbox",
      requestType = sd2526.trab.api.grpc.Messages.SearchInboxArgs.class,
      responseType = sd2526.trab.api.grpc.Messages.SearchInboxResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.SearchInboxArgs,
      sd2526.trab.api.grpc.Messages.SearchInboxResult> getSearchInboxMethod() {
    io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.SearchInboxArgs, sd2526.trab.api.grpc.Messages.SearchInboxResult> getSearchInboxMethod;
    if ((getSearchInboxMethod = GrpcMessagesGrpc.getSearchInboxMethod) == null) {
      synchronized (GrpcMessagesGrpc.class) {
        if ((getSearchInboxMethod = GrpcMessagesGrpc.getSearchInboxMethod) == null) {
          GrpcMessagesGrpc.getSearchInboxMethod = getSearchInboxMethod =
              io.grpc.MethodDescriptor.<sd2526.trab.api.grpc.Messages.SearchInboxArgs, sd2526.trab.api.grpc.Messages.SearchInboxResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SearchInbox"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Messages.SearchInboxArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Messages.SearchInboxResult.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcMessagesMethodDescriptorSupplier("SearchInbox"))
              .build();
        }
      }
    }
    return getSearchInboxMethod;
  }

  private static volatile io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.InternalDeliverMessageArgs,
      com.google.protobuf.Empty> getInternalDeliverMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "InternalDeliverMessage",
      requestType = sd2526.trab.api.grpc.Messages.InternalDeliverMessageArgs.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.InternalDeliverMessageArgs,
      com.google.protobuf.Empty> getInternalDeliverMessageMethod() {
    io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.InternalDeliverMessageArgs, com.google.protobuf.Empty> getInternalDeliverMessageMethod;
    if ((getInternalDeliverMessageMethod = GrpcMessagesGrpc.getInternalDeliverMessageMethod) == null) {
      synchronized (GrpcMessagesGrpc.class) {
        if ((getInternalDeliverMessageMethod = GrpcMessagesGrpc.getInternalDeliverMessageMethod) == null) {
          GrpcMessagesGrpc.getInternalDeliverMessageMethod = getInternalDeliverMessageMethod =
              io.grpc.MethodDescriptor.<sd2526.trab.api.grpc.Messages.InternalDeliverMessageArgs, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "InternalDeliverMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Messages.InternalDeliverMessageArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcMessagesMethodDescriptorSupplier("InternalDeliverMessage"))
              .build();
        }
      }
    }
    return getInternalDeliverMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.InternalRemoveDeliveredMessageArgs,
      com.google.protobuf.Empty> getInternalRemoveDeliveredMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "InternalRemoveDeliveredMessage",
      requestType = sd2526.trab.api.grpc.Messages.InternalRemoveDeliveredMessageArgs.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.InternalRemoveDeliveredMessageArgs,
      com.google.protobuf.Empty> getInternalRemoveDeliveredMessageMethod() {
    io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Messages.InternalRemoveDeliveredMessageArgs, com.google.protobuf.Empty> getInternalRemoveDeliveredMessageMethod;
    if ((getInternalRemoveDeliveredMessageMethod = GrpcMessagesGrpc.getInternalRemoveDeliveredMessageMethod) == null) {
      synchronized (GrpcMessagesGrpc.class) {
        if ((getInternalRemoveDeliveredMessageMethod = GrpcMessagesGrpc.getInternalRemoveDeliveredMessageMethod) == null) {
          GrpcMessagesGrpc.getInternalRemoveDeliveredMessageMethod = getInternalRemoveDeliveredMessageMethod =
              io.grpc.MethodDescriptor.<sd2526.trab.api.grpc.Messages.InternalRemoveDeliveredMessageArgs, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "InternalRemoveDeliveredMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Messages.InternalRemoveDeliveredMessageArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcMessagesMethodDescriptorSupplier("InternalRemoveDeliveredMessage"))
              .build();
        }
      }
    }
    return getInternalRemoveDeliveredMessageMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GrpcMessagesStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcMessagesStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcMessagesStub>() {
        @java.lang.Override
        public GrpcMessagesStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcMessagesStub(channel, callOptions);
        }
      };
    return GrpcMessagesStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static GrpcMessagesBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcMessagesBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcMessagesBlockingV2Stub>() {
        @java.lang.Override
        public GrpcMessagesBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcMessagesBlockingV2Stub(channel, callOptions);
        }
      };
    return GrpcMessagesBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GrpcMessagesBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcMessagesBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcMessagesBlockingStub>() {
        @java.lang.Override
        public GrpcMessagesBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcMessagesBlockingStub(channel, callOptions);
        }
      };
    return GrpcMessagesBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GrpcMessagesFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcMessagesFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcMessagesFutureStub>() {
        @java.lang.Override
        public GrpcMessagesFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcMessagesFutureStub(channel, callOptions);
        }
      };
    return GrpcMessagesFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void postMessage(sd2526.trab.api.grpc.Messages.PostMessageArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Messages.PostMessageResult> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPostMessageMethod(), responseObserver);
    }

    /**
     */
    default void getInboxMessage(sd2526.trab.api.grpc.Messages.GetInboxMessageArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Messages.GrpcMessage> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetInboxMessageMethod(), responseObserver);
    }

    /**
     */
    default void getAllInboxMessages(sd2526.trab.api.grpc.Messages.GetAllInboxMessagesArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Messages.GetAllInboxMessagesResult> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAllInboxMessagesMethod(), responseObserver);
    }

    /**
     */
    default void removeInboxMessage(sd2526.trab.api.grpc.Messages.RemoveInboxMessageArgs request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRemoveInboxMessageMethod(), responseObserver);
    }

    /**
     */
    default void deleteMessage(sd2526.trab.api.grpc.Messages.DeleteMessageArgs request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteMessageMethod(), responseObserver);
    }

    /**
     */
    default void searchInbox(sd2526.trab.api.grpc.Messages.SearchInboxArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Messages.SearchInboxResult> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchInboxMethod(), responseObserver);
    }

    /**
     */
    default void internalDeliverMessage(sd2526.trab.api.grpc.Messages.InternalDeliverMessageArgs request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getInternalDeliverMessageMethod(), responseObserver);
    }

    /**
     */
    default void internalRemoveDeliveredMessage(sd2526.trab.api.grpc.Messages.InternalRemoveDeliveredMessageArgs request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getInternalRemoveDeliveredMessageMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service GrpcMessages.
   */
  public static abstract class GrpcMessagesImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return GrpcMessagesGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service GrpcMessages.
   */
  public static final class GrpcMessagesStub
      extends io.grpc.stub.AbstractAsyncStub<GrpcMessagesStub> {
    private GrpcMessagesStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcMessagesStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcMessagesStub(channel, callOptions);
    }

    /**
     */
    public void postMessage(sd2526.trab.api.grpc.Messages.PostMessageArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Messages.PostMessageResult> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPostMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getInboxMessage(sd2526.trab.api.grpc.Messages.GetInboxMessageArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Messages.GrpcMessage> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetInboxMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAllInboxMessages(sd2526.trab.api.grpc.Messages.GetAllInboxMessagesArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Messages.GetAllInboxMessagesResult> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAllInboxMessagesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeInboxMessage(sd2526.trab.api.grpc.Messages.RemoveInboxMessageArgs request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRemoveInboxMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteMessage(sd2526.trab.api.grpc.Messages.DeleteMessageArgs request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void searchInbox(sd2526.trab.api.grpc.Messages.SearchInboxArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Messages.SearchInboxResult> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSearchInboxMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void internalDeliverMessage(sd2526.trab.api.grpc.Messages.InternalDeliverMessageArgs request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getInternalDeliverMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void internalRemoveDeliveredMessage(sd2526.trab.api.grpc.Messages.InternalRemoveDeliveredMessageArgs request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getInternalRemoveDeliveredMessageMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service GrpcMessages.
   */
  public static final class GrpcMessagesBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<GrpcMessagesBlockingV2Stub> {
    private GrpcMessagesBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcMessagesBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcMessagesBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public sd2526.trab.api.grpc.Messages.PostMessageResult postMessage(sd2526.trab.api.grpc.Messages.PostMessageArgs request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getPostMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public sd2526.trab.api.grpc.Messages.GrpcMessage getInboxMessage(sd2526.trab.api.grpc.Messages.GetInboxMessageArgs request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetInboxMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public sd2526.trab.api.grpc.Messages.GetAllInboxMessagesResult getAllInboxMessages(sd2526.trab.api.grpc.Messages.GetAllInboxMessagesArgs request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetAllInboxMessagesMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty removeInboxMessage(sd2526.trab.api.grpc.Messages.RemoveInboxMessageArgs request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getRemoveInboxMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty deleteMessage(sd2526.trab.api.grpc.Messages.DeleteMessageArgs request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getDeleteMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public sd2526.trab.api.grpc.Messages.SearchInboxResult searchInbox(sd2526.trab.api.grpc.Messages.SearchInboxArgs request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getSearchInboxMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty internalDeliverMessage(sd2526.trab.api.grpc.Messages.InternalDeliverMessageArgs request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getInternalDeliverMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty internalRemoveDeliveredMessage(sd2526.trab.api.grpc.Messages.InternalRemoveDeliveredMessageArgs request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getInternalRemoveDeliveredMessageMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service GrpcMessages.
   */
  public static final class GrpcMessagesBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<GrpcMessagesBlockingStub> {
    private GrpcMessagesBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcMessagesBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcMessagesBlockingStub(channel, callOptions);
    }

    /**
     */
    public sd2526.trab.api.grpc.Messages.PostMessageResult postMessage(sd2526.trab.api.grpc.Messages.PostMessageArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPostMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public sd2526.trab.api.grpc.Messages.GrpcMessage getInboxMessage(sd2526.trab.api.grpc.Messages.GetInboxMessageArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetInboxMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public sd2526.trab.api.grpc.Messages.GetAllInboxMessagesResult getAllInboxMessages(sd2526.trab.api.grpc.Messages.GetAllInboxMessagesArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAllInboxMessagesMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty removeInboxMessage(sd2526.trab.api.grpc.Messages.RemoveInboxMessageArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRemoveInboxMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty deleteMessage(sd2526.trab.api.grpc.Messages.DeleteMessageArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public sd2526.trab.api.grpc.Messages.SearchInboxResult searchInbox(sd2526.trab.api.grpc.Messages.SearchInboxArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSearchInboxMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty internalDeliverMessage(sd2526.trab.api.grpc.Messages.InternalDeliverMessageArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getInternalDeliverMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty internalRemoveDeliveredMessage(sd2526.trab.api.grpc.Messages.InternalRemoveDeliveredMessageArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getInternalRemoveDeliveredMessageMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service GrpcMessages.
   */
  public static final class GrpcMessagesFutureStub
      extends io.grpc.stub.AbstractFutureStub<GrpcMessagesFutureStub> {
    private GrpcMessagesFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcMessagesFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcMessagesFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<sd2526.trab.api.grpc.Messages.PostMessageResult> postMessage(
        sd2526.trab.api.grpc.Messages.PostMessageArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPostMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<sd2526.trab.api.grpc.Messages.GrpcMessage> getInboxMessage(
        sd2526.trab.api.grpc.Messages.GetInboxMessageArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetInboxMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<sd2526.trab.api.grpc.Messages.GetAllInboxMessagesResult> getAllInboxMessages(
        sd2526.trab.api.grpc.Messages.GetAllInboxMessagesArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAllInboxMessagesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> removeInboxMessage(
        sd2526.trab.api.grpc.Messages.RemoveInboxMessageArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRemoveInboxMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> deleteMessage(
        sd2526.trab.api.grpc.Messages.DeleteMessageArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<sd2526.trab.api.grpc.Messages.SearchInboxResult> searchInbox(
        sd2526.trab.api.grpc.Messages.SearchInboxArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSearchInboxMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> internalDeliverMessage(
        sd2526.trab.api.grpc.Messages.InternalDeliverMessageArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getInternalDeliverMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> internalRemoveDeliveredMessage(
        sd2526.trab.api.grpc.Messages.InternalRemoveDeliveredMessageArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getInternalRemoveDeliveredMessageMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_POST_MESSAGE = 0;
  private static final int METHODID_GET_INBOX_MESSAGE = 1;
  private static final int METHODID_GET_ALL_INBOX_MESSAGES = 2;
  private static final int METHODID_REMOVE_INBOX_MESSAGE = 3;
  private static final int METHODID_DELETE_MESSAGE = 4;
  private static final int METHODID_SEARCH_INBOX = 5;
  private static final int METHODID_INTERNAL_DELIVER_MESSAGE = 6;
  private static final int METHODID_INTERNAL_REMOVE_DELIVERED_MESSAGE = 7;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_POST_MESSAGE:
          serviceImpl.postMessage((sd2526.trab.api.grpc.Messages.PostMessageArgs) request,
              (io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Messages.PostMessageResult>) responseObserver);
          break;
        case METHODID_GET_INBOX_MESSAGE:
          serviceImpl.getInboxMessage((sd2526.trab.api.grpc.Messages.GetInboxMessageArgs) request,
              (io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Messages.GrpcMessage>) responseObserver);
          break;
        case METHODID_GET_ALL_INBOX_MESSAGES:
          serviceImpl.getAllInboxMessages((sd2526.trab.api.grpc.Messages.GetAllInboxMessagesArgs) request,
              (io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Messages.GetAllInboxMessagesResult>) responseObserver);
          break;
        case METHODID_REMOVE_INBOX_MESSAGE:
          serviceImpl.removeInboxMessage((sd2526.trab.api.grpc.Messages.RemoveInboxMessageArgs) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_DELETE_MESSAGE:
          serviceImpl.deleteMessage((sd2526.trab.api.grpc.Messages.DeleteMessageArgs) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_SEARCH_INBOX:
          serviceImpl.searchInbox((sd2526.trab.api.grpc.Messages.SearchInboxArgs) request,
              (io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Messages.SearchInboxResult>) responseObserver);
          break;
        case METHODID_INTERNAL_DELIVER_MESSAGE:
          serviceImpl.internalDeliverMessage((sd2526.trab.api.grpc.Messages.InternalDeliverMessageArgs) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_INTERNAL_REMOVE_DELIVERED_MESSAGE:
          serviceImpl.internalRemoveDeliveredMessage((sd2526.trab.api.grpc.Messages.InternalRemoveDeliveredMessageArgs) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getPostMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              sd2526.trab.api.grpc.Messages.PostMessageArgs,
              sd2526.trab.api.grpc.Messages.PostMessageResult>(
                service, METHODID_POST_MESSAGE)))
        .addMethod(
          getGetInboxMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              sd2526.trab.api.grpc.Messages.GetInboxMessageArgs,
              sd2526.trab.api.grpc.Messages.GrpcMessage>(
                service, METHODID_GET_INBOX_MESSAGE)))
        .addMethod(
          getGetAllInboxMessagesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              sd2526.trab.api.grpc.Messages.GetAllInboxMessagesArgs,
              sd2526.trab.api.grpc.Messages.GetAllInboxMessagesResult>(
                service, METHODID_GET_ALL_INBOX_MESSAGES)))
        .addMethod(
          getRemoveInboxMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              sd2526.trab.api.grpc.Messages.RemoveInboxMessageArgs,
              com.google.protobuf.Empty>(
                service, METHODID_REMOVE_INBOX_MESSAGE)))
        .addMethod(
          getDeleteMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              sd2526.trab.api.grpc.Messages.DeleteMessageArgs,
              com.google.protobuf.Empty>(
                service, METHODID_DELETE_MESSAGE)))
        .addMethod(
          getSearchInboxMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              sd2526.trab.api.grpc.Messages.SearchInboxArgs,
              sd2526.trab.api.grpc.Messages.SearchInboxResult>(
                service, METHODID_SEARCH_INBOX)))
        .addMethod(
          getInternalDeliverMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              sd2526.trab.api.grpc.Messages.InternalDeliverMessageArgs,
              com.google.protobuf.Empty>(
                service, METHODID_INTERNAL_DELIVER_MESSAGE)))
        .addMethod(
          getInternalRemoveDeliveredMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              sd2526.trab.api.grpc.Messages.InternalRemoveDeliveredMessageArgs,
              com.google.protobuf.Empty>(
                service, METHODID_INTERNAL_REMOVE_DELIVERED_MESSAGE)))
        .build();
  }

  private static abstract class GrpcMessagesBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GrpcMessagesBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return sd2526.trab.api.grpc.Messages.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("GrpcMessages");
    }
  }

  private static final class GrpcMessagesFileDescriptorSupplier
      extends GrpcMessagesBaseDescriptorSupplier {
    GrpcMessagesFileDescriptorSupplier() {}
  }

  private static final class GrpcMessagesMethodDescriptorSupplier
      extends GrpcMessagesBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    GrpcMessagesMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (GrpcMessagesGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GrpcMessagesFileDescriptorSupplier())
              .addMethod(getPostMessageMethod())
              .addMethod(getGetInboxMessageMethod())
              .addMethod(getGetAllInboxMessagesMethod())
              .addMethod(getRemoveInboxMessageMethod())
              .addMethod(getDeleteMessageMethod())
              .addMethod(getSearchInboxMethod())
              .addMethod(getInternalDeliverMessageMethod())
              .addMethod(getInternalRemoveDeliveredMessageMethod())
              .build();
        }
      }
    }
    return result;
  }
}
