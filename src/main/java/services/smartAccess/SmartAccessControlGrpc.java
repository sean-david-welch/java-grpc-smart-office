package services.smartAccess;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Service methods
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.65.1)",
    comments = "Source: smartAccess.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class SmartAccessControlGrpc {

  private SmartAccessControlGrpc() {}

  public static final java.lang.String SERVICE_NAME = "smartAccess.SmartAccessControl";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<services.smartAccess.SmartAccess.UnlockDoorRequest,
      services.smartAccess.SmartAccess.ActionResponse> getUnlockDoorMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UnlockDoor",
      requestType = services.smartAccess.SmartAccess.UnlockDoorRequest.class,
      responseType = services.smartAccess.SmartAccess.ActionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<services.smartAccess.SmartAccess.UnlockDoorRequest,
      services.smartAccess.SmartAccess.ActionResponse> getUnlockDoorMethod() {
    io.grpc.MethodDescriptor<services.smartAccess.SmartAccess.UnlockDoorRequest, services.smartAccess.SmartAccess.ActionResponse> getUnlockDoorMethod;
    if ((getUnlockDoorMethod = SmartAccessControlGrpc.getUnlockDoorMethod) == null) {
      synchronized (SmartAccessControlGrpc.class) {
        if ((getUnlockDoorMethod = SmartAccessControlGrpc.getUnlockDoorMethod) == null) {
          SmartAccessControlGrpc.getUnlockDoorMethod = getUnlockDoorMethod =
              io.grpc.MethodDescriptor.<services.smartAccess.SmartAccess.UnlockDoorRequest, services.smartAccess.SmartAccess.ActionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UnlockDoor"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartAccess.SmartAccess.UnlockDoorRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartAccess.SmartAccess.ActionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SmartAccessControlMethodDescriptorSupplier("UnlockDoor"))
              .build();
        }
      }
    }
    return getUnlockDoorMethod;
  }

  private static volatile io.grpc.MethodDescriptor<services.smartAccess.SmartAccess.RaiseAlarmRequest,
      services.smartAccess.SmartAccess.ActionResponse> getRaiseAlarmMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RaiseAlarm",
      requestType = services.smartAccess.SmartAccess.RaiseAlarmRequest.class,
      responseType = services.smartAccess.SmartAccess.ActionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<services.smartAccess.SmartAccess.RaiseAlarmRequest,
      services.smartAccess.SmartAccess.ActionResponse> getRaiseAlarmMethod() {
    io.grpc.MethodDescriptor<services.smartAccess.SmartAccess.RaiseAlarmRequest, services.smartAccess.SmartAccess.ActionResponse> getRaiseAlarmMethod;
    if ((getRaiseAlarmMethod = SmartAccessControlGrpc.getRaiseAlarmMethod) == null) {
      synchronized (SmartAccessControlGrpc.class) {
        if ((getRaiseAlarmMethod = SmartAccessControlGrpc.getRaiseAlarmMethod) == null) {
          SmartAccessControlGrpc.getRaiseAlarmMethod = getRaiseAlarmMethod =
              io.grpc.MethodDescriptor.<services.smartAccess.SmartAccess.RaiseAlarmRequest, services.smartAccess.SmartAccess.ActionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RaiseAlarm"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartAccess.SmartAccess.RaiseAlarmRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartAccess.SmartAccess.ActionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SmartAccessControlMethodDescriptorSupplier("RaiseAlarm"))
              .build();
        }
      }
    }
    return getRaiseAlarmMethod;
  }

  private static volatile io.grpc.MethodDescriptor<services.smartAccess.SmartAccess.GetAccessLogsRequest,
      services.smartAccess.SmartAccess.AccessLogsResponse> getGetAccessLogsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAccessLogs",
      requestType = services.smartAccess.SmartAccess.GetAccessLogsRequest.class,
      responseType = services.smartAccess.SmartAccess.AccessLogsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<services.smartAccess.SmartAccess.GetAccessLogsRequest,
      services.smartAccess.SmartAccess.AccessLogsResponse> getGetAccessLogsMethod() {
    io.grpc.MethodDescriptor<services.smartAccess.SmartAccess.GetAccessLogsRequest, services.smartAccess.SmartAccess.AccessLogsResponse> getGetAccessLogsMethod;
    if ((getGetAccessLogsMethod = SmartAccessControlGrpc.getGetAccessLogsMethod) == null) {
      synchronized (SmartAccessControlGrpc.class) {
        if ((getGetAccessLogsMethod = SmartAccessControlGrpc.getGetAccessLogsMethod) == null) {
          SmartAccessControlGrpc.getGetAccessLogsMethod = getGetAccessLogsMethod =
              io.grpc.MethodDescriptor.<services.smartAccess.SmartAccess.GetAccessLogsRequest, services.smartAccess.SmartAccess.AccessLogsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAccessLogs"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartAccess.SmartAccess.GetAccessLogsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartAccess.SmartAccess.AccessLogsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SmartAccessControlMethodDescriptorSupplier("GetAccessLogs"))
              .build();
        }
      }
    }
    return getGetAccessLogsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SmartAccessControlStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SmartAccessControlStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SmartAccessControlStub>() {
        @java.lang.Override
        public SmartAccessControlStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SmartAccessControlStub(channel, callOptions);
        }
      };
    return SmartAccessControlStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SmartAccessControlBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SmartAccessControlBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SmartAccessControlBlockingStub>() {
        @java.lang.Override
        public SmartAccessControlBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SmartAccessControlBlockingStub(channel, callOptions);
        }
      };
    return SmartAccessControlBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SmartAccessControlFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SmartAccessControlFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SmartAccessControlFutureStub>() {
        @java.lang.Override
        public SmartAccessControlFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SmartAccessControlFutureStub(channel, callOptions);
        }
      };
    return SmartAccessControlFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Service methods
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void unlockDoor(services.smartAccess.SmartAccess.UnlockDoorRequest request,
        io.grpc.stub.StreamObserver<services.smartAccess.SmartAccess.ActionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUnlockDoorMethod(), responseObserver);
    }

    /**
     */
    default void raiseAlarm(services.smartAccess.SmartAccess.RaiseAlarmRequest request,
        io.grpc.stub.StreamObserver<services.smartAccess.SmartAccess.ActionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRaiseAlarmMethod(), responseObserver);
    }

    /**
     */
    default void getAccessLogs(services.smartAccess.SmartAccess.GetAccessLogsRequest request,
        io.grpc.stub.StreamObserver<services.smartAccess.SmartAccess.AccessLogsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAccessLogsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service SmartAccessControl.
   * <pre>
   * Service methods
   * </pre>
   */
  public static abstract class SmartAccessControlImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return SmartAccessControlGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service SmartAccessControl.
   * <pre>
   * Service methods
   * </pre>
   */
  public static final class SmartAccessControlStub
      extends io.grpc.stub.AbstractAsyncStub<SmartAccessControlStub> {
    private SmartAccessControlStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SmartAccessControlStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SmartAccessControlStub(channel, callOptions);
    }

    /**
     */
    public void unlockDoor(services.smartAccess.SmartAccess.UnlockDoorRequest request,
        io.grpc.stub.StreamObserver<services.smartAccess.SmartAccess.ActionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUnlockDoorMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void raiseAlarm(services.smartAccess.SmartAccess.RaiseAlarmRequest request,
        io.grpc.stub.StreamObserver<services.smartAccess.SmartAccess.ActionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRaiseAlarmMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAccessLogs(services.smartAccess.SmartAccess.GetAccessLogsRequest request,
        io.grpc.stub.StreamObserver<services.smartAccess.SmartAccess.AccessLogsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAccessLogsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service SmartAccessControl.
   * <pre>
   * Service methods
   * </pre>
   */
  public static final class SmartAccessControlBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<SmartAccessControlBlockingStub> {
    private SmartAccessControlBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SmartAccessControlBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SmartAccessControlBlockingStub(channel, callOptions);
    }

    /**
     */
    public services.smartAccess.SmartAccess.ActionResponse unlockDoor(services.smartAccess.SmartAccess.UnlockDoorRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUnlockDoorMethod(), getCallOptions(), request);
    }

    /**
     */
    public services.smartAccess.SmartAccess.ActionResponse raiseAlarm(services.smartAccess.SmartAccess.RaiseAlarmRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRaiseAlarmMethod(), getCallOptions(), request);
    }

    /**
     */
    public services.smartAccess.SmartAccess.AccessLogsResponse getAccessLogs(services.smartAccess.SmartAccess.GetAccessLogsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAccessLogsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service SmartAccessControl.
   * <pre>
   * Service methods
   * </pre>
   */
  public static final class SmartAccessControlFutureStub
      extends io.grpc.stub.AbstractFutureStub<SmartAccessControlFutureStub> {
    private SmartAccessControlFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SmartAccessControlFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SmartAccessControlFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<services.smartAccess.SmartAccess.ActionResponse> unlockDoor(
        services.smartAccess.SmartAccess.UnlockDoorRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUnlockDoorMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<services.smartAccess.SmartAccess.ActionResponse> raiseAlarm(
        services.smartAccess.SmartAccess.RaiseAlarmRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRaiseAlarmMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<services.smartAccess.SmartAccess.AccessLogsResponse> getAccessLogs(
        services.smartAccess.SmartAccess.GetAccessLogsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAccessLogsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_UNLOCK_DOOR = 0;
  private static final int METHODID_RAISE_ALARM = 1;
  private static final int METHODID_GET_ACCESS_LOGS = 2;

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
        case METHODID_UNLOCK_DOOR:
          serviceImpl.unlockDoor((services.smartAccess.SmartAccess.UnlockDoorRequest) request,
              (io.grpc.stub.StreamObserver<services.smartAccess.SmartAccess.ActionResponse>) responseObserver);
          break;
        case METHODID_RAISE_ALARM:
          serviceImpl.raiseAlarm((services.smartAccess.SmartAccess.RaiseAlarmRequest) request,
              (io.grpc.stub.StreamObserver<services.smartAccess.SmartAccess.ActionResponse>) responseObserver);
          break;
        case METHODID_GET_ACCESS_LOGS:
          serviceImpl.getAccessLogs((services.smartAccess.SmartAccess.GetAccessLogsRequest) request,
              (io.grpc.stub.StreamObserver<services.smartAccess.SmartAccess.AccessLogsResponse>) responseObserver);
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
          getUnlockDoorMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              services.smartAccess.SmartAccess.UnlockDoorRequest,
              services.smartAccess.SmartAccess.ActionResponse>(
                service, METHODID_UNLOCK_DOOR)))
        .addMethod(
          getRaiseAlarmMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              services.smartAccess.SmartAccess.RaiseAlarmRequest,
              services.smartAccess.SmartAccess.ActionResponse>(
                service, METHODID_RAISE_ALARM)))
        .addMethod(
          getGetAccessLogsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              services.smartAccess.SmartAccess.GetAccessLogsRequest,
              services.smartAccess.SmartAccess.AccessLogsResponse>(
                service, METHODID_GET_ACCESS_LOGS)))
        .build();
  }

  private static abstract class SmartAccessControlBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SmartAccessControlBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return services.smartAccess.SmartAccess.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SmartAccessControl");
    }
  }

  private static final class SmartAccessControlFileDescriptorSupplier
      extends SmartAccessControlBaseDescriptorSupplier {
    SmartAccessControlFileDescriptorSupplier() {}
  }

  private static final class SmartAccessControlMethodDescriptorSupplier
      extends SmartAccessControlBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    SmartAccessControlMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (SmartAccessControlGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SmartAccessControlFileDescriptorSupplier())
              .addMethod(getUnlockDoorMethod())
              .addMethod(getRaiseAlarmMethod())
              .addMethod(getGetAccessLogsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
