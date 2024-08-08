package services.smartMeeting;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static org.mockito.Mockito.*;

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
                .setTimeSlot("10:00")
                .build();

        ResultSet mockRoomResultSet = mock(ResultSet.class);
        when(mockRoomResultSet.next()).thenReturn(true);
        when(mockRoomResultSet.getString("status")).thenReturn("AVAILABLE");
        when(mockRoomResultSet.getString("available_times")).thenReturn("[\"09:00\", \"10:00\"]");

        PreparedStatement mockRoomStmt = mock(PreparedStatement.class);
        when(mockRoomStmt.executeQuery()).thenReturn(mockRoomResultSet);

        PreparedStatement mockBookingStmt = mock(PreparedStatement.class);
        when(mockBookingStmt.executeUpdate()).thenReturn(1);

        PreparedStatement mockUpdateStmt = mock(PreparedStatement.class);
        when(mockUpdateStmt.executeUpdate()).thenReturn(1);

        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement("SELECT status, location, available_times FROM room_details WHERE room_id = ?"))
                .thenReturn(mockRoomStmt);
        when(mockConn.prepareStatement("INSERT INTO booking (room_id, user_id, time_slot) VALUES (?, ?, ?)"))
                .thenReturn(mockBookingStmt);
        when(mockConn.prepareStatement("UPDATE room_details SET available_times = ? WHERE room_id = ?"))
                .thenReturn(mockUpdateStmt);

        SmartMeetingRoomImpl smartMeetingRoom = new SmartMeetingRoomImpl(mockConn);

        smartMeetingRoom.bookRoom(request, actionResponseObserver);

        verify(actionResponseObserver).onNext(any(ActionResponse.class));
        verify(actionResponseObserver).onCompleted();
    }

    @Test
    public void testBookRoomUnavailable() throws SQLException {
        BookRoomRequest request = BookRoomRequest.newBuilder()
                .setRoomId(4)
                .setUserId(101)
                .setTimeSlot("08:00")
                .build();

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("status")).thenReturn("OCCUPIED");
        when(mockResultSet.getString("location")).thenReturn("floor 5");
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


        ResultSet mockBookingResultSet = mock(ResultSet.class);
        when(mockBookingResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockBookingResultSet.getInt("room_id")).thenReturn(1);
        when(mockBookingResultSet.getString("time_slot")).thenReturn("09:00");

        ResultSet mockRoomResultSet = mock(ResultSet.class);
        when(mockRoomResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockRoomResultSet.getString("available_times")).thenReturn("[]");

        PreparedStatement mockBookingDetailsStmt = mock(PreparedStatement.class);
        when(mockBookingDetailsStmt.executeQuery()).thenReturn(mockBookingResultSet);

        PreparedStatement mockCancelStmt = mock(PreparedStatement.class);
        when(mockCancelStmt.executeUpdate()).thenReturn(1);

        PreparedStatement mockRoomDetailsStmt = mock(PreparedStatement.class);
        when(mockRoomDetailsStmt.executeQuery()).thenReturn(mockRoomResultSet);

        PreparedStatement mockUpdateStmt = mock(PreparedStatement.class);
        when(mockUpdateStmt.executeUpdate()).thenReturn(1);

        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement("SELECT room_id, time_slot FROM booking WHERE booking_id = ?"))
                .thenReturn(mockBookingDetailsStmt);
        when(mockConn.prepareStatement("DELETE FROM booking WHERE booking_id = ?"))
                .thenReturn(mockCancelStmt);
        when(mockConn.prepareStatement("SELECT available_times FROM room_details WHERE room_id = ?"))
                .thenReturn(mockRoomDetailsStmt);
        when(mockConn.prepareStatement("UPDATE room_details SET available_times = ? WHERE room_id = ?"))
                .thenReturn(mockUpdateStmt);

        SmartMeetingRoomImpl smartMeetingRoom = new SmartMeetingRoomImpl(mockConn);

        smartMeetingRoom.cancelBooking(request, actionResponseObserver);

        verify(actionResponseObserver).onNext(any(ActionResponse.class));
        verify(actionResponseObserver).onCompleted();
    }


    @Test
    public void testCheckAvailabilitySuccess() throws SQLException {
        CheckAvailabilityRequest request = CheckAvailabilityRequest.newBuilder()
                .setRoomId(1)
                .build();

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("status")).thenReturn("AVAILABLE");
        when(mockResultSet.getString("available_times")).thenReturn("[\"08:00\", \"13:00\", \"16:00\"]");
        when(mockResultSet.getString("location")).thenReturn("floor 5");
        when(mockResultSet.getInt("room_id")).thenReturn(1);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartMeetingRoom.checkAvailability(request, availabilityResponseObserver);

        InOrder inOrder = inOrder(availabilityResponseObserver);
        inOrder.verify(availabilityResponseObserver).onNext(argThat(response ->
                response.getAvailableTimesList().containsAll(Arrays.asList("08:00", "13:00", "16:00")) &&
                        response.getDetails().getLocation().equals("floor 5")));
        inOrder.verify(availabilityResponseObserver).onCompleted();
    }


    @Test
    public void testCheckAvailabilityFailure() throws SQLException {
        CheckAvailabilityRequest request = CheckAvailabilityRequest.newBuilder()
                .setRoomId(1)
                .build();

        when(mockResultSet.next()).thenReturn(false);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        smartMeetingRoom.checkAvailability(request, availabilityResponseObserver);

        verify(availabilityResponseObserver, never()).onNext(any(AvailabilityResponse.class));
        verify(availabilityResponseObserver).onCompleted();
    }
}
