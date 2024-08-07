package client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import services.smartAccess.ActionResponse;
import services.smartAccess.SmartAccessControlGrpc;
import services.smartAccess.UnlockDoorRequest;
import services.smartCoffee.SmartCoffeeMachineGrpc;
import services.smartMeeting.SmartMeetingRoomGrpc;

import java.util.concurrent.TimeUnit;

public class GrpcClient {
    private final ManagedChannel channel;
    private final SmartAccessControlGrpc.SmartAccessControlBlockingStub accessControlStub;

    public GrpcClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        accessControlStub = SmartAccessControlGrpc.newBlockingStub(channel);
        SmartAccessControlGrpc.SmartAccessControlBlockingStub accessControlStub = SmartAccessControlGrpc.newBlockingStub(channel);
        SmartCoffeeMachineGrpc.SmartCoffeeMachineBlockingStub coffeeMachineStub = SmartCoffeeMachineGrpc.newBlockingStub(channel);
        SmartMeetingRoomGrpc.SmartMeetingRoomBlockingStub meetingRoomStub = SmartMeetingRoomGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void accessControl() {
        UnlockDoorRequest request = UnlockDoorRequest.newBuilder()
                .build();
        ActionResponse response = accessControlStub.unlockDoor(request);
        System.out.println("Access control response: " + response.toString());
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