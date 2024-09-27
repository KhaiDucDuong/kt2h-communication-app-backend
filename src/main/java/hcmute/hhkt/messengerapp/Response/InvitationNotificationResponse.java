package hcmute.hhkt.messengerapp.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.hhkt.messengerapp.domain.InvitationNotification;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class InvitationNotificationResponse {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("sent_date_time")
    private Instant sent_date_time;

    @JsonProperty("friend_request")
    private FriendRequestResponse friendRequest;

    public static InvitationNotificationResponse fromIntivationNotification(InvitationNotification notification){
        return InvitationNotificationResponse.builder()
                .id(notification.getId())
                .sent_date_time(notification.getCreatedDate())
                .friendRequest(FriendRequestResponse.generateFriendRequestResponse(notification.getFriendRequest()))
                .build();
    }

    public static List<InvitationNotificationResponse> fromInvitationNotificationList(List<InvitationNotification> notificationList){
        return notificationList.stream().map(InvitationNotificationResponse::fromIntivationNotification).toList();
    }
}
