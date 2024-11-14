package hcmute.hhkt.messengerapp.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.hhkt.messengerapp.domain.InvitationNotification;
import hcmute.hhkt.messengerapp.domain.enums.NotificationSocketEvent;
import lombok.*;

@Getter
@Setter
@Builder
public class InvitationNotificationSocketResponse{
    @JsonProperty("socket_event")
    private String socketEvent;

    @JsonProperty("invitation_notification")
    InvitationNotificationResponse invitationNotificationResponse;

    public static InvitationNotificationSocketResponse fromInvitationNotificationWithEvent(InvitationNotification notification, NotificationSocketEvent event){
        return InvitationNotificationSocketResponse.builder()
                .socketEvent(event.name())
                .invitationNotificationResponse(InvitationNotificationResponse.fromIntivationNotification(notification))
                .build();
    }
}
