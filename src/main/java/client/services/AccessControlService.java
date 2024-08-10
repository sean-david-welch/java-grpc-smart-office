package client.services;

import io.grpc.StatusRuntimeException;
import services.smartAccess.AccessCredentials;
import services.smartAccess.AccessLevel;
import services.smartAccess.SmartAccessControlGrpc;
import services.smartAccess.UnlockDoorRequest;

public class AccessControlService {
    private final SmartAccessControlGrpc.SmartAccessControlBlockingStub accessControlStub;

    public AccessControlService(SmartAccessControlGrpc.SmartAccessControlBlockingStub accessControlStub) {
        this.accessControlStub = accessControlStub;
    }

    public String unlockDoor(int userId, AccessLevel level) {
        try {
            UnlockDoorRequest request = UnlockDoorRequest.newBuilder()
                    .setDoorId(1)
                    .setCredentials(AccessCredentials.newBuilder().setUserId(userId).setLevel(level).build())
                    .build();
            services.smartAccess.ActionResponse response = accessControlStub.unlockDoor(request);
            return "Access control response: " + response.toString();
        } catch (StatusRuntimeException e) {
            return "Error: " + e.getStatus().getDescription();
        }
    }
}
