package client.services;

import io.grpc.StatusRuntimeException;
import services.smartMeeting.*;

import java.util.Iterator;

public class MeetingRoomClientController {
    private final SmartMeetingRoomGrpc.SmartMeetingRoomBlockingStub meetingRoomStub;

    public MeetingRoomClientController(SmartMeetingRoomGrpc.SmartMeetingRoomBlockingStub meetingRoomStub) {
        this.meetingRoomStub = meetingRoomStub;
    }

    public String bookRoom(int roomId, int userId, String time) {
        try {
            BookRoomRequest request = BookRoomRequest.newBuilder()
                    .setRoomId(roomId)
                    .setUserId(userId)
                    .setTimeSlot(time)
                    .build();
            services.smartMeeting.ActionResponse response = meetingRoomStub.bookRoom(request);
            return "Room booking response: " + response.toString();
        } catch (StatusRuntimeException e) {
            return "Error: " + e.getStatus().getDescription();
        }
    }

    public String cancelBooking(int bookingId) {
        try {
            CancelBookingRequest request = CancelBookingRequest.newBuilder()
                    .setBookingId(bookingId)
                    .build();
            ActionResponse response = meetingRoomStub.cancelBooking(request);
            return "Cancel booking response: " + response.toString();
        } catch (StatusRuntimeException e) {
            return "Error: " + e.getStatus().getDescription();
        }
    }

    public String checkAvailability(int roomId) {
        try {
            CheckAvailabilityRequest request = CheckAvailabilityRequest.newBuilder()
                    .setRoomId(roomId)
                    .build();

            Iterator<AvailabilityResponse> responses = meetingRoomStub.checkAvailability(request);

            StringBuilder availabilityInfo = new StringBuilder();
            availabilityInfo.append("Room Availability for Room ID ").append(roomId).append(":\n");

            while (responses.hasNext()) {
                AvailabilityResponse response = responses.next();
                availabilityInfo.append("Room Status: ").append(response.getStatus()).append("\n");
                availabilityInfo.append("Available Times:\n");
                for (String time : response.getAvailableTimesList()) {
                    availabilityInfo.append(" - ").append(time).append("\n");
                }
                availabilityInfo.append("\n");
            }

            return availabilityInfo.toString();
        } catch (StatusRuntimeException e) {
            return "Error: " + e.getStatus().getDescription();
        }
    }
}
