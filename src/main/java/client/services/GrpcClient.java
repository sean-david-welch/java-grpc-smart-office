package client.services;

import io.grpc.*;
import services.constants.Constants;
import services.smartAccess.AccessLevel;
import services.smartAccess.SmartAccessControlGrpc;
import services.smartCoffee.CoffeeType;
import services.smartCoffee.SmartCoffeeMachineGrpc;
import services.smartMeeting.SmartMeetingRoomGrpc;
import services.utils.JwtUtility;

import java.util.concurrent.TimeUnit;

public class GrpcClient {
    private final ManagedChannel channel;
    private final AccessControlService accessControlService;
    private final CoffeeMachineService coffeeMachineService;
    private final MeetingRoomService meetingRoomService;

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

        accessControlService = new AccessControlService(SmartAccessControlGrpc.newBlockingStub(channel));
        coffeeMachineService = new CoffeeMachineService(SmartCoffeeMachineGrpc.newBlockingStub(channel));
        meetingRoomService = new MeetingRoomService(SmartMeetingRoomGrpc.newBlockingStub(channel));
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public String accessControl(int userId, AccessLevel level) {
        return accessControlService.unlockDoor(userId, level);
    }

    public String brewCoffee(CoffeeType coffeeType) {
        return coffeeMachineService.brewCoffee(coffeeType);
    }

    public String bookRoom(int roomId, int userId, String time) {
        return meetingRoomService.bookRoom(roomId, userId, time);
    }
}
