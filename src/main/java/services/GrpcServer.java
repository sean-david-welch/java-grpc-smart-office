package services;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import services.database.Database;
import services.serviceRegister.ServiceRegister;
import services.smartAccess.SmartAccessControlImpl;
import services.smartCoffee.SmartCoffeeMachineImpl;
import services.smartMeeting.SmartMeetingRoomImpl;

import java.sql.Connection;

public class GrpcServer {
    public static void main(String[] args) {
        Database.initializeDatabase();
        try (Connection conn = Database.getConnection()) {
            if (conn != null) {
                Server server = ServerBuilder.forPort(8080)
                        .addService(new SmartAccessControlImpl(conn))
                        .addService(new SmartCoffeeMachineImpl(conn))
                        .addService(new SmartMeetingRoomImpl(conn))
                        .build();

                ServiceRegister serviceRegister = new ServiceRegister();
                serviceRegister.registerService("SmartMeetingService");
                serviceRegister.registerService("SmartCoffeeService");
                serviceRegister.registerService("SmartAccessService");

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    System.err.println("*** shutting down gRPC server since JVM is shutting down");
                    server.shutdown();
                    serviceRegister.unregisterService();
                    System.err.println("*** server shut down");
                }));

                server.start();
                System.out.println("Server started on port 8080");
                server.awaitTermination();
            }
        } catch (Exception e) {
            System.out.println("Failed to start server: " + e.getMessage());
        }
    }
}
