package client.services;

import io.grpc.*;
import services.constants.Constants;
import services.smartAccess.AccessCredentials;
import services.smartAccess.AccessLevel;
import services.smartAccess.SmartAccessControlGrpc;
import services.smartAccess.UnlockDoorRequest;
import services.smartCoffee.BrewCoffeeRequest;
import services.smartCoffee.CoffeeType;
import services.smartCoffee.SmartCoffeeMachineGrpc;
import services.smartMeeting.BookRoomRequest;
import services.smartMeeting.SmartMeetingRoomGrpc;
import services.utils.JwtUtility;

import java.util.concurrent.TimeUnit;

public class GrpcClient {
    private final ManagedChannel channel;
    private final SmartAccessControlGrpc.SmartAccessControlBlockingStub accessControlStub;
    private final SmartCoffeeMachineGrpc.SmartCoffeeMachineBlockingStub coffeeMachineStub;
    private final SmartMeetingRoomGrpc.SmartMeetingRoomBlockingStub meetingRoomStub;

    public GrpcClient(String host, int port) {
        String jwtToken = JwtUtility.generateToken("testClientId");

        ClientInterceptor authInterceptor = new ClientInterceptor() {
            @Override
            public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
                    MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
                return new ForwardingClientCall.SimpleForwardingClientCall<>(
                        next.newCall(method, callOptions)) {
                    @Override
                    public void start(Listener<RespT> responseListener, Metadata headers) {
                        headers.put(Constants.AUTHORIZATION_METADATA_KEY, Constants.BEARER_TYPE + " " + jwtToken);
                        super.start(responseListener, headers);
                    }
                };
            }
        };

        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .intercept(authInterceptor)
                .build();

        Metadata metadata = new Metadata();
        metadata.put(Constants.AUTHORIZATION_METADATA_KEY, Constants.BEARER_TYPE + " " + jwtToken);

        accessControlStub = SmartAccessControlGrpc.newBlockingStub(channel);
        coffeeMachineStub = SmartCoffeeMachineGrpc.newBlockingStub(channel);
        meetingRoomStub = SmartMeetingRoomGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public String accessControl(int userId, AccessLevel level) {
        try {
            UnlockDoorRequest request = UnlockDoorRequest.newBuilder()
                    .setDoorId(1)
                    .setCredentials(AccessCredentials.newBuilder().setUserId(userId).setLevel(level).build())
                    .build();
            services.smartAccess.ActionResponse response = accessControlStub.unlockDoor(request);
            return "Access control response: " + response.toString();
        } catch (StatusRuntimeException e) {
            return "Error: " + e.getStatus().getDescription();
        }
    }

    public String brewCoffee(CoffeeType coffeeType) {
        try {
            BrewCoffeeRequest request = BrewCoffeeRequest.newBuilder()
                    .setCoffeeType(coffeeType)
                    .build();
            services.smartCoffee.ActionResponse response = coffeeMachineStub.brewCoffee(request);
            return "Coffee brewed: " + response.toString();
        } catch (StatusRuntimeException e) {
            return "Error: " + e.getStatus().getDescription();
        }
    }

    public String bookRoom(int roomId, int userId, String time) {
        try {
            BookRoomRequest request = BookRoomRequest.newBuilder()
                    .setRoomId(roomId)
                    .setUserId(userId)
                    .setTimeSlot(time)
                    .build();
            services.smartMeeting.ActionResponse response = meetingRoomStub.bookRoom(request);
            return "Room booking response: " + response.toString();
        } catch (StatusRuntimeException e) {
            return "Error: " + e.getStatus().getDescription();
        }
    }
}