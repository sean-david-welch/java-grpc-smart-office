package services.smartAccess;

import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import services.constants.Constants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SmartAccessControlImpl extends SmartAccessControlGrpc.SmartAccessControlImplBase {

    private final Connection conn;
    private static final System.Logger logger = System.getLogger(SmartAccessControlImpl.class.getName());

    public SmartAccessControlImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void unlockDoor(UnlockDoorRequest request, StreamObserver<ActionResponse> responseObserver) {
        String clientId = Constants.CLIENT_ID_CONTEXT_KEY.get(Context.current());
        if (clientId == null) {
            responseObserver.onError(Status.UNAUTHENTICATED.withDescription("Client not authenticated").asRuntimeException());
            return;
        }

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
        Context context = Context.current();
        if (context.getDeadline() != null && context.getDeadline().isExpired()) {
            responseObserver.onError(Status.DEADLINE_EXCEEDED.withDescription("Deadline exceeded").asRuntimeException());
            return;
        }

        boolean alarmRaised = raiseAlarmInternal(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(alarmRaised)
                .setErrorCode(alarmRaised ? ErrorCode.NONE : ErrorCode.SYSTEM_ERROR)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

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
                logger.log(System.Logger.Level.ERROR, "Error receiving GetAccessLogsRequest: {0}", t.getMessage());
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
                logger.log(System.Logger.Level.INFO, "Door ID not found: {0}", doorID);
                return false;
            }

            String doorStatus = doorResult.getString("status");
            if (!"locked".equals(doorStatus)) {
                logger.log(System.Logger.Level.INFO, "Door is not locked: {0}", doorID);
                return false;
            }

            try (PreparedStatement credentialsStmt = conn.prepareStatement(credentialsQuery)) {
                credentialsStmt.setInt(1, userID);
                credentialsStmt.setString(2, accessLevel.toString().toLowerCase());
                ResultSet credResult = credentialsStmt.executeQuery();

                if (!credResult.next()) {
                    logger.log(System.Logger.Level.INFO, "No matching access credentials for userID: {0}", userID);
                    return false;
                }

                String userAccessLevel = credResult.getString("access_level");
                if (userAccessLevel == null || !userAccessLevel.toUpperCase().equals(accessLevel.toString())) {
                    logger.log(System.Logger.Level.INFO, "Access level mismatch: required {0}, found {1}", new Object[]{accessLevel, userAccessLevel});
                    return false;
                }

                logger.log(System.Logger.Level.INFO, "Door unlocked: {0}", doorID);
                String updateDoorStatusQuery = "update door set status = ? WHERE id = ?";

                try (PreparedStatement updateStmt = conn.prepareStatement(updateDoorStatusQuery)) {
                    updateStmt.setString(1, "unlocked");
                    updateStmt.setInt(2, doorID);
                    updateStmt.executeUpdate();
                } catch (SQLException e) {
                    logger.log(System.Logger.Level.ERROR, "Error updating door status: {0}", e.getMessage());
                }
                return true;
            }
        } catch (SQLException e) {
            logger.log(System.Logger.Level.ERROR, "Error querying door status: {0}", e.getMessage());
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
                logger.log(System.Logger.Level.INFO, "No door found with ID: {0}", doorID);
                return false;
            }

            try (PreparedStatement credentialsStmt = conn.prepareStatement(credentialsQuery)) {
                credentialsStmt.setInt(1, userID);
                ResultSet credResult = credentialsStmt.executeQuery();

                if (!credResult.next()) {
                    logger.log(System.Logger.Level.INFO, "Invalid PIN or insufficient access level for raising an alarm.");
                    return false;
                }

                String userAccessLevel = credResult.getString("access_level");
                if (userAccessLevel == null || !userAccessLevel.toUpperCase().equals(accessLevel.toString())) {
                    logger.log(System.Logger.Level.INFO, "Access level mismatch: required {0}, found {1}", new Object[]{accessLevel, userAccessLevel});
                    return false;
                }

                logger.log(System.Logger.Level.INFO, "Alarm raised for door: {0}", doorID);
                return true;
            }
        } catch (SQLException e) {
            logger.log(System.Logger.Level.ERROR, "Error querying database: {0}", e.getMessage());
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
            logger.log(System.Logger.Level.ERROR, "Error retrieving access logs: {0}", e.getMessage());
        }

        return logs;
    }
}
