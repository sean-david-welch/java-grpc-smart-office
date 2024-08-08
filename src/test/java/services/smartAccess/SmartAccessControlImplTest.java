package services.smartAccess;

import io.grpc.Context;
import io.grpc.Metadata;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.constants.Constants;
import services.utils.JwtUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.*;

public class SmartAccessControlImplTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private StreamObserver<ActionResponse> actionResponseObserver;

    @Mock
    private StreamObserver<AccessLogsResponse> accessLogsResponseObserver;

    @InjectMocks
    private SmartAccessControlImpl smartAccessControl;

    @BeforeEach
    public void setup() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    private void createAuthMetadata() {
        String jwt = JwtUtility.generateToken("testClientId");
        Metadata metadata = new Metadata();
        metadata.put(Constants.AUTHORIZATION_METADATA_KEY, Constants.BEARER_TYPE + " " + jwt);
    }

    @Test
    public void testUnlockDoorSuccess() throws SQLException {
        createAuthMetadata();

        Context ctx = Context.current().withValue(Constants.CLIENT_ID_CONTEXT_KEY, "testClientId");
        Context previous = ctx.attach();

        UnlockDoorRequest request = UnlockDoorRequest.newBuilder()
                .setDoorId(1)
                .setCredentials(AccessCredentials.newBuilder().setUserId(1).setLevel(AccessLevel.ADMIN).build())
                .build();

        when(mockResultSet.next()).thenReturn(true, true);
        when(mockResultSet.getString("status")).thenReturn("locked");
        when(mockResultSet.getString("access_level")).thenReturn("ADMIN");
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartAccessControl.unlockDoor(request, actionResponseObserver);

        verify(actionResponseObserver).onNext(argThat(response -> response.getSuccess() && response.getErrorCode() == ErrorCode.NONE));
        verify(actionResponseObserver).onCompleted();

        ctx.detach(previous);
    }

    @Test
    public void testUnlockDoorAccessDenied() throws SQLException {
        createAuthMetadata();

        Context ctx = Context.current().withValue(Constants.CLIENT_ID_CONTEXT_KEY, "testClientId");
        Context previous = ctx.attach();

        UnlockDoorRequest request = UnlockDoorRequest.newBuilder()
                .setDoorId(1)
                .setCredentials(AccessCredentials.newBuilder().setUserId(1).setLevel(AccessLevel.GENERAL).build())
                .build();

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("status")).thenReturn("locked");
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartAccessControl.unlockDoor(request, actionResponseObserver);

        verify(actionResponseObserver).onNext(argThat(response -> !response.getSuccess() && response.getErrorCode() == ErrorCode.ACCESS_DENIED));
        verify(actionResponseObserver).onCompleted();

        ctx.detach(previous);
    }

    @Test
    public void testRaiseAlarmSuccess() throws SQLException {
        RaiseAlarmRequest request = RaiseAlarmRequest.newBuilder()
                .setDoorId(1)
                .setCredentials(AccessCredentials.newBuilder().setUserId(1).setLevel(AccessLevel.ADMIN).build())
                .build();

        when(mockResultSet.next()).thenReturn(true, true);
        when(mockResultSet.getString("status")).thenReturn("locked");
        when(mockResultSet.getString("access_level")).thenReturn("ADMIN");
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartAccessControl.raiseAlarm(request, actionResponseObserver);

        verify(actionResponseObserver).onNext(argThat(response -> response.getSuccess() && response.getErrorCode() == ErrorCode.NONE));
        verify(actionResponseObserver).onCompleted();
    }


    @Test
    public void testRaiseAlarmAccessDenied() throws SQLException {
        RaiseAlarmRequest request = RaiseAlarmRequest.newBuilder()
                .setDoorId(1)
                .setCredentials(AccessCredentials.newBuilder().setUserId(1).setLevel(AccessLevel.GENERAL).build())
                .build();

        when(mockResultSet.next()).thenReturn(true);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartAccessControl.raiseAlarm(request, actionResponseObserver);

        verify(actionResponseObserver).onNext(argThat(response -> !response.getSuccess() && response.getErrorCode() == ErrorCode.SYSTEM_ERROR));
        verify(actionResponseObserver).onCompleted();
    }

    @Test
    public void testGetAccessLogsSuccess() throws SQLException {
        GetAccessLogsRequest request = GetAccessLogsRequest.newBuilder()
                .setDoorId(1)
                .setStartTime("09:00")
                .setEndTime("10:00")
                .build();

        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("user_id")).thenReturn(1);
        when(mockResultSet.getInt("door_id")).thenReturn(1);
        when(mockResultSet.getString("access_time")).thenReturn("09:00");
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        StreamObserver<GetAccessLogsRequest> requestObserver = smartAccessControl.getAccessLogs(accessLogsResponseObserver);

        requestObserver.onNext(request);
        requestObserver.onCompleted();

        verify(accessLogsResponseObserver).onNext(argThat(response -> {
            List<LogEntry> logs = response.getLogsList();
            return !logs.isEmpty() && response.getErrorCode() == ErrorCode.NONE && !response.getEndOfStream();
        }));
        verify(accessLogsResponseObserver).onCompleted();
    }

    @Test
    public void testGetAccessLogsNoLogs() throws SQLException {
        GetAccessLogsRequest request = GetAccessLogsRequest.newBuilder()
                .setDoorId(1)
                .setStartTime("09:00")
                .setEndTime("10:00")
                .build();

        when(mockResultSet.next()).thenReturn(false);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        StreamObserver<GetAccessLogsRequest> requestObserver = smartAccessControl.getAccessLogs(accessLogsResponseObserver);

        requestObserver.onNext(request);
        requestObserver.onCompleted();

        verify(accessLogsResponseObserver).onNext(argThat(response -> response.getLogsList().isEmpty() && response.getErrorCode() == ErrorCode.NONE && response.getEndOfStream()));
        verify(accessLogsResponseObserver).onCompleted();
    }
}
