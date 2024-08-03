package services.smartAccess;

import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class SmartAccessControlImpl extends SmartAccessControlGrpc.SmartAccessControlImplBase {

    @Override
    public void unlockDoor(UnlockDoorRequest request, StreamObserver<ActionResponse> responseObserver) {
        boolean unlocked = unlockDoorLogic(request);

        ActionResponse response;
        if (unlocked) {
            response = ActionResponse.newBuilder()
                    .setSuccess(true)
                    .setErrorCode(ErrorCode.NONE)
                    .build();
        } else {
            response = ActionResponse.newBuilder()
                    .setSuccess(false)
                    .setErrorCode(ErrorCode.ACCESS_DENIED)
                    .build();
        }

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

    private boolean unlockDoorLogic(UnlockDoorRequest request) {
        return true;
    }

    private boolean raiseAlarmLogic(RaiseAlarmRequest request) {
        return true;
    }

    private List<LogEntry> retrieveAccessLogs(GetAccessLogsRequest request) {
        return new ArrayList<>();
    }
}