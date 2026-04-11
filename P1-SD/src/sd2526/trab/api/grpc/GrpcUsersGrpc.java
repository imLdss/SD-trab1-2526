package sd2526.trab.api.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class GrpcUsersGrpc {

  private GrpcUsersGrpc() {}

  public static final java.lang.String SERVICE_NAME = "sd2526.trab.api.grpc.GrpcUsers";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.GrpcUser,
      sd2526.trab.api.grpc.Users.PostUserResult> getPostUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "PostUser",
      requestType = sd2526.trab.api.grpc.Users.GrpcUser.class,
      responseType = sd2526.trab.api.grpc.Users.PostUserResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.GrpcUser,
      sd2526.trab.api.grpc.Users.PostUserResult> getPostUserMethod() {
    io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.GrpcUser, sd2526.trab.api.grpc.Users.PostUserResult> getPostUserMethod;
    if ((getPostUserMethod = GrpcUsersGrpc.getPostUserMethod) == null) {
      synchronized (GrpcUsersGrpc.class) {
        if ((getPostUserMethod = GrpcUsersGrpc.getPostUserMethod) == null) {
          GrpcUsersGrpc.getPostUserMethod = getPostUserMethod =
              io.grpc.MethodDescriptor.<sd2526.trab.api.grpc.Users.GrpcUser, sd2526.trab.api.grpc.Users.PostUserResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "PostUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Users.GrpcUser.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Users.PostUserResult.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcUsersMethodDescriptorSupplier("PostUser"))
              .build();
        }
      }
    }
    return getPostUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.GetUserArgs,
      sd2526.trab.api.grpc.Users.GetUserResult> getGetUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetUser",
      requestType = sd2526.trab.api.grpc.Users.GetUserArgs.class,
      responseType = sd2526.trab.api.grpc.Users.GetUserResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.GetUserArgs,
      sd2526.trab.api.grpc.Users.GetUserResult> getGetUserMethod() {
    io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.GetUserArgs, sd2526.trab.api.grpc.Users.GetUserResult> getGetUserMethod;
    if ((getGetUserMethod = GrpcUsersGrpc.getGetUserMethod) == null) {
      synchronized (GrpcUsersGrpc.class) {
        if ((getGetUserMethod = GrpcUsersGrpc.getGetUserMethod) == null) {
          GrpcUsersGrpc.getGetUserMethod = getGetUserMethod =
              io.grpc.MethodDescriptor.<sd2526.trab.api.grpc.Users.GetUserArgs, sd2526.trab.api.grpc.Users.GetUserResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Users.GetUserArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Users.GetUserResult.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcUsersMethodDescriptorSupplier("GetUser"))
              .build();
        }
      }
    }
    return getGetUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.GetUserInternalArgs,
      sd2526.trab.api.grpc.Users.GetUserResult> getGetUserInternalMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetUserInternal",
      requestType = sd2526.trab.api.grpc.Users.GetUserInternalArgs.class,
      responseType = sd2526.trab.api.grpc.Users.GetUserResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.GetUserInternalArgs,
      sd2526.trab.api.grpc.Users.GetUserResult> getGetUserInternalMethod() {
    io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.GetUserInternalArgs, sd2526.trab.api.grpc.Users.GetUserResult> getGetUserInternalMethod;
    if ((getGetUserInternalMethod = GrpcUsersGrpc.getGetUserInternalMethod) == null) {
      synchronized (GrpcUsersGrpc.class) {
        if ((getGetUserInternalMethod = GrpcUsersGrpc.getGetUserInternalMethod) == null) {
          GrpcUsersGrpc.getGetUserInternalMethod = getGetUserInternalMethod =
              io.grpc.MethodDescriptor.<sd2526.trab.api.grpc.Users.GetUserInternalArgs, sd2526.trab.api.grpc.Users.GetUserResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetUserInternal"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Users.GetUserInternalArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Users.GetUserResult.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcUsersMethodDescriptorSupplier("GetUserInternal"))
              .build();
        }
      }
    }
    return getGetUserInternalMethod;
  }

  private static volatile io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.UpdateUserArgs,
      sd2526.trab.api.grpc.Users.UpdateUserResult> getUpdateUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateUser",
      requestType = sd2526.trab.api.grpc.Users.UpdateUserArgs.class,
      responseType = sd2526.trab.api.grpc.Users.UpdateUserResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.UpdateUserArgs,
      sd2526.trab.api.grpc.Users.UpdateUserResult> getUpdateUserMethod() {
    io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.UpdateUserArgs, sd2526.trab.api.grpc.Users.UpdateUserResult> getUpdateUserMethod;
    if ((getUpdateUserMethod = GrpcUsersGrpc.getUpdateUserMethod) == null) {
      synchronized (GrpcUsersGrpc.class) {
        if ((getUpdateUserMethod = GrpcUsersGrpc.getUpdateUserMethod) == null) {
          GrpcUsersGrpc.getUpdateUserMethod = getUpdateUserMethod =
              io.grpc.MethodDescriptor.<sd2526.trab.api.grpc.Users.UpdateUserArgs, sd2526.trab.api.grpc.Users.UpdateUserResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Users.UpdateUserArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Users.UpdateUserResult.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcUsersMethodDescriptorSupplier("UpdateUser"))
              .build();
        }
      }
    }
    return getUpdateUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.DeleteUserArgs,
      sd2526.trab.api.grpc.Users.DeleteUserResult> getDeleteUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteUser",
      requestType = sd2526.trab.api.grpc.Users.DeleteUserArgs.class,
      responseType = sd2526.trab.api.grpc.Users.DeleteUserResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.DeleteUserArgs,
      sd2526.trab.api.grpc.Users.DeleteUserResult> getDeleteUserMethod() {
    io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.DeleteUserArgs, sd2526.trab.api.grpc.Users.DeleteUserResult> getDeleteUserMethod;
    if ((getDeleteUserMethod = GrpcUsersGrpc.getDeleteUserMethod) == null) {
      synchronized (GrpcUsersGrpc.class) {
        if ((getDeleteUserMethod = GrpcUsersGrpc.getDeleteUserMethod) == null) {
          GrpcUsersGrpc.getDeleteUserMethod = getDeleteUserMethod =
              io.grpc.MethodDescriptor.<sd2526.trab.api.grpc.Users.DeleteUserArgs, sd2526.trab.api.grpc.Users.DeleteUserResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Users.DeleteUserArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Users.DeleteUserResult.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcUsersMethodDescriptorSupplier("DeleteUser"))
              .build();
        }
      }
    }
    return getDeleteUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.SearchUsersArgs,
      sd2526.trab.api.grpc.Users.GrpcUser> getSearchUsersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SearchUsers",
      requestType = sd2526.trab.api.grpc.Users.SearchUsersArgs.class,
      responseType = sd2526.trab.api.grpc.Users.GrpcUser.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.SearchUsersArgs,
      sd2526.trab.api.grpc.Users.GrpcUser> getSearchUsersMethod() {
    io.grpc.MethodDescriptor<sd2526.trab.api.grpc.Users.SearchUsersArgs, sd2526.trab.api.grpc.Users.GrpcUser> getSearchUsersMethod;
    if ((getSearchUsersMethod = GrpcUsersGrpc.getSearchUsersMethod) == null) {
      synchronized (GrpcUsersGrpc.class) {
        if ((getSearchUsersMethod = GrpcUsersGrpc.getSearchUsersMethod) == null) {
          GrpcUsersGrpc.getSearchUsersMethod = getSearchUsersMethod =
              io.grpc.MethodDescriptor.<sd2526.trab.api.grpc.Users.SearchUsersArgs, sd2526.trab.api.grpc.Users.GrpcUser>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SearchUsers"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Users.SearchUsersArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sd2526.trab.api.grpc.Users.GrpcUser.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcUsersMethodDescriptorSupplier("SearchUsers"))
              .build();
        }
      }
    }
    return getSearchUsersMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GrpcUsersStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcUsersStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcUsersStub>() {
        @java.lang.Override
        public GrpcUsersStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcUsersStub(channel, callOptions);
        }
      };
    return GrpcUsersStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static GrpcUsersBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcUsersBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcUsersBlockingV2Stub>() {
        @java.lang.Override
        public GrpcUsersBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcUsersBlockingV2Stub(channel, callOptions);
        }
      };
    return GrpcUsersBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GrpcUsersBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcUsersBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcUsersBlockingStub>() {
        @java.lang.Override
        public GrpcUsersBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcUsersBlockingStub(channel, callOptions);
        }
      };
    return GrpcUsersBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GrpcUsersFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcUsersFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcUsersFutureStub>() {
        @java.lang.Override
        public GrpcUsersFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcUsersFutureStub(channel, callOptions);
        }
      };
    return GrpcUsersFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void postUser(sd2526.trab.api.grpc.Users.GrpcUser request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.PostUserResult> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPostUserMethod(), responseObserver);
    }

    /**
     */
    default void getUser(sd2526.trab.api.grpc.Users.GetUserArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.GetUserResult> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUserMethod(), responseObserver);
    }

    /**
     */
    default void getUserInternal(sd2526.trab.api.grpc.Users.GetUserInternalArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.GetUserResult> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUserInternalMethod(), responseObserver);
    }

    /**
     */
    default void updateUser(sd2526.trab.api.grpc.Users.UpdateUserArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.UpdateUserResult> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateUserMethod(), responseObserver);
    }

    /**
     */
    default void deleteUser(sd2526.trab.api.grpc.Users.DeleteUserArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.DeleteUserResult> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteUserMethod(), responseObserver);
    }

    /**
     */
    default void searchUsers(sd2526.trab.api.grpc.Users.SearchUsersArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.GrpcUser> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchUsersMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service GrpcUsers.
   */
  public static abstract class GrpcUsersImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return GrpcUsersGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service GrpcUsers.
   */
  public static final class GrpcUsersStub
      extends io.grpc.stub.AbstractAsyncStub<GrpcUsersStub> {
    private GrpcUsersStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcUsersStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcUsersStub(channel, callOptions);
    }

    /**
     */
    public void postUser(sd2526.trab.api.grpc.Users.GrpcUser request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.PostUserResult> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPostUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getUser(sd2526.trab.api.grpc.Users.GetUserArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.GetUserResult> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getUserInternal(sd2526.trab.api.grpc.Users.GetUserInternalArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.GetUserResult> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUserInternalMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateUser(sd2526.trab.api.grpc.Users.UpdateUserArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.UpdateUserResult> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteUser(sd2526.trab.api.grpc.Users.DeleteUserArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.DeleteUserResult> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void searchUsers(sd2526.trab.api.grpc.Users.SearchUsersArgs request,
        io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.GrpcUser> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getSearchUsersMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service GrpcUsers.
   */
  public static final class GrpcUsersBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<GrpcUsersBlockingV2Stub> {
    private GrpcUsersBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcUsersBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcUsersBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public sd2526.trab.api.grpc.Users.PostUserResult postUser(sd2526.trab.api.grpc.Users.GrpcUser request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getPostUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public sd2526.trab.api.grpc.Users.GetUserResult getUser(sd2526.trab.api.grpc.Users.GetUserArgs request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public sd2526.trab.api.grpc.Users.GetUserResult getUserInternal(sd2526.trab.api.grpc.Users.GetUserInternalArgs request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetUserInternalMethod(), getCallOptions(), request);
    }

    /**
     */
    public sd2526.trab.api.grpc.Users.UpdateUserResult updateUser(sd2526.trab.api.grpc.Users.UpdateUserArgs request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getUpdateUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public sd2526.trab.api.grpc.Users.DeleteUserResult deleteUser(sd2526.trab.api.grpc.Users.DeleteUserArgs request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getDeleteUserMethod(), getCallOptions(), request);
    }

    /**
     */
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/10918")
    public io.grpc.stub.BlockingClientCall<?, sd2526.trab.api.grpc.Users.GrpcUser>
        searchUsers(sd2526.trab.api.grpc.Users.SearchUsersArgs request) {
      return io.grpc.stub.ClientCalls.blockingV2ServerStreamingCall(
          getChannel(), getSearchUsersMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service GrpcUsers.
   */
  public static final class GrpcUsersBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<GrpcUsersBlockingStub> {
    private GrpcUsersBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcUsersBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcUsersBlockingStub(channel, callOptions);
    }

    /**
     */
    public sd2526.trab.api.grpc.Users.PostUserResult postUser(sd2526.trab.api.grpc.Users.GrpcUser request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPostUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public sd2526.trab.api.grpc.Users.GetUserResult getUser(sd2526.trab.api.grpc.Users.GetUserArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public sd2526.trab.api.grpc.Users.GetUserResult getUserInternal(sd2526.trab.api.grpc.Users.GetUserInternalArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUserInternalMethod(), getCallOptions(), request);
    }

    /**
     */
    public sd2526.trab.api.grpc.Users.UpdateUserResult updateUser(sd2526.trab.api.grpc.Users.UpdateUserArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public sd2526.trab.api.grpc.Users.DeleteUserResult deleteUser(sd2526.trab.api.grpc.Users.DeleteUserArgs request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<sd2526.trab.api.grpc.Users.GrpcUser> searchUsers(
        sd2526.trab.api.grpc.Users.SearchUsersArgs request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getSearchUsersMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service GrpcUsers.
   */
  public static final class GrpcUsersFutureStub
      extends io.grpc.stub.AbstractFutureStub<GrpcUsersFutureStub> {
    private GrpcUsersFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcUsersFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcUsersFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<sd2526.trab.api.grpc.Users.PostUserResult> postUser(
        sd2526.trab.api.grpc.Users.GrpcUser request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPostUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<sd2526.trab.api.grpc.Users.GetUserResult> getUser(
        sd2526.trab.api.grpc.Users.GetUserArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<sd2526.trab.api.grpc.Users.GetUserResult> getUserInternal(
        sd2526.trab.api.grpc.Users.GetUserInternalArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUserInternalMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<sd2526.trab.api.grpc.Users.UpdateUserResult> updateUser(
        sd2526.trab.api.grpc.Users.UpdateUserArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<sd2526.trab.api.grpc.Users.DeleteUserResult> deleteUser(
        sd2526.trab.api.grpc.Users.DeleteUserArgs request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteUserMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_POST_USER = 0;
  private static final int METHODID_GET_USER = 1;
  private static final int METHODID_GET_USER_INTERNAL = 2;
  private static final int METHODID_UPDATE_USER = 3;
  private static final int METHODID_DELETE_USER = 4;
  private static final int METHODID_SEARCH_USERS = 5;

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
        case METHODID_POST_USER:
          serviceImpl.postUser((sd2526.trab.api.grpc.Users.GrpcUser) request,
              (io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.PostUserResult>) responseObserver);
          break;
        case METHODID_GET_USER:
          serviceImpl.getUser((sd2526.trab.api.grpc.Users.GetUserArgs) request,
              (io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.GetUserResult>) responseObserver);
          break;
        case METHODID_GET_USER_INTERNAL:
          serviceImpl.getUserInternal((sd2526.trab.api.grpc.Users.GetUserInternalArgs) request,
              (io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.GetUserResult>) responseObserver);
          break;
        case METHODID_UPDATE_USER:
          serviceImpl.updateUser((sd2526.trab.api.grpc.Users.UpdateUserArgs) request,
              (io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.UpdateUserResult>) responseObserver);
          break;
        case METHODID_DELETE_USER:
          serviceImpl.deleteUser((sd2526.trab.api.grpc.Users.DeleteUserArgs) request,
              (io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.DeleteUserResult>) responseObserver);
          break;
        case METHODID_SEARCH_USERS:
          serviceImpl.searchUsers((sd2526.trab.api.grpc.Users.SearchUsersArgs) request,
              (io.grpc.stub.StreamObserver<sd2526.trab.api.grpc.Users.GrpcUser>) responseObserver);
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
          getPostUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              sd2526.trab.api.grpc.Users.GrpcUser,
              sd2526.trab.api.grpc.Users.PostUserResult>(
                service, METHODID_POST_USER)))
        .addMethod(
          getGetUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              sd2526.trab.api.grpc.Users.GetUserArgs,
              sd2526.trab.api.grpc.Users.GetUserResult>(
                service, METHODID_GET_USER)))
        .addMethod(
          getGetUserInternalMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              sd2526.trab.api.grpc.Users.GetUserInternalArgs,
              sd2526.trab.api.grpc.Users.GetUserResult>(
                service, METHODID_GET_USER_INTERNAL)))
        .addMethod(
          getUpdateUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              sd2526.trab.api.grpc.Users.UpdateUserArgs,
              sd2526.trab.api.grpc.Users.UpdateUserResult>(
                service, METHODID_UPDATE_USER)))
        .addMethod(
          getDeleteUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              sd2526.trab.api.grpc.Users.DeleteUserArgs,
              sd2526.trab.api.grpc.Users.DeleteUserResult>(
                service, METHODID_DELETE_USER)))
        .addMethod(
          getSearchUsersMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              sd2526.trab.api.grpc.Users.SearchUsersArgs,
              sd2526.trab.api.grpc.Users.GrpcUser>(
                service, METHODID_SEARCH_USERS)))
        .build();
  }

  private static abstract class GrpcUsersBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GrpcUsersBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return sd2526.trab.api.grpc.Users.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("GrpcUsers");
    }
  }

  private static final class GrpcUsersFileDescriptorSupplier
      extends GrpcUsersBaseDescriptorSupplier {
    GrpcUsersFileDescriptorSupplier() {}
  }

  private static final class GrpcUsersMethodDescriptorSupplier
      extends GrpcUsersBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    GrpcUsersMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (GrpcUsersGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GrpcUsersFileDescriptorSupplier())
              .addMethod(getPostUserMethod())
              .addMethod(getGetUserMethod())
              .addMethod(getGetUserInternalMethod())
              .addMethod(getUpdateUserMethod())
              .addMethod(getDeleteUserMethod())
              .addMethod(getSearchUsersMethod())
              .build();
        }
      }
    }
    return result;
  }
}
