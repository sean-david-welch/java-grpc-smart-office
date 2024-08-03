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
        boolean alarmRaised = raiseAlarmLogic(request);

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

    private boolean unlockDoorInternal(UnlockDoorRequest request) {
        String doorID = request.getDoorId();
        String userID = request.getUserId();
        AccessLevel accessLevel = request.getCredentials().getLevel();

        String doorQuery = "select id from door where id = ?";
        String credentialsQuery = "select accessLevel from accessCredentials where accessLevel = ? and badgeid = ?";

        try (PreparedStatement doorStmt = conn.prepareStatement(doorQuery)) {
            doorStmt.setString(1, doorID);
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
                credentialsStmt.setString(1, userID);
                credentialsStmt.setString(2, accessLevel.toString());
                ResultSet credResult = credentialsStmt.executeQuery();

                if (!credResult.next()) {
                    System.out.println("No matching access credentials for badgeID: " + userID);
                    return false;
                }

                String userAccessLevel = credResult.getString("accessLevel");
                if (!userAccessLevel.equals(accessLevel.toString())) {
                    System.out.println("Access level mismatch: required " + accessLevel + ", found " + userAccessLevel);
                    return false;
                }
                System.out.println("Door unlocked: " + doorID);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error querying door status: " + e.getMessage());
            return false;
        }
    }

    private boolean raiseAlarmLogic(RaiseAlarmRequest request) {
        String doorID = request.getDoorId();

        return true;
    }

    private List<LogEntry> retrieveAccessLogs(GetAccessLogsRequest request) {
        return new ArrayList<>();
    }
}