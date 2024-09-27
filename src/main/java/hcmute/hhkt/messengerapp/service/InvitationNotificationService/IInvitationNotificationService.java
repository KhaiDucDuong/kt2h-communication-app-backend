package hcmute.hhkt.messengerapp.service.InvitationNotificationService;

import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.domain.FriendRequest;
import hcmute.hhkt.messengerapp.domain.InvitationNotification;
import hcmute.hhkt.messengerapp.domain.User;
import org.springframework.data.domain.Pageable;

public interface IInvitationNotificationService {
    ResultPaginationResponse findUserInvitationNotification(User currentUser, Pageable pageable);

    InvitationNotification createInvitationNotification(User toUser, FriendRequest friendRequest);
}
