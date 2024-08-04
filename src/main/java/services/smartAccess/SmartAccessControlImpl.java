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

    @Override
    public void getAccessLogs(GetAccessLogsRequest request, StreamObserver<AccessLogsResponse> responseObserver) {
        List<LogEntry> logs = retrieveAccessLogs(request);

        AccessLogsResponse response = AccessLogsResponse.newBuilder()
                .addAllLogs(logs)
                .setErrorCode(ErrorCode.NONE)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // <--------  Interal Logic Methods -------->
    private boolean unlockDoorInternal(UnlockDoorRequest request) {
        int doorID = request.getDoorId();
        int userID = request.getUserId();
        AccessLevel accessLevel = request.getCredentials().getLevel();

        String doorQuery = "select id from door where id = ?";
        String credentialsQuery = "select access_level from access_credentials where user_id = ? and access_level = ?";

            try (PreparedStatement doorStmt = conn.prepareStatement(doorQuery)) {
            doorStmt.setInt(1, doorID);
            updateDoorStatus(doorID, "locked");

            ResultSet doorResult = doorStmt.executeQuery();

            if (!doorResult.next()) {
                System.out.println("Door ID not found: " + doorID);
                updateDoorStatus(doorID, "locked");
                return false;
            }

            String doorStatus = doorResult.getString("status");
            if (!"locked".equals(doorStatus)) {
                System.out.println("Door is not locked: " + doorID);
                updateDoorStatus(doorID, "locked");
                return false;
            }

            try (PreparedStatement credentialsStmt = conn.prepareStatement(credentialsQuery)) {
                credentialsStmt.setInt(1, userID);
                credentialsStmt.setString(2, accessLevel.toString());
                ResultSet credResult = credentialsStmt.executeQuery();

                if (!credResult.next()) {
                    System.out.println("No matching access credentials for badgeID: " + userID);
                    updateDoorStatus(doorID, "locked");
                    return false;
                }

                String userAccessLevel = credResult.getString("accessLevel");
                if (!userAccessLevel.equals(accessLevel.toString())) {
                    System.out.println("Access level mismatch: required " + accessLevel + ", found " + userAccessLevel);
                    updateDoorStatus(doorID, "locked");
                    return false;
                }
                System.out.println("Door unlocked: " + doorID);
                updateDoorStatus(doorID, "unlocked");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error querying door status: " + e.getMessage());
            return false;
        }
    }

    private boolean raiseAlarmInternal(RaiseAlarmRequest request) {
        int doorID = request.getDoorId();
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
                // change proto to use user id
                credentialsStmt.setInt(1, doorID);
                ResultSet credResult = credentialsStmt.executeQuery();

                if (!credResult.next()) {
                    System.out.println("Invalid PIN or insufficient access level for raising an alarm.");
                    return false;
                }

                String userAccessLevel = credResult.getString("accessLevel");
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
        String time = request.getTime();

        String query = "select * from access_log where door_id = ? AND access_time = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, doorID);
            stmt.setString(2, time);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                // perhaps change proto to use door id here too
                LogEntry logEntry = LogEntry.newBuilder()
                        .setUserId(rs.getInt("user_id"))
                        .setAccessTime(rs.getString("access_time"))
                        .build();
                logs.add(logEntry);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving access logs: " + e.getMessage());
        }

        return logs;
    }

    private void updateDoorStatus(int doorID, String status) {
        String updateDoorStatusQuery = "update door set status = ? WHERE id = ?";

        try (PreparedStatement updateStmt = conn.prepareStatement(updateDoorStatusQuery)) {
            updateStmt.setString(1, status);
            updateStmt.setInt(2, doorID);
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating door status: " + e.getMessage());
        }
    }
}