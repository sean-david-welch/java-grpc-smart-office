package services.smartMeeting;

import io.grpc.stub.StreamObserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SmartMeetingRoomImpl extends SmartMeetingRoomGrpc.SmartMeetingRoomImplBase {

    private final Connection conn;

    public SmartMeetingRoomImpl(Connection conn) {
        this.conn = conn;
    }

    //  <-------- External Grpc methods -------->
    @Override
    public void bookRoom(BookRoomRequest request, StreamObserver<ActionResponse> responseObserver) {
        String bookingId = bookRoomLogic(request);

        boolean success = bookingId != null;
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
        String roomId = request.getRoomId();
        String userId = request.getUserId();
        Timestamp timeSlot = request.getTimeSlot();

        String roomQuery = "select status from roomDetails where roomId = ?";
        String bookingQuery = "insert into booking (roomId, userId, timeslot) values (?, ?, ?)";

        try (PreparedStatement roomStmt = conn.prepareStatement(roomQuery)) {
            roomStmt.setString(1, roomId);
            ResultSet roomResult = roomStmt.executeQuery();
            if (!roomResult.next()) return "No Room found with ID: " + roomId;

            String status = roomResult.getString("status");
            RoomStatus roomStatus = RoomStatus.valueOf(status);

            if (roomStatus == RoomStatus.OCCUPIED || roomStatus == RoomStatus.UNAVAILABLE) {
                return "Room is unavailable" + roomResult;
            }

            try (PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery)) {
                bookingStmt.setString();

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

    private boolean cancelBookingLogic(String bookingId) {
        for (Map<services.smartMeeting.Timestamp, String> roomBookings : bookings.values()) {
            if (roomBookings.containsValue(bookingId)) {
                roomBookings.values().removeIf(id -> id.equals(bookingId));
                return true;
            }
        }
        return false;
    }

    private AvailabilityResponse checkAvailabilityLogic(CheckAvailabilityRequest request) {
        String roomId = request.getRoomId();
        services.smartMeeting.Timestamp timeSlot = request.getTimeSlot();

        if (!rooms.containsKey(roomId)) {
            return AvailabilityResponse.newBuilder()
                    .setSuccess(false)
                    .setStatus(RoomStatus.UNAVAILABLE)
                    .build();
        }

        RoomDetails roomDetails = rooms.get(roomId);
        RoomStatus status = bookings.get(roomId).containsKey(timeSlot) ? RoomStatus.OCCUPIED : RoomStatus.AVAILABLE;

        List<services.smartMeeting.Timestamp> availableTimes = new ArrayList<>();

        if (status == RoomStatus.OCCUPIED) {
            Timestamp googleTimestamp = timeSlot.getValue();

            Timestamp newGoogleTimestamp = Timestamp.newBuilder()
                    .setSeconds(googleTimestamp.getSeconds() + 3600)
                    .setNanos(googleTimestamp.getNanos())
                    .build();

            services.smartMeeting.Timestamp newTimeSlot = services.smartMeeting.Timestamp.newBuilder()
                    .setValue(newGoogleTimestamp)
                    .build();

            availableTimes.add(newTimeSlot);
        }

        return AvailabilityResponse.newBuilder()
                .setSuccess(true)
                .setStatus(status)
                .setDetails(roomDetails)
                .addAllAvailableTimes(availableTimes)
                .build();
    }

}