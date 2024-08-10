package client.services;

import io.grpc.*;
import services.constants.Constants;
import services.smartAccess.AccessLevel;
import services.smartAccess.AccessLogsResponse;
import services.smartAccess.SmartAccessControlGrpc;
import services.smartCoffee.CoffeeType;
import services.smartCoffee.SmartCoffeeMachineGrpc;
import services.smartMeeting.SmartMeetingRoomGrpc;
import services.utils.JwtUtility;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class GrpcClient {
    private final ManagedChannel channel;
    private final AccessControlClientController accessControlService;
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

        SmartAccessControlGrpc.SmartAccessControlBlockingStub blockingStub = SmartAccessControlGrpc.newBlockingStub(channel);
        SmartAccessControlGrpc.SmartAccessControlStub asyncStub = SmartAccessControlGrpc.newStub(channel);

        accessControlService = new AccessControlClientController(blockingStub, asyncStub);
        coffeeMachineService = new CoffeeMachineService(SmartCoffeeMachineGrpc.newBlockingStub(channel));
        meetingRoomService = new MeetingRoomService(SmartMeetingRoomGrpc.newBlockingStub(channel));
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    // Access control methods
    public String unlockDoor(int userId, AccessLevel level) {
        return accessControlService.unlockDoor(userId, level);
    }

    public String raiseAlarm(int doorId, int userId, AccessLevel level) {
        return accessControlService.raiseAlarm(doorId, userId, level);
    }

    public List<AccessLogsResponse> getAccessLogs(int doorId, String startTime, String endTime) {
        return accessControlService.getAccessLogs(doorId, startTime, endTime);
    }

    // Coffee Machine Methods
    public String brewCoffee(CoffeeType coffeeType) {
        return coffeeMachineService.brewCoffee(coffeeType);
    }

    // Meeting Room Methods
    public String bookRoom(int roomId, int userId, String time) {
        return meetingRoomService.bookRoom(roomId, userId, time);
    }
}
