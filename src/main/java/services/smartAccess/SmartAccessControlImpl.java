package services.smartAccess;

import io.grpc.stub.StreamObserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SmartAccessControlImpl extends SmartAccessControlGrpc.SmartAccessControlImplBase {

    private final Connection conn;

    public SmartAccessControlImpl(Connection conn) {
        this.conn = conn;
    }

    // <---------- External Grpc methods -------->
    // Simple RPC
    @Override
    public void unlockDoor(UnlockDoorRequest request, StreamObserver<ActionResponse> responseObserver) {
        boolean unlocked = unlockDoorInternal(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(unlocked)
                .setErrorCode(unlocked ? ErrorCode.NONE : ErrorCode.ACCESS_DENIED)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // Simple RPC
    @Override
    public void raiseAlarm(RaiseAlarmRequest request, StreamObserver<ActionResponse> responseObserver) {
        boolean alarmRaised = raiseAlarmInternal(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(alarmRaised)
                .setErrorCode(alarmRaised ? ErrorCode.NONE : ErrorCode.SYSTEM_ERROR)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // Bidirectional Streaming
    @Override
    public StreamObserver<GetAccessLogsRequest> getAccessLogs(StreamObserver<AccessLogsResponse> responseObserver) {
        return new StreamObserver<>() {

            @Override
            public void onNext(GetAccessLogsRequest request) {
                List<LogEntry> logs = retrieveAccessLogs(request);

                AccessLogsResponse response = AccessLogsResponse.newBuilder()
                        .addAllLogs(logs)
                        .setErrorCode(ErrorCode.NONE)
                        .setEndOfStream(false)
                        .build();

                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error receiving GetAccessLogsRequest: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                AccessLogsResponse endOfStreamResponse = AccessLogsResponse.newBuilder()
                        .setEndOfStream(true)
                        .build();
                responseObserver.onNext(endOfStreamResponse);
                responseObserver.onCompleted();
            }
        };
    }


    // <--------  Interal Logic Methods -------->
    private boolean unlockDoorInternal(UnlockDoorRequest request) {
        int doorID = request.getDoorId();
        int userID = request.getCredentials().getUserId();
        AccessLevel accessLevel = request.getCredentials().getLevel();

        String doorQuery = "SELECT status FROM door WHERE id = ?";
        String credentialsQuery = "SELECT access_level FROM access_credentials WHERE user_id = ? AND access_level = ?";

        try (PreparedStatement doorStmt = conn.prepareStatement(doorQuery)) {
            doorStmt.setInt(1, doorID);
            ResultSet doorResult = doorStmt.executeQuery();

            if (!doorResult.next()) {
                System.out.println("Door ID not found: " + doorID);
                return false;
            }

            String doorStatus = doorResult.getString("status");
            if (!"locked".equals(doorStatus)) {
                System.out.println("Door is not locked: " + doorID);
                return false;
            }

            try (PreparedStatement credentialsStmt = conn.prepareStatement(credentialsQuery)) {
                credentialsStmt.setInt(1, userID);
                credentialsStmt.setString(2, accessLevel.toString().toLowerCase());
                ResultSet credResult = credentialsStmt.executeQuery();

                if (!credResult.next()) {
                    System.out.println("No matching access credentials for userID: " + userID);
                    return false;
                }

                String userAccessLevel = credResult.getString("access_level").toUpperCase();

                if (!userAccessLevel.equals(accessLevel.toString())) {
                    System.out.println("Access level mismatch: required " + accessLevel + ", found " + userAccessLevel);
                    return false;
                }

                System.out.println("Door unlocked: " + doorID);
                updateDoorStatus(doorID);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error querying door status: " + e.getMessage());
            return false;
        }
    }

    private boolean raiseAlarmInternal(RaiseAlarmRequest request) {
        int doorID = request.getDoorId();
        int userID = request.getCredentials().getUserId();
        AccessLevel accessLevel = request.getCredentials().getLevel();

        String doorQuery = "select status from door where id = ?";
        String credentialsQuery = "select access_level from access_credentials WHERE user_id = ?";

        try (PreparedStatement doorStmt = conn.prepareStatement(doorQuery)) {
            doorStmt.setInt(1, doorID);
            ResultSet doorResult = doorStmt.executeQuery();

            if (!doorResult.next()) {
                System.out.println("No door found with ID: " + doorID);
                return false;
            }

            try (PreparedStatement credentialsStmt = conn.prepareStatement(credentialsQuery)) {
                credentialsStmt.setInt(1, userID);
                ResultSet credResult = credentialsStmt.executeQuery();

                if (!credResult.next()) {
                    System.out.println("Invalid PIN or insufficient access level for raising an alarm.");
                    return false;
                }

                String userAccessLevel = credResult.getString("access_level").toUpperCase();

                if (!userAccessLevel.equals(accessLevel.toString())) {
                    System.out.println("Access level mismatch: required " + accessLevel + ", found " + userAccessLevel);
                    return false;
                }

                System.out.println("Alarm raised for door: " + doorID);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error querying database: " + e.getMessage());
            return false;
        }
    }

    private List<LogEntry> retrieveAccessLogs(GetAccessLogsRequest request) {
        List<LogEntry> logs = new ArrayList<>();

        int doorID = request.getDoorId();
        String startTime = request.getStartTime();
        String endTime = request.getEndTime();

        String query = "SELECT * FROM access_log WHERE door_id = ? AND access_time BETWEEN ? AND ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, doorID);
            stmt.setString(2, startTime);
            stmt.setString(3, endTime);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LogEntry logEntry = LogEntry.newBuilder()
                        .setUserId(rs.getInt("user_id"))
                        .setDoorId(rs.getInt("door_id"))
                        .setAccessTime(rs.getString("access_time"))
                        .build();
                logs.add(logEntry);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving access logs: " + e.getMessage());
        }

        return logs;
    }

    private void updateDoorStatus(int doorID) {
        String updateDoorStatusQuery = "update door set status = ? WHERE id = ?";

        try (PreparedStatement updateStmt = conn.prepareStatement(updateDoorStatusQuery)) {
            updateStmt.setString(1, "unlocked");
            updateStmt.setInt(2, doorID);
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating door status: " + e.getMessage());
        }
    }
}