package client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;

import services.constants.Constants;
import services.smartAccess.*;
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
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        String jwtToken = JwtUtility.generateToken("testClientId");
        Metadata metadata = new Metadata();
        metadata.put(Constants.AUTHORIZATION_METADATA_KEY, Constants.BEARER_TYPE + " " + jwtToken);

        accessControlStub = SmartAccessControlGrpc.newBlockingStub(channel);
        coffeeMachineStub = SmartCoffeeMachineGrpc.newBlockingStub(channel);
        meetingRoomStub = SmartMeetingRoomGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public String accessControl() {
        UnlockDoorRequest request = UnlockDoorRequest.newBuilder()
                .setDoorId(1)
                .setCredentials(AccessCredentials.newBuilder().setUserId(1).setLevel(AccessLevel.ADMIN).build())
                .build();
        services.smartAccess.ActionResponse response = accessControlStub.unlockDoor(request);
        return "Access control response: " + response.toString();
    }

    public String brewCoffee() {
        BrewCoffeeRequest request = BrewCoffeeRequest.newBuilder()
                .setCoffeeType(CoffeeType.AMERICANO)
                .build();
        services.smartCoffee.ActionResponse response = coffeeMachineStub.brewCoffee(request);
        return "Coffee brewed: " + response.toString();
    }

    public String bookRoom(int roomId, int userId, String time) {
        BookRoomRequest request = BookRoomRequest.newBuilder()
                .setRoomId(roomId)
                .setUserId(userId)
                .setTimeSlot(time)
                .build();
        services.smartMeeting.ActionResponse response = meetingRoomStub.bookRoom(request);
        return "Room booking response: " + response.toString();
    }
}