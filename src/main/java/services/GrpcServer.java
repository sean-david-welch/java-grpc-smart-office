package services;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import services.database.Database;
import services.smartAccess.SmartAccessControlImpl;
import services.smartCoffee.SmartCoffeeMachineImpl;
import services.smartMeeting.SmartMeetingRoomImpl;

import java.sql.Connection;

public class GrpcServer {
    public static void main(String[] args) throws Exception {
        Database.initializeDatabase();
        try (Connection conn = Database.getConnection()) {
            if (conn != null) {
                Server server = ServerBuilder.forPort(8080)
                        .addService(new SmartAccessControlImpl(conn))
                        .addService(new SmartCoffeeMachineImpl(conn))
                        .addService(new SmartMeetingRoomImpl(conn))
                        .build();

                server.start();
                System.out.println("Server started on port 8080");
                server.awaitTermination();
            }
        } catch (Exception e) {
            System.out.println("Failed to start server: " + e.getMessage());
        }
    }
}
