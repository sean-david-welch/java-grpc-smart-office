package services.smartMeeting;

import io.grpc.stub.StreamObserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SmartMeetingRoomImpl extends SmartMeetingRoomGrpc.SmartMeetingRoomImplBase {

    private final Connection conn;

    public SmartMeetingRoomImpl(Connection conn) {
        this.conn = conn;
    }

    //  <-------- External Grpc methods -------->
    @Override
    public void bookRoom(BookRoomRequest request, StreamObserver<ActionResponse> responseObserver) {
        String bookingMessage = bookRoomLogic(request);

        boolean success = !bookingMessage.isEmpty();
        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(success)
                .setErrorCode(success ? ErrorCode.NONE : ErrorCode.SYSTEM_ERROR)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void cancelBooking(CancelBookingRequest request, StreamObserver<ActionResponse> responseObserver) {
        boolean canceled = cancelBookingLogic(request.getBookingId());

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(canceled)
                .setErrorCode(canceled ? ErrorCode.NONE : ErrorCode.SYSTEM_ERROR)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void checkAvailability(CheckAvailabilityRequest request, StreamObserver<AvailabilityResponse> responseObserver) {
        AvailabilityResponse response = checkAvailabilityLogic(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // <------- Interal Logic Methods ------->
    private String bookRoomLogic(BookRoomRequest request) {
        int roomId = request.getRoomId();
        int userId = request.getUserId();
        String timeSlot = request.getTimeSlot();

        String roomQuery = "select status and location from room_details where room_id = ?";
        String bookingQuery = "insert into booking (room_id, user_id, time_slot) values (?, ?, ?)";

        try (PreparedStatement roomStmt = conn.prepareStatement(roomQuery)) {
            roomStmt.setInt(1, roomId);
            ResultSet roomResult = roomStmt.executeQuery();
            if (!roomResult.next()) return "No Room found with ID: " + roomId;

            String status = roomResult.getString("status");
            RoomStatus roomStatus = RoomStatus.valueOf(status);

            if (roomStatus == RoomStatus.OCCUPIED || roomStatus == RoomStatus.UNAVAILABLE) {
                return "Room is unavailable" + roomResult;
            }

            try (PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery)) {
                bookingStmt.setInt(1, roomId);
                bookingStmt.setInt(2, userId);
                bookingStmt.setString(3, timeSlot);

                return "booking complete";
            } catch (SQLException e) {
                System.out.println("An error occurred while writing to the database" + e);
                return "booking not complete" + e;
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while querying the database" + e);
            return "Booking not complete" + e;
        }
    }

    private boolean cancelBookingLogic(int bookingId) {
        String cancelQuery = "delete from booking where booking_id = ?";

        try (PreparedStatement cancelStmt = conn.prepareStatement(cancelQuery)) {
            cancelStmt.setInt(1, bookingId);
            int affectedRows = cancelStmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("No booking found with that ID");
                return false;
            }
            System.out.println("Booking was deleted successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("An error occurred while querying the database" + e.getMessage());
            return false;
        }
    }

    private AvailabilityResponse checkAvailabilityLogic(CheckAvailabilityRequest request) {
        int roomId = request.getRoomId();
        String timeSlot = request.getTimeSlot();

        String roomQuery = "select * from room_details where room_id = ?";
        try (PreparedStatement roomStmt = conn.prepareStatement(roomQuery)) {
            roomStmt.setInt(1, roomId);
            ResultSet roomResult = roomStmt.executeQuery();

            if (!roomResult.next()) {
                return AvailabilityResponse.newBuilder().setSuccess(false).build();
            }

            RoomDetails roomDetails = RoomDetails.newBuilder()
                    .setRoomId(roomId)
                    .setLocation(roomResult.getString("location"))
                    .build();

            // edit room details with available times in database'
            List<String> availableTimes = new ArrayList<String>();
            String sqlTimes = roomResult.getString("available_time");
            return AvailabilityResponse.newBuilder()
                    .setSuccess(true)
                    .setStatus(RoomStatus.valueOf(roomResult.getString("status")))
                    .setDetails(roomDetails)
                    .addAllAvailableTimes(availableTimes)
                    .build();
        } catch (SQLException e) {
            System.out.println("an error occured while querying the database" + e.getMessage());
            return AvailabilityResponse.newBuilder().setSuccess(false).build();
        }
    }
}