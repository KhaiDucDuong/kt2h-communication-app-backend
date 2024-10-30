package hcmute.hhkt.messengerapp.service.InvitationNotificationService;

import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.domain.FriendRequest;
import hcmute.hhkt.messengerapp.domain.InvitationNotification;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.InvitationNotificationType;
import org.springframework.data.domain.Pageable;

public interface IInvitationNotificationService {
    ResultPaginationResponse findUserInvitationNotification(User currentUser, Pageable pageable);

    InvitationNotification createInvitationNotification(User toUser, FriendRequest friendRequest, InvitationNotificationType notificationType);

    void deleteInvitationNotification(InvitationNotification invitationNotification);
    void deleteInvitationNotification(FriendRequest friendRequest, InvitationNotificationType type);

    InvitationNotification findInvitationNotification(FriendRequest friendRequest, InvitationNotificationType type);
}
