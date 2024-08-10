package client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;

import services.constants.Constants;
import services.smartAccess.*;
import services.smartCoffee.SmartCoffeeMachineGrpc;
import services.smartMeeting.SmartMeetingRoomGrpc;
import services.utils.JwtUtility;

import java.util.concurrent.TimeUnit;

public class GrpcClient {
    private final ManagedChannel channel;
    private final SmartAccessControlGrpc.SmartAccessControlBlockingStub accessControlStub;

    public GrpcClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        String jwtToken = JwtUtility.generateToken("testClientId");
        Metadata metadata = new Metadata();
        metadata.put(Constants.AUTHORIZATION_METADATA_KEY, Constants.BEARER_TYPE + " " + jwtToken);

        accessControlStub = SmartAccessControlGrpc.newBlockingStub(channel);

         SmartAccessControlGrpc.SmartAccessControlBlockingStub accessControlStub = SmartAccessControlGrpc.newBlockingStub(channel);
         SmartCoffeeMachineGrpc.SmartCoffeeMachineBlockingStub coffeeMachineStub = SmartCoffeeMachineGrpc.newBlockingStub(channel);
         SmartMeetingRoomGrpc.SmartMeetingRoomBlockingStub meetingRoomStub = SmartMeetingRoomGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public String accessControl() {
        UnlockDoorRequest request = UnlockDoorRequest.newBuilder()
                .setDoorId(1)
                .setCredentials(AccessCredentials.newBuilder().setUserId(1).setLevel(AccessLevel.ADMIN).build())
                .build();
        ActionResponse response = accessControlStub.unlockDoor(request);
        return "Access control response: " + response.toString();
    }


    public static void main(String[] args) throws Exception {
        GrpcClient client = new GrpcClient("localhost", 8080);
        try {
            client.accessControl();
        } finally {
            client.shutdown();
        }
    }
}