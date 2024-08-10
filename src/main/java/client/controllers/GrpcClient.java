package client.controllers;

import io.grpc.*;
import services.constants.Constants;
import services.smartAccess.AccessLevel;
import services.smartAccess.AccessLogsResponse;
import services.smartAccess.SmartAccessControlGrpc;
import services.smartCoffee.CoffeeType;
import services.smartCoffee.InventoryItem;
import services.smartCoffee.SmartCoffeeMachineGrpc;
import services.smartMeeting.SmartMeetingRoomGrpc;
import services.utils.JwtUtility;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


public class GrpcClient {
    private final ManagedChannel channel;
    private final AccessControlClientController accessControlController;
    private final CoffeeMachineClientController coffeeMachineController;
    private final MeetingRoomClientController meetingRoomController;

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

        // Access control
        SmartAccessControlGrpc.SmartAccessControlBlockingStub accessBlockingStub = SmartAccessControlGrpc.newBlockingStub(channel);
        SmartAccessControlGrpc.SmartAccessControlStub accessAsyncStub = SmartAccessControlGrpc.newStub(channel);
        accessControlController = new AccessControlClientController(accessBlockingStub, accessAsyncStub);

        // Coffee machine
        SmartCoffeeMachineGrpc.SmartCoffeeMachineBlockingStub coffeeBlockingStub = SmartCoffeeMachineGrpc.newBlockingStub(channel);
        SmartCoffeeMachineGrpc.SmartCoffeeMachineStub coffeeAsyncStub = SmartCoffeeMachineGrpc.newStub(channel);
        coffeeMachineController = new CoffeeMachineClientController(coffeeBlockingStub, coffeeAsyncStub);

        // Meeting room
        SmartMeetingRoomGrpc.SmartMeetingRoomBlockingStub meetingBlockingStub = SmartMeetingRoomGrpc.newBlockingStub(channel);
        meetingRoomController = new MeetingRoomClientController(meetingBlockingStub);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    // Access control methods
    public String unlockDoor(int doorId, int userId, AccessLevel level) {
        return accessControlController.unlockDoor(doorId, userId, level);
    }

    public String raiseAlarm(int doorId, int userId, AccessLevel level) {
        return accessControlController.raiseAlarm(doorId, userId, level);
    }

    public List<AccessLogsResponse> getAccessLogs(int doorId, String startTime, String endTime) {
        return accessControlController.getAccessLogs(doorId, startTime, endTime);
    }

    // Coffee Machine Methods
    public String brewCoffee(CoffeeType coffeeType) {
        return coffeeMachineController.brewCoffee(coffeeType, Deadline.after(5, TimeUnit.SECONDS));
    }

    public String checkInventory(Optional<InventoryItem> itemOptional) {
        return coffeeMachineController.checkInventory(itemOptional);
    }

    public String refillInventory(Map<InventoryItem, Integer> itemsToRefill) {
        return coffeeMachineController.refillInventory(itemsToRefill);
    }

    // Meeting Room Methods
    public String bookRoom(int roomId, int userId, String time) {
        return meetingRoomController.bookRoom(roomId, userId, time);
    }

    public String cancelBooking(int bookingId) {
        return meetingRoomController.cancelBooking(bookingId);
    }

    public String checkAvailability(int roomId) {
        return meetingRoomController.checkAvailability(roomId);
    }
}
