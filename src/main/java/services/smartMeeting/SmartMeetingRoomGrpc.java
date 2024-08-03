package services.smartMeeting;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Service Methods
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.65.1)",
    comments = "Source: smartMeeting.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class SmartMeetingRoomGrpc {

  private SmartMeetingRoomGrpc() {}

  public static final java.lang.String SERVICE_NAME = "smartMeeting.SmartMeetingRoom";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<services.smartMeeting.SmartMeeting.BookRoomRequest,
      services.smartMeeting.SmartMeeting.ActionResponse> getBookRoomMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "BookRoom",
      requestType = services.smartMeeting.SmartMeeting.BookRoomRequest.class,
      responseType = services.smartMeeting.SmartMeeting.ActionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<services.smartMeeting.SmartMeeting.BookRoomRequest,
      services.smartMeeting.SmartMeeting.ActionResponse> getBookRoomMethod() {
    io.grpc.MethodDescriptor<services.smartMeeting.SmartMeeting.BookRoomRequest, services.smartMeeting.SmartMeeting.ActionResponse> getBookRoomMethod;
    if ((getBookRoomMethod = SmartMeetingRoomGrpc.getBookRoomMethod) == null) {
      synchronized (SmartMeetingRoomGrpc.class) {
        if ((getBookRoomMethod = SmartMeetingRoomGrpc.getBookRoomMethod) == null) {
          SmartMeetingRoomGrpc.getBookRoomMethod = getBookRoomMethod =
              io.grpc.MethodDescriptor.<services.smartMeeting.SmartMeeting.BookRoomRequest, services.smartMeeting.SmartMeeting.ActionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "BookRoom"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartMeeting.SmartMeeting.BookRoomRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartMeeting.SmartMeeting.ActionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SmartMeetingRoomMethodDescriptorSupplier("BookRoom"))
              .build();
        }
      }
    }
    return getBookRoomMethod;
  }

  private static volatile io.grpc.MethodDescriptor<services.smartMeeting.SmartMeeting.CancelBookingRequest,
      services.smartMeeting.SmartMeeting.ActionResponse> getCancelBookingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CancelBooking",
      requestType = services.smartMeeting.SmartMeeting.CancelBookingRequest.class,
      responseType = services.smartMeeting.SmartMeeting.ActionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<services.smartMeeting.SmartMeeting.CancelBookingRequest,
      services.smartMeeting.SmartMeeting.ActionResponse> getCancelBookingMethod() {
    io.grpc.MethodDescriptor<services.smartMeeting.SmartMeeting.CancelBookingRequest, services.smartMeeting.SmartMeeting.ActionResponse> getCancelBookingMethod;
    if ((getCancelBookingMethod = SmartMeetingRoomGrpc.getCancelBookingMethod) == null) {
      synchronized (SmartMeetingRoomGrpc.class) {
        if ((getCancelBookingMethod = SmartMeetingRoomGrpc.getCancelBookingMethod) == null) {
          SmartMeetingRoomGrpc.getCancelBookingMethod = getCancelBookingMethod =
              io.grpc.MethodDescriptor.<services.smartMeeting.SmartMeeting.CancelBookingRequest, services.smartMeeting.SmartMeeting.ActionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CancelBooking"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartMeeting.SmartMeeting.CancelBookingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartMeeting.SmartMeeting.ActionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SmartMeetingRoomMethodDescriptorSupplier("CancelBooking"))
              .build();
        }
      }
    }
    return getCancelBookingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<services.smartMeeting.SmartMeeting.CheckAvailabilityRequest,
      services.smartMeeting.SmartMeeting.AvailabilityResponse> getCheckAvailabilityMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CheckAvailability",
      requestType = services.smartMeeting.SmartMeeting.CheckAvailabilityRequest.class,
      responseType = services.smartMeeting.SmartMeeting.AvailabilityResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<services.smartMeeting.SmartMeeting.CheckAvailabilityRequest,
      services.smartMeeting.SmartMeeting.AvailabilityResponse> getCheckAvailabilityMethod() {
    io.grpc.MethodDescriptor<services.smartMeeting.SmartMeeting.CheckAvailabilityRequest, services.smartMeeting.SmartMeeting.AvailabilityResponse> getCheckAvailabilityMethod;
    if ((getCheckAvailabilityMethod = SmartMeetingRoomGrpc.getCheckAvailabilityMethod) == null) {
      synchronized (SmartMeetingRoomGrpc.class) {
        if ((getCheckAvailabilityMethod = SmartMeetingRoomGrpc.getCheckAvailabilityMethod) == null) {
          SmartMeetingRoomGrpc.getCheckAvailabilityMethod = getCheckAvailabilityMethod =
              io.grpc.MethodDescriptor.<services.smartMeeting.SmartMeeting.CheckAvailabilityRequest, services.smartMeeting.SmartMeeting.AvailabilityResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CheckAvailability"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartMeeting.SmartMeeting.CheckAvailabilityRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartMeeting.SmartMeeting.AvailabilityResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SmartMeetingRoomMethodDescriptorSupplier("CheckAvailability"))
              .build();
        }
      }
    }
    return getCheckAvailabilityMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SmartMeetingRoomStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SmartMeetingRoomStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SmartMeetingRoomStub>() {
        @java.lang.Override
        public SmartMeetingRoomStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SmartMeetingRoomStub(channel, callOptions);
        }
      };
    return SmartMeetingRoomStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SmartMeetingRoomBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SmartMeetingRoomBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SmartMeetingRoomBlockingStub>() {
        @java.lang.Override
        public SmartMeetingRoomBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SmartMeetingRoomBlockingStub(channel, callOptions);
        }
      };
    return SmartMeetingRoomBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SmartMeetingRoomFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SmartMeetingRoomFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SmartMeetingRoomFutureStub>() {
        @java.lang.Override
        public SmartMeetingRoomFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SmartMeetingRoomFutureStub(channel, callOptions);
        }
      };
    return SmartMeetingRoomFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Service Methods
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void bookRoom(services.smartMeeting.SmartMeeting.BookRoomRequest request,
        io.grpc.stub.StreamObserver<services.smartMeeting.SmartMeeting.ActionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getBookRoomMethod(), responseObserver);
    }

    /**
     */
    default void cancelBooking(services.smartMeeting.SmartMeeting.CancelBookingRequest request,
        io.grpc.stub.StreamObserver<services.smartMeeting.SmartMeeting.ActionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCancelBookingMethod(), responseObserver);
    }

    /**
     */
    default void checkAvailability(services.smartMeeting.SmartMeeting.CheckAvailabilityRequest request,
        io.grpc.stub.StreamObserver<services.smartMeeting.SmartMeeting.AvailabilityResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCheckAvailabilityMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service SmartMeetingRoom.
   * <pre>
   * Service Methods
   * </pre>
   */
  public static abstract class SmartMeetingRoomImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return SmartMeetingRoomGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service SmartMeetingRoom.
   * <pre>
   * Service Methods
   * </pre>
   */
  public static final class SmartMeetingRoomStub
      extends io.grpc.stub.AbstractAsyncStub<SmartMeetingRoomStub> {
    private SmartMeetingRoomStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SmartMeetingRoomStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SmartMeetingRoomStub(channel, callOptions);
    }

    /**
     */
    public void bookRoom(services.smartMeeting.SmartMeeting.BookRoomRequest request,
        io.grpc.stub.StreamObserver<services.smartMeeting.SmartMeeting.ActionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getBookRoomMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void cancelBooking(services.smartMeeting.SmartMeeting.CancelBookingRequest request,
        io.grpc.stub.StreamObserver<services.smartMeeting.SmartMeeting.ActionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCancelBookingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void checkAvailability(services.smartMeeting.SmartMeeting.CheckAvailabilityRequest request,
        io.grpc.stub.StreamObserver<services.smartMeeting.SmartMeeting.AvailabilityResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCheckAvailabilityMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service SmartMeetingRoom.
   * <pre>
   * Service Methods
   * </pre>
   */
  public static final class SmartMeetingRoomBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<SmartMeetingRoomBlockingStub> {
    private SmartMeetingRoomBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SmartMeetingRoomBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SmartMeetingRoomBlockingStub(channel, callOptions);
    }

    /**
     */
    public services.smartMeeting.SmartMeeting.ActionResponse bookRoom(services.smartMeeting.SmartMeeting.BookRoomRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getBookRoomMethod(), getCallOptions(), request);
    }

    /**
     */
    public services.smartMeeting.SmartMeeting.ActionResponse cancelBooking(services.smartMeeting.SmartMeeting.CancelBookingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCancelBookingMethod(), getCallOptions(), request);
    }

    /**
     */
    public services.smartMeeting.SmartMeeting.AvailabilityResponse checkAvailability(services.smartMeeting.SmartMeeting.CheckAvailabilityRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCheckAvailabilityMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service SmartMeetingRoom.
   * <pre>
   * Service Methods
   * </pre>
   */
  public static final class SmartMeetingRoomFutureStub
      extends io.grpc.stub.AbstractFutureStub<SmartMeetingRoomFutureStub> {
    private SmartMeetingRoomFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SmartMeetingRoomFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SmartMeetingRoomFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<services.smartMeeting.SmartMeeting.ActionResponse> bookRoom(
        services.smartMeeting.SmartMeeting.BookRoomRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getBookRoomMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<services.smartMeeting.SmartMeeting.ActionResponse> cancelBooking(
        services.smartMeeting.SmartMeeting.CancelBookingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCancelBookingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<services.smartMeeting.SmartMeeting.AvailabilityResponse> checkAvailability(
        services.smartMeeting.SmartMeeting.CheckAvailabilityRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCheckAvailabilityMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_BOOK_ROOM = 0;
  private static final int METHODID_CANCEL_BOOKING = 1;
  private static final int METHODID_CHECK_AVAILABILITY = 2;

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
        case METHODID_BOOK_ROOM:
          serviceImpl.bookRoom((services.smartMeeting.SmartMeeting.BookRoomRequest) request,
              (io.grpc.stub.StreamObserver<services.smartMeeting.SmartMeeting.ActionResponse>) responseObserver);
          break;
        case METHODID_CANCEL_BOOKING:
          serviceImpl.cancelBooking((services.smartMeeting.SmartMeeting.CancelBookingRequest) request,
              (io.grpc.stub.StreamObserver<services.smartMeeting.SmartMeeting.ActionResponse>) responseObserver);
          break;
        case METHODID_CHECK_AVAILABILITY:
          serviceImpl.checkAvailability((services.smartMeeting.SmartMeeting.CheckAvailabilityRequest) request,
              (io.grpc.stub.StreamObserver<services.smartMeeting.SmartMeeting.AvailabilityResponse>) responseObserver);
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
          getBookRoomMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              services.smartMeeting.SmartMeeting.BookRoomRequest,
              services.smartMeeting.SmartMeeting.ActionResponse>(
                service, METHODID_BOOK_ROOM)))
        .addMethod(
          getCancelBookingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              services.smartMeeting.SmartMeeting.CancelBookingRequest,
              services.smartMeeting.SmartMeeting.ActionResponse>(
                service, METHODID_CANCEL_BOOKING)))
        .addMethod(
          getCheckAvailabilityMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              services.smartMeeting.SmartMeeting.CheckAvailabilityRequest,
              services.smartMeeting.SmartMeeting.AvailabilityResponse>(
                service, METHODID_CHECK_AVAILABILITY)))
        .build();
  }

  private static abstract class SmartMeetingRoomBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SmartMeetingRoomBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return services.smartMeeting.SmartMeeting.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SmartMeetingRoom");
    }
  }

  private static final class SmartMeetingRoomFileDescriptorSupplier
      extends SmartMeetingRoomBaseDescriptorSupplier {
    SmartMeetingRoomFileDescriptorSupplier() {}
  }

  private static final class SmartMeetingRoomMethodDescriptorSupplier
      extends SmartMeetingRoomBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    SmartMeetingRoomMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (SmartMeetingRoomGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SmartMeetingRoomFileDescriptorSupplier())
              .addMethod(getBookRoomMethod())
              .addMethod(getCancelBookingMethod())
              .addMethod(getCheckAvailabilityMethod())
              .build();
        }
      }
    }
    return result;
  }
}
