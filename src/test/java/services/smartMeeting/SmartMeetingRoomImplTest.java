package services.smartMeeting;

import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SmartMeetingRoomImplTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private StreamObserver<ActionResponse> actionResponseObserver;

    @Mock
    private StreamObserver<AvailabilityResponse> availabilityResponseObserver;

    @InjectMocks
    private SmartMeetingRoomImpl smartMeetingRoom;

    @BeforeEach
    public void setup() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testBookRoomSuccess() throws SQLException {
        BookRoomRequest request = BookRoomRequest.newBuilder()
                .setRoomId(1)
                .setUserId(1)
                .setTimeSlot("10:00-11:00")
                .build();

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("status")).thenReturn("AVAILABLE");
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartMeetingRoom.bookRoom(request, actionResponseObserver);

        verify(actionResponseObserver).onNext(any(ActionResponse.class));
        verify(actionResponseObserver).onCompleted();
    }

    @Test
    public void testBookRoomUnavailable() throws SQLException {
        BookRoomRequest request = BookRoomRequest.newBuilder()
                .setRoomId(1)
                .setUserId(1)
                .setTimeSlot("10:00-11:00")
                .build();

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("status")).thenReturn("OCCUPIED");
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartMeetingRoom.bookRoom(request, actionResponseObserver);

        verify(actionResponseObserver).onNext(argThat(response -> !response.getSuccess()));
        verify(actionResponseObserver).onCompleted();
    }

    @Test
    public void testCancelBookingSuccess() throws SQLException {
        CancelBookingRequest request = CancelBookingRequest.newBuilder()
                .setBookingId(1)
                .build();

        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        smartMeetingRoom.cancelBooking(request, actionResponseObserver);

        verify(actionResponseObserver).onNext(any(ActionResponse.class));
        verify(actionResponseObserver).onCompleted();
    }

    @Test
    public void testCancelBookingFailure() throws SQLException {
        CancelBookingRequest request = CancelBookingRequest.newBuilder()
                .setBookingId(1)
                .build();

        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        smartMeetingRoom.cancelBooking(request, actionResponseObserver);

        verify(actionResponseObserver).onNext(argThat(response -> !response.getSuccess()));
        verify(actionResponseObserver).onCompleted();
    }

    @Test
    public void testCheckAvailabilitySuccess() throws SQLException, JsonProcessingException {
        CheckAvailabilityRequest request = CheckAvailabilityRequest.newBuilder()
                .setRoomId(1)
                .setTimeSlot("10:00-11:00")
                .build();

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("status")).thenReturn("AVAILABLE");
        when(mockResultSet.getString("available_time")).thenReturn("[\"10:00-11:00\", \"12:00-13:00\"]");
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartMeetingRoom.checkAvailability(request, availabilityResponseObserver);

        verify(availabilityResponseObserver).onNext(any(AvailabilityResponse.class));
        verify(availabilityResponseObserver).onCompleted();
    }

    @Test
    public void testCheckAvailabilityFailure() throws SQLException {
        CheckAvailabilityRequest request = CheckAvailabilityRequest.newBuilder()
                .setRoomId(1)
                .setTimeSlot("10:00-11:00")
                .build();

        when(mockResultSet.next()).thenReturn(false);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartMeetingRoom.checkAvailability(request, availabilityResponseObserver);

        verify(availabilityResponseObserver).onNext(argThat(response -> !response.getSuccess()));
        verify(availabilityResponseObserver).onCompleted();
    }
}
