package services;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import services.smartAccess.SmartAccessControlImpl;
import services.smartCoffee.SmartCoffeeMachineImpl;
import services.smartMeeting.SmartMeetingRoomImpl;

public class GrpcServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(8080)
                .addService(new SmartAccessControlImpl())
                .addService(new SmartCoffeeMachineImpl())
                .addService(new SmartMeetingRoomImpl())
                .build();

        server.start();
        System.out.println("Server started on port 8080");
        server.awaitTermination();
    }
}
