package services.smartMeeting;

import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;

import java.util.*;

public class SmartMeetingRoomImpl extends SmartMeetingRoomGrpc.SmartMeetingRoomImplBase {

    private final Map<String, RoomDetails> rooms;
    private final Map<String, Map<services.smartMeeting.Timestamp, String>> bookings;

    public SmartMeetingRoomImpl() {
        rooms = new HashMap<>();
        bookings = new HashMap<>();

        RoomDetails room1 = RoomDetails.newBuilder()
                .setRoomId("room1")
                .setName("Conference Room A")
                .setLocation("1st Floor")
                .setCapacity(10)
                .build();
        rooms.put("room1", room1);
        bookings.put("room1", new HashMap<>());
    }

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

    private String bookRoomLogic(BookRoomRequest request) {
        String roomId = request.getRoomId();
        services.smartMeeting.Timestamp timeSlot = request.getTimeSlot();

        if (!rooms.containsKey(roomId) || bookings.get(roomId).containsKey(timeSlot)) {
            return null;
        }

        String bookingId = UUID.randomUUID().toString();
        bookings.get(roomId).put(timeSlot, bookingId);
        return bookingId;
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