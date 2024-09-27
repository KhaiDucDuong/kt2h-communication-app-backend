package hcmute.hhkt.messengerapp.service.InvitationNotificationService;

import hcmute.hhkt.messengerapp.Response.FriendRequestResponse;
import hcmute.hhkt.messengerapp.Response.InvitationNotificationResponse;
import hcmute.hhkt.messengerapp.Response.Meta;
import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.domain.FriendRequest;
import hcmute.hhkt.messengerapp.domain.InvitationNotification;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.repository.InvitationNotificataionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationNotificationServiceImpl implements IInvitationNotificationService{
    private final InvitationNotificataionRepository invitationNotificataionRepository;
    @Override
    public ResultPaginationResponse findUserInvitationNotification(User currentUser, Pageable pageable) {
        Page<InvitationNotification> invitationNotificationPage = invitationNotificataionRepository.findInvitationNotificationsByReceiver(currentUser, pageable);
        Meta meta = Meta.builder()
                .page(invitationNotificationPage.getNumber() + 1)
                .pageSize(invitationNotificationPage.getSize())
                .pages(invitationNotificationPage.getTotalPages())
                .total(invitationNotificationPage.getTotalElements())
                .build();

        return ResultPaginationResponse.builder()
                .meta(meta)
                .result(InvitationNotificationResponse.fromInvitationNotificationList(invitationNotificationPage.getContent()))
                .build();
    }
}
