package smartMeeting;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.smartMeeting.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SmartMeetingRoomImplTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private SmartMeetingRoomImpl smartMeetingRoom;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        smartMeetingRoom = new SmartMeetingRoomImpl(mockConnection);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    void testBookRoom() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("status")).thenReturn(RoomStatus.AVAILABLE.name());
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        BookRoomRequest request = BookRoomRequest.newBuilder()
                .setRoomId(1)
                .setUserId(1)
                .setTimeSlot("2023-08-04 14:00-15:00")
                .build();

        // Act
        TestStreamObserver<ActionResponse> responseObserver = new TestStreamObserver<>();
        smartMeetingRoom.bookRoom(request, responseObserver);

        // Assert
        assertTrue(responseObserver.getValues().get(0).getSuccess());
        assertEquals(ErrorCode.NONE, responseObserver.getValues().get(0).getErrorCode());
        verify(mockPreparedStatement, times(2)).setInt(anyInt(), anyInt());
        verify(mockPreparedStatement).setString(anyInt(), anyString());
    }

    @Test
    void testCancelBooking() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        CancelBookingRequest request = CancelBookingRequest.newBuilder()
                .setBookingId(1)
                .build();

        // Act
        TestStreamObserver<ActionResponse> responseObserver = new TestStreamObserver<>();
        smartMeetingRoom.cancelBooking(request, responseObserver);

        // Assert
        assertTrue(responseObserver.getValues().get(0).getSuccess());
        assertEquals(ErrorCode.NONE, responseObserver.getValues().get(0).getErrorCode());
        verify(mockPreparedStatement).setInt(1, 1);
    }

    @Test
    void testCheckAvailability() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("location")).thenReturn("Room A");
        when(mockResultSet.getString("status")).thenReturn(RoomStatus.AVAILABLE.name());

        CheckAvailabilityRequest request = CheckAvailabilityRequest.newBuilder()
                .setRoomId(1)
                .setTimeSlot("2023-08-04 14:00-15:00")
                .build();

        // Act
        TestStreamObserver<AvailabilityResponse> responseObserver = new TestStreamObserver<>();
        smartMeetingRoom.checkAvailability(request, responseObserver);

        // Assert
        AvailabilityResponse response = responseObserver.getValues().get(0);
        assertTrue(response.getSuccess());
        assertEquals(RoomStatus.AVAILABLE, response.getStatus());
        assertEquals(1, response.getDetails().getRoomId());
        assertEquals("Room A", response.getDetails().getLocation());
        verify(mockPreparedStatement).setInt(1, 1);
    }
}