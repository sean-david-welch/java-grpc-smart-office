package services.smartCoffee;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Service Methods
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.65.1)",
    comments = "Source: smartCoffee.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class SmartCoffeeMachineGrpc {

  private SmartCoffeeMachineGrpc() {}

  public static final java.lang.String SERVICE_NAME = "smartCoffee.SmartCoffeeMachine";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<services.smartCoffee.SmartCoffee.BrewCoffeeRequest,
      services.smartCoffee.SmartCoffee.ActionResponse> getBrewCoffeeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "BrewCoffee",
      requestType = services.smartCoffee.SmartCoffee.BrewCoffeeRequest.class,
      responseType = services.smartCoffee.SmartCoffee.ActionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<services.smartCoffee.SmartCoffee.BrewCoffeeRequest,
      services.smartCoffee.SmartCoffee.ActionResponse> getBrewCoffeeMethod() {
    io.grpc.MethodDescriptor<services.smartCoffee.SmartCoffee.BrewCoffeeRequest, services.smartCoffee.SmartCoffee.ActionResponse> getBrewCoffeeMethod;
    if ((getBrewCoffeeMethod = SmartCoffeeMachineGrpc.getBrewCoffeeMethod) == null) {
      synchronized (SmartCoffeeMachineGrpc.class) {
        if ((getBrewCoffeeMethod = SmartCoffeeMachineGrpc.getBrewCoffeeMethod) == null) {
          SmartCoffeeMachineGrpc.getBrewCoffeeMethod = getBrewCoffeeMethod =
              io.grpc.MethodDescriptor.<services.smartCoffee.SmartCoffee.BrewCoffeeRequest, services.smartCoffee.SmartCoffee.ActionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "BrewCoffee"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartCoffee.SmartCoffee.BrewCoffeeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartCoffee.SmartCoffee.ActionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SmartCoffeeMachineMethodDescriptorSupplier("BrewCoffee"))
              .build();
        }
      }
    }
    return getBrewCoffeeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<services.smartCoffee.SmartCoffee.CheckInventoryRequest,
      services.smartCoffee.SmartCoffee.InventoryResponse> getCheckInventoryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CheckInventory",
      requestType = services.smartCoffee.SmartCoffee.CheckInventoryRequest.class,
      responseType = services.smartCoffee.SmartCoffee.InventoryResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<services.smartCoffee.SmartCoffee.CheckInventoryRequest,
      services.smartCoffee.SmartCoffee.InventoryResponse> getCheckInventoryMethod() {
    io.grpc.MethodDescriptor<services.smartCoffee.SmartCoffee.CheckInventoryRequest, services.smartCoffee.SmartCoffee.InventoryResponse> getCheckInventoryMethod;
    if ((getCheckInventoryMethod = SmartCoffeeMachineGrpc.getCheckInventoryMethod) == null) {
      synchronized (SmartCoffeeMachineGrpc.class) {
        if ((getCheckInventoryMethod = SmartCoffeeMachineGrpc.getCheckInventoryMethod) == null) {
          SmartCoffeeMachineGrpc.getCheckInventoryMethod = getCheckInventoryMethod =
              io.grpc.MethodDescriptor.<services.smartCoffee.SmartCoffee.CheckInventoryRequest, services.smartCoffee.SmartCoffee.InventoryResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CheckInventory"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartCoffee.SmartCoffee.CheckInventoryRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartCoffee.SmartCoffee.InventoryResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SmartCoffeeMachineMethodDescriptorSupplier("CheckInventory"))
              .build();
        }
      }
    }
    return getCheckInventoryMethod;
  }

  private static volatile io.grpc.MethodDescriptor<services.smartCoffee.SmartCoffee.RefillItemRequest,
      services.smartCoffee.SmartCoffee.InventoryResponse> getRefillInventoryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RefillInventory",
      requestType = services.smartCoffee.SmartCoffee.RefillItemRequest.class,
      responseType = services.smartCoffee.SmartCoffee.InventoryResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<services.smartCoffee.SmartCoffee.RefillItemRequest,
      services.smartCoffee.SmartCoffee.InventoryResponse> getRefillInventoryMethod() {
    io.grpc.MethodDescriptor<services.smartCoffee.SmartCoffee.RefillItemRequest, services.smartCoffee.SmartCoffee.InventoryResponse> getRefillInventoryMethod;
    if ((getRefillInventoryMethod = SmartCoffeeMachineGrpc.getRefillInventoryMethod) == null) {
      synchronized (SmartCoffeeMachineGrpc.class) {
        if ((getRefillInventoryMethod = SmartCoffeeMachineGrpc.getRefillInventoryMethod) == null) {
          SmartCoffeeMachineGrpc.getRefillInventoryMethod = getRefillInventoryMethod =
              io.grpc.MethodDescriptor.<services.smartCoffee.SmartCoffee.RefillItemRequest, services.smartCoffee.SmartCoffee.InventoryResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RefillInventory"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartCoffee.SmartCoffee.RefillItemRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  services.smartCoffee.SmartCoffee.InventoryResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SmartCoffeeMachineMethodDescriptorSupplier("RefillInventory"))
              .build();
        }
      }
    }
    return getRefillInventoryMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SmartCoffeeMachineStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SmartCoffeeMachineStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SmartCoffeeMachineStub>() {
        @java.lang.Override
        public SmartCoffeeMachineStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SmartCoffeeMachineStub(channel, callOptions);
        }
      };
    return SmartCoffeeMachineStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SmartCoffeeMachineBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SmartCoffeeMachineBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SmartCoffeeMachineBlockingStub>() {
        @java.lang.Override
        public SmartCoffeeMachineBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SmartCoffeeMachineBlockingStub(channel, callOptions);
        }
      };
    return SmartCoffeeMachineBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SmartCoffeeMachineFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SmartCoffeeMachineFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SmartCoffeeMachineFutureStub>() {
        @java.lang.Override
        public SmartCoffeeMachineFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SmartCoffeeMachineFutureStub(channel, callOptions);
        }
      };
    return SmartCoffeeMachineFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Service Methods
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void brewCoffee(services.smartCoffee.SmartCoffee.BrewCoffeeRequest request,
        io.grpc.stub.StreamObserver<services.smartCoffee.SmartCoffee.ActionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getBrewCoffeeMethod(), responseObserver);
    }

    /**
     */
    default void checkInventory(services.smartCoffee.SmartCoffee.CheckInventoryRequest request,
        io.grpc.stub.StreamObserver<services.smartCoffee.SmartCoffee.InventoryResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCheckInventoryMethod(), responseObserver);
    }

    /**
     */
    default void refillInventory(services.smartCoffee.SmartCoffee.RefillItemRequest request,
        io.grpc.stub.StreamObserver<services.smartCoffee.SmartCoffee.InventoryResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRefillInventoryMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service SmartCoffeeMachine.
   * <pre>
   * Service Methods
   * </pre>
   */
  public static abstract class SmartCoffeeMachineImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return SmartCoffeeMachineGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service SmartCoffeeMachine.
   * <pre>
   * Service Methods
   * </pre>
   */
  public static final class SmartCoffeeMachineStub
      extends io.grpc.stub.AbstractAsyncStub<SmartCoffeeMachineStub> {
    private SmartCoffeeMachineStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SmartCoffeeMachineStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SmartCoffeeMachineStub(channel, callOptions);
    }

    /**
     */
    public void brewCoffee(services.smartCoffee.SmartCoffee.BrewCoffeeRequest request,
        io.grpc.stub.StreamObserver<services.smartCoffee.SmartCoffee.ActionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getBrewCoffeeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void checkInventory(services.smartCoffee.SmartCoffee.CheckInventoryRequest request,
        io.grpc.stub.StreamObserver<services.smartCoffee.SmartCoffee.InventoryResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCheckInventoryMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void refillInventory(services.smartCoffee.SmartCoffee.RefillItemRequest request,
        io.grpc.stub.StreamObserver<services.smartCoffee.SmartCoffee.InventoryResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRefillInventoryMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service SmartCoffeeMachine.
   * <pre>
   * Service Methods
   * </pre>
   */
  public static final class SmartCoffeeMachineBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<SmartCoffeeMachineBlockingStub> {
    private SmartCoffeeMachineBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SmartCoffeeMachineBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SmartCoffeeMachineBlockingStub(channel, callOptions);
    }

    /**
     */
    public services.smartCoffee.SmartCoffee.ActionResponse brewCoffee(services.smartCoffee.SmartCoffee.BrewCoffeeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getBrewCoffeeMethod(), getCallOptions(), request);
    }

    /**
     */
    public services.smartCoffee.SmartCoffee.InventoryResponse checkInventory(services.smartCoffee.SmartCoffee.CheckInventoryRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCheckInventoryMethod(), getCallOptions(), request);
    }

    /**
     */
    public services.smartCoffee.SmartCoffee.InventoryResponse refillInventory(services.smartCoffee.SmartCoffee.RefillItemRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRefillInventoryMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service SmartCoffeeMachine.
   * <pre>
   * Service Methods
   * </pre>
   */
  public static final class SmartCoffeeMachineFutureStub
      extends io.grpc.stub.AbstractFutureStub<SmartCoffeeMachineFutureStub> {
    private SmartCoffeeMachineFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SmartCoffeeMachineFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SmartCoffeeMachineFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<services.smartCoffee.SmartCoffee.ActionResponse> brewCoffee(
        services.smartCoffee.SmartCoffee.BrewCoffeeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getBrewCoffeeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<services.smartCoffee.SmartCoffee.InventoryResponse> checkInventory(
        services.smartCoffee.SmartCoffee.CheckInventoryRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCheckInventoryMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<services.smartCoffee.SmartCoffee.InventoryResponse> refillInventory(
        services.smartCoffee.SmartCoffee.RefillItemRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRefillInventoryMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_BREW_COFFEE = 0;
  private static final int METHODID_CHECK_INVENTORY = 1;
  private static final int METHODID_REFILL_INVENTORY = 2;

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
        case METHODID_BREW_COFFEE:
          serviceImpl.brewCoffee((services.smartCoffee.SmartCoffee.BrewCoffeeRequest) request,
              (io.grpc.stub.StreamObserver<services.smartCoffee.SmartCoffee.ActionResponse>) responseObserver);
          break;
        case METHODID_CHECK_INVENTORY:
          serviceImpl.checkInventory((services.smartCoffee.SmartCoffee.CheckInventoryRequest) request,
              (io.grpc.stub.StreamObserver<services.smartCoffee.SmartCoffee.InventoryResponse>) responseObserver);
          break;
        case METHODID_REFILL_INVENTORY:
          serviceImpl.refillInventory((services.smartCoffee.SmartCoffee.RefillItemRequest) request,
              (io.grpc.stub.StreamObserver<services.smartCoffee.SmartCoffee.InventoryResponse>) responseObserver);
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
          getBrewCoffeeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              services.smartCoffee.SmartCoffee.BrewCoffeeRequest,
              services.smartCoffee.SmartCoffee.ActionResponse>(
                service, METHODID_BREW_COFFEE)))
        .addMethod(
          getCheckInventoryMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              services.smartCoffee.SmartCoffee.CheckInventoryRequest,
              services.smartCoffee.SmartCoffee.InventoryResponse>(
                service, METHODID_CHECK_INVENTORY)))
        .addMethod(
          getRefillInventoryMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              services.smartCoffee.SmartCoffee.RefillItemRequest,
              services.smartCoffee.SmartCoffee.InventoryResponse>(
                service, METHODID_REFILL_INVENTORY)))
        .build();
  }

  private static abstract class SmartCoffeeMachineBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SmartCoffeeMachineBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return services.smartCoffee.SmartCoffee.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SmartCoffeeMachine");
    }
  }

  private static final class SmartCoffeeMachineFileDescriptorSupplier
      extends SmartCoffeeMachineBaseDescriptorSupplier {
    SmartCoffeeMachineFileDescriptorSupplier() {}
  }

  private static final class SmartCoffeeMachineMethodDescriptorSupplier
      extends SmartCoffeeMachineBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    SmartCoffeeMachineMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (SmartCoffeeMachineGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SmartCoffeeMachineFileDescriptorSupplier())
              .addMethod(getBrewCoffeeMethod())
              .addMethod(getCheckInventoryMethod())
              .addMethod(getRefillInventoryMethod())
              .build();
        }
      }
    }
    return result;
  }
}
