package services.smartMeeting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SmartMeetingRoomImpl extends SmartMeetingRoomGrpc.SmartMeetingRoomImplBase {

    private static final System.Logger logger = System.getLogger(SmartMeetingRoomImpl.class.getName());
    private final Connection conn;

    public SmartMeetingRoomImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void bookRoom(BookRoomRequest request, StreamObserver<ActionResponse> responseObserver) {
        Context context = Context.current();
        if (context.getDeadline() != null && context.getDeadline().isExpired()) {
            responseObserver.onError(Status.DEADLINE_EXCEEDED.withDescription("Deadline exceeded").asRuntimeException());
            return;
        }

        boolean success = bookRoomLogic(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(success)
                .setErrorCode(success ? ErrorCode.NONE : ErrorCode.SYSTEM_ERROR)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void cancelBooking(CancelBookingRequest request, StreamObserver<ActionResponse> responseObserver) {
        Context context = Context.current();
        if (context.getDeadline() != null && context.getDeadline().isExpired()) {
            responseObserver.onError(Status.DEADLINE_EXCEEDED.withDescription("Deadline exceeded").asRuntimeException());
            return;
        }

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
        Context context = Context.current();
        if (context.getDeadline() != null && context.getDeadline().isExpired()) {
            responseObserver.onError(Status.DEADLINE_EXCEEDED.withDescription("Deadline exceeded").asRuntimeException());
            return;
        }

        streamAvailabilityLogic(request, responseObserver);
    }

    private boolean bookRoomLogic(BookRoomRequest request) {
        int roomId = request.getRoomId();
        int userId = request.getUserId();
        String timeSlot = request.getTimeSlot();

        String roomQuery = "SELECT status, location, available_times FROM room_details WHERE room_id = ?";
        String bookingQuery = "INSERT INTO booking (room_id, user_id, time_slot) VALUES (?, ?, ?)";
        String updateDetailsQuery = "UPDATE room_details SET available_times = ? WHERE room_id = ?";

        try (PreparedStatement roomStmt = conn.prepareStatement(roomQuery)) {
            roomStmt.setInt(1, roomId);
            ResultSet roomResult = roomStmt.executeQuery();
            if (!roomResult.next()) {
                logger.log(System.Logger.Level.INFO, "No Room found with ID: {0}", roomId);
                return false;
            }

            String status = roomResult.getString("status");
            String availableTimesJson = roomResult.getString("available_times");
            RoomStatus roomStatus = RoomStatus.valueOf(status.toUpperCase());

            if (roomStatus == RoomStatus.OCCUPIED || roomStatus == RoomStatus.UNAVAILABLE) {
                logger.log(System.Logger.Level.INFO, "Room is unavailable: {0}", roomResult);
                return false;
            }

            List<String> availableTimes = new ObjectMapper().readValue(availableTimesJson, new TypeReference<>() {});
            if (!availableTimes.contains(timeSlot)) {
                logger.log(System.Logger.Level.INFO, "Time slot is not available: {0}", timeSlot);
                return false;
            }

            availableTimes.remove(timeSlot);
            String updatedTimesJson = new ObjectMapper().writeValueAsString(availableTimes);

            try (PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery);
                 PreparedStatement updateStmt = conn.prepareStatement(updateDetailsQuery)) {
                bookingStmt.setInt(1, roomId);
                bookingStmt.setInt(2, userId);
                bookingStmt.setString(3, timeSlot);
                bookingStmt.executeUpdate();

                updateStmt.setString(1, updatedTimesJson);
                updateStmt.setInt(2, roomId);
                updateStmt.executeUpdate();

                logger.log(System.Logger.Level.INFO, "Booking complete");
                return true;
            } catch (SQLException e) {
                logger.log(System.Logger.Level.ERROR, "An error occurred while writing to the database: {0}", e);
                return false;
            }
        } catch (SQLException | IOException e) {
            logger.log(System.Logger.Level.ERROR, "An error occurred while querying the database: {0}", e);
            return false;
        }
    }

    private boolean cancelBookingLogic(int bookingId) {
        String bookingDetailsQuery = "SELECT room_id, time_slot FROM booking WHERE booking_id = ?";
        String cancelQuery = "DELETE FROM booking WHERE booking_id = ?";
        String roomDetailsQuery = "SELECT available_times FROM room_details WHERE room_id = ?";
        String updateDetailsQuery = "UPDATE room_details SET available_times = ? WHERE room_id = ?";

        try (PreparedStatement bookingDetailsStmt = conn.prepareStatement(bookingDetailsQuery)) {
            bookingDetailsStmt.setInt(1, bookingId);
            ResultSet bookingResult = bookingDetailsStmt.executeQuery();

            if (!bookingResult.next()) {
                logger.log(System.Logger.Level.INFO, "No booking found with that ID");
                return false;
            }

            int roomId = bookingResult.getInt("room_id");
            String timeSlot = bookingResult.getString("time_slot");

            try (PreparedStatement cancelStmt = conn.prepareStatement(cancelQuery)) {
                cancelStmt.setInt(1, bookingId);
                int affectedRows = cancelStmt.executeUpdate();
                if (affectedRows == 0) {
                    logger.log(System.Logger.Level.INFO, "No booking found with that ID");
                    return false;
                }

                try (PreparedStatement roomDetailsStmt = conn.prepareStatement(roomDetailsQuery)) {
                    roomDetailsStmt.setInt(1, roomId);
                    ResultSet roomResult = roomDetailsStmt.executeQuery();

                    if (!roomResult.next()) {
                        logger.log(System.Logger.Level.INFO, "No Room found with ID: {0}", roomId);
                        return false;
                    }

                    String availableTimesJson = roomResult.getString("available_times");
                    List<String> availableTimes = new ObjectMapper().readValue(availableTimesJson, new TypeReference<>() {});

                    if (!availableTimes.contains(timeSlot)) {
                        availableTimes.add(timeSlot);
                    }

                    String updatedTimesJson = new ObjectMapper().writeValueAsString(availableTimes);

                    try (PreparedStatement updateStmt = conn.prepareStatement(updateDetailsQuery)) {
                        updateStmt.setString(1, updatedTimesJson);
                        updateStmt.setInt(2, roomId);
                        updateStmt.executeUpdate();

                        logger.log(System.Logger.Level.INFO, "Booking was deleted successfully and available times updated!");
                        return true;
                    } catch (SQLException e) {
                        logger.log(System.Logger.Level.ERROR, "An error occurred while updating the database: {0}", e.getMessage());
                        return false;
                    }
                } catch (SQLException | IOException e) {
                    logger.log(System.Logger.Level.ERROR, "An error occurred while querying the database: {0}", e.getMessage());
                    return false;
                }
            } catch (SQLException e) {
                logger.log(System.Logger.Level.ERROR, "An error occurred while deleting the booking: {0}", e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            logger.log(System.Logger.Level.ERROR, "An error occurred while querying the database: {0}", e.getMessage());
            return false;
        }
    }

    private void streamAvailabilityLogic(CheckAvailabilityRequest request, StreamObserver<AvailabilityResponse> responseObserver) {
        int roomId = request.getRoomId();

        String roomQuery = "SELECT * FROM room_details WHERE room_id = ?";
        try (PreparedStatement roomStmt = conn.prepareStatement(roomQuery)) {
            roomStmt.setInt(1, roomId);
            ResultSet roomResult = roomStmt.executeQuery();

            if (!roomResult.next()) {
                responseObserver.onCompleted();
                return;
            }

            RoomDetails roomDetails = RoomDetails.newBuilder()
                    .setRoomId(roomId)
                    .setLocation(roomResult.getString("location"))
                    .build();

            List<String> availableTimes;
            String sqlTimes = roomResult.getString("available_times");

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                availableTimes = objectMapper.readValue(sqlTimes, new TypeReference<>() {});
            } catch (JsonProcessingException e) {
                logger.log(System.Logger.Level.ERROR, "Error parsing JSON: {0}", e.getMessage());
                responseObserver.onCompleted();
                return;
            }

            RoomStatus status = RoomStatus.valueOf(roomResult.getString("status").toUpperCase());

            AvailabilityResponse.Builder responseBuilder = AvailabilityResponse.newBuilder()
                    .setSuccess(true)
                    .setStatus(status)
                    .setDetails(roomDetails);

            for (String time : availableTimes) {
                responseBuilder.addAvailableTimes(time);
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        } catch (SQLException e) {
            logger.log(System.Logger.Level.ERROR, "An error occurred while querying the database: {0}", e.getMessage());
            responseObserver.onCompleted();
        }
    }
}
