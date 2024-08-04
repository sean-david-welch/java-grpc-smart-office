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
        String doorID = request.getDoorId();
        String userID = request.getUserId();
        AccessLevel accessLevel = request.getCredentials().getLevel();

        String doorQuery = "select id from door where id = ?";
        String credentialsQuery = "select accessLevel from accessCredentials where accessLevel = ? and badgeid = ?";

            try (PreparedStatement doorStmt = conn.prepareStatement(doorQuery)) {
            doorStmt.setString(1, doorID);
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
                credentialsStmt.setString(1, userID);
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
        String doorID = request.getDoorId();
        String pin = request.getCredentials().getPin();
        AccessLevel accessLevel = request.getCredentials().getLevel();

        String doorQuery = "SELECT status FROM door WHERE id = ?";
        String credentialsQuery = "SELECT accessLevel FROM accessCredentials WHERE badgeid = ? AND pin = ?";

        try (PreparedStatement doorStmt = conn.prepareStatement(doorQuery)) {
            doorStmt.setString(1, doorID);
            ResultSet doorResult = doorStmt.executeQuery();

            if (!doorResult.next()) {
                System.out.println("No door found with ID: " + doorID);
                return false;
            }

            try (PreparedStatement credentialsStmt = conn.prepareStatement(credentialsQuery)) {
                credentialsStmt.setString(1, doorID);
                credentialsStmt.setString(2, pin);
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
        String query = "SELECT * FROM logEntry WHERE doorId = ? AND accesstime = ?";

        String doorID = request.getDoorId();
        String startTime = request.getStartTime().toString();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, doorID);
            stmt.setString(2, startTime);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String accessTimeStr = rs.getString("accessTime");
                Timestamp protoTimestamp = Timestamp.newBuilder().build();

                LogEntry logEntry = LogEntry.newBuilder()
                        .setUserId(rs.getString("doorId"))
                        .setAccessTime(protoTimestamp)
                        .build();
                logs.add(logEntry);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving access logs: " + e.getMessage());
        }

        return logs;
    }

    private void updateDoorStatus(String doorID, String status) {
        String updateDoorStatusQuery = "UPDATE door SET status = ? WHERE id = ?";

        try (PreparedStatement updateStmt = conn.prepareStatement(updateDoorStatusQuery)) {
            updateStmt.setString(1, status);
            updateStmt.setString(2, doorID);
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating door status: " + e.getMessage());
        }
    }
}