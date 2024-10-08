package client.controllers;

import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import services.smartAccess.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AccessControlClientController {
    private final SmartAccessControlGrpc.SmartAccessControlBlockingStub accessControlStub;
    private final SmartAccessControlGrpc.SmartAccessControlStub asyncAccessControlStub;

    public AccessControlClientController(SmartAccessControlGrpc.SmartAccessControlBlockingStub accessControlStub,
                                         SmartAccessControlGrpc.SmartAccessControlStub asyncAccessControlStub) {
        this.accessControlStub = accessControlStub;
        this.asyncAccessControlStub = asyncAccessControlStub;
    }

    public String unlockDoor(int doorId, int userId, AccessLevel level) {
        try {
            UnlockDoorRequest request = UnlockDoorRequest.newBuilder()
                    .setDoorId(doorId)
                    .setCredentials(AccessCredentials.newBuilder().setUserId(userId).setLevel(level).build())
                    .build();
            ActionResponse response = accessControlStub.unlockDoor(request);
            return "Unlock Door Response: " + response.toString();
        } catch (StatusRuntimeException e) {
            return "Error: " + e.getStatus().getDescription();
        }
    }

    public String raiseAlarm(int doorId, int userId, AccessLevel level) {
        try {
            RaiseAlarmRequest request = RaiseAlarmRequest.newBuilder()
                    .setDoorId(doorId)
                    .setCredentials(AccessCredentials.newBuilder().setUserId(userId).setLevel(level).build())
                    .build();
            ActionResponse response = accessControlStub.raiseAlarm(request);
            return "Raise Alarm Response: " + response.toString();
        } catch (StatusRuntimeException e) {
            return "Error: " + e.getStatus().getDescription();
        }
    }

    public List<AccessLogsResponse> getAccessLogs(int doorId, String startTime, String endTime) {
        List<AccessLogsResponse> logsResponses = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<GetAccessLogsRequest> requestObserver = asyncAccessControlStub.getAccessLogs(new StreamObserver<>() {
            @Override
            public void onNext(AccessLogsResponse response) {
                logsResponses.add(response);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error retrieving access logs: " + t.getMessage());
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Completed receiving access logs.");
                latch.countDown();
            }
        });

        try {
            GetAccessLogsRequest request = GetAccessLogsRequest.newBuilder()
                    .setDoorId(doorId)
                    .setStartTime(startTime)
                    .setEndTime(endTime)
                    .build();

            requestObserver.onNext(request);
            requestObserver.onCompleted();

            latch.await(5, TimeUnit.SECONDS);

            return logsResponses;
        } catch (Exception e) {
            requestObserver.onError(e);
            return logsResponses;
        }
    }
}
