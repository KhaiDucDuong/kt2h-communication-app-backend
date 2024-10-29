package hcmute.hhkt.messengerapp.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.hhkt.messengerapp.domain.InvitationNotification;
import hcmute.hhkt.messengerapp.domain.enums.NotificationSocketEvent;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
public class InvitationNotificationSocketResponse{
    @JsonProperty("socket_event")
    private String socketEvent;

    @JsonProperty("invitation_notification")
    InvitationNotificationResponse invitationNotificationResponse;

    public static InvitationNotificationSocketResponse fromIntivationNotificationWithEvent(InvitationNotification notification, NotificationSocketEvent event){
        return InvitationNotificationSocketResponse.builder()
                .socketEvent(event.name())
                .invitationNotificationResponse(InvitationNotificationResponse.fromIntivationNotification(notification))
                .build();
    }
}
