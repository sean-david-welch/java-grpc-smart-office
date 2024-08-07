package services.smartAccess;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    @Test
    public void testUnlockDoorSuccess() throws SQLException {
        UnlockDoorRequest request = UnlockDoorRequest.newBuilder()
                .setDoorId(1)
                .setUserId(1)
                .setCredentials(AccessCredentials.newBuilder().setLevel(AccessLevel.ADMIN))
                .build();

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("status")).thenReturn("locked");
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartAccessControl.unlockDoor(request, actionResponseObserver);

        verify(actionResponseObserver).onNext(any(ActionResponse.class));
        verify(actionResponseObserver).onCompleted();
    }

    @Test
    public void testUnlockDoorAccessDenied() throws SQLException {
        UnlockDoorRequest request = UnlockDoorRequest.newBuilder()
                .setDoorId(1)
                .setUserId(1)
                .setCredentials(AccessCredentials.newBuilder().setLevel(AccessLevel.GENERAL))
                .build();

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("status")).thenReturn("locked");
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartAccessControl.unlockDoor(request, actionResponseObserver);

        verify(actionResponseObserver).onNext(argThat(response -> !response.getSuccess()));
        verify(actionResponseObserver).onCompleted();
    }

    @Test
    public void testRaiseAlarmSuccess() throws SQLException {
        RaiseAlarmRequest request = RaiseAlarmRequest.newBuilder()
                .setDoorId(1)
                .setUserId(1)
                .setCredentials(AccessCredentials.newBuilder().setLevel(AccessLevel.ADMIN))
                .build();

        when(mockResultSet.next()).thenReturn(true);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartAccessControl.raiseAlarm(request, actionResponseObserver);

        verify(actionResponseObserver).onNext(any(ActionResponse.class));
        verify(actionResponseObserver).onCompleted();
    }

    @Test
    public void testRaiseAlarmAccessDenied() throws SQLException {
        RaiseAlarmRequest request = RaiseAlarmRequest.newBuilder()
                .setDoorId(1)
                .setUserId(1)
                .setCredentials(AccessCredentials.newBuilder().setLevel(AccessLevel.GENERAL))
                .build();

        when(mockResultSet.next()).thenReturn(false);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartAccessControl.raiseAlarm(request, actionResponseObserver);

        verify(actionResponseObserver).onNext(argThat(response -> !response.getSuccess()));
        verify(actionResponseObserver).onCompleted();
    }

    @Test
    public void testGetAccessLogsSuccess() throws SQLException {
        GetAccessLogsRequest request = GetAccessLogsRequest.newBuilder()
                .setDoorId(1)
                .setUserId(1)
                .setTime("2023-08-05")
                .build();

        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("user_id")).thenReturn(1);
        when(mockResultSet.getInt("door_id")).thenReturn(1);
        when(mockResultSet.getString("access_time")).thenReturn("2023-08-05");
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        ArgumentCaptor<StreamObserver<AccessLogsResponse>> responseObserverCaptor = ArgumentCaptor.forClass((Class) StreamObserver.class);
        StreamObserver<GetAccessLogsRequest> requestObserver = smartAccessControl.getAccessLogs(accessLogsResponseObserver);

        requestObserver.onNext(request);
        requestObserver.onCompleted();

        verify(accessLogsResponseObserver).onNext(any(AccessLogsResponse.class));
        verify(accessLogsResponseObserver).onCompleted();
    }


    @Test
    public void testGetAccessLogsNoLogs() throws SQLException {
        GetAccessLogsRequest request = GetAccessLogsRequest.newBuilder()
                .setDoorId(1)
                .setUserId(1)
                .setTime("2023-08-05")
                .build();

        // Set up the mocks to return no results
        when(mockResultSet.next()).thenReturn(false);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Capture the response observer
        ArgumentCaptor<StreamObserver<AccessLogsResponse>> responseObserverCaptor = ArgumentCaptor.forClass((Class) StreamObserver.class);
        doNothing().when(accessLogsResponseObserver).onNext(any(AccessLogsResponse.class));
        doNothing().when(accessLogsResponseObserver).onCompleted();

        // Call the method and get the request observer
        StreamObserver<GetAccessLogsRequest> requestObserver = smartAccessControl.getAccessLogs(accessLogsResponseObserver);

        // Send the request
        requestObserver.onNext(request);
        requestObserver.onCompleted();

        // Verify interactions with the response observer
        verify(accessLogsResponseObserver).onNext(any(AccessLogsResponse.class));
        verify(accessLogsResponseObserver).onCompleted();
    }

}
