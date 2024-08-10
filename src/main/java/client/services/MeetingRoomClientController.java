package client.services;

import io.grpc.StatusRuntimeException;
import services.smartMeeting.BookRoomRequest;
import services.smartMeeting.SmartMeetingRoomGrpc;

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
}
