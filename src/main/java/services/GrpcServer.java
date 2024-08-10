package services;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import services.database.Database;
import services.serviceRegister.ServiceRegister;
import services.smartAccess.SmartAccessControlImpl;
import services.smartCoffee.SmartCoffeeMachineImpl;
import services.smartMeeting.SmartMeetingRoomImpl;
import services.utils.JwtServerInterceptor;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrpcServer {

    private static final Logger logger = Logger.getLogger(GrpcServer.class.getName());

    public static void main(String[] args) {
        Database.initializeDatabase();
        try (Connection conn = Database.getConnection()) {
            if (conn != null) {
                Server server = ServerBuilder.forPort(8080)
                        .addService(new SmartAccessControlImpl(conn))
                        .addService(new SmartCoffeeMachineImpl(conn))
                        .addService(new SmartMeetingRoomImpl(conn))
                        .intercept(new JwtServerInterceptor())
                        .build();

                ServiceRegister serviceRegister = new ServiceRegister();
                serviceRegister.registerService("_smartMeeting._tcp.local.", "SmartMeetingService", 8080);
                serviceRegister.registerService("_smartCoffee._tcp.local.", "SmartCoffeeService", 8080);
                serviceRegister.registerService("_smartAccess._tcp.local.", "SmartAccessService", 8080);

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    logger.log(Level.INFO, "*** Shutting down gRPC server since JVM is shutting down");
                    server.shutdown();
                    serviceRegister.unregisterService();
                    logger.log(Level.INFO, "*** Server shut down");
                }));

                server.start();
                logger.log(Level.INFO, "Server started on port 8080");
                server.awaitTermination();
            } else {
                logger.log(Level.SEVERE, "Failed to obtain database connection");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to start server: {0}", e.getMessage());
        }
    }
}
