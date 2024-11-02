package hcmute.hhkt.messengerapp.service.InvitationNotificationService;

import hcmute.hhkt.messengerapp.Response.InvitationNotificationResponse;
import hcmute.hhkt.messengerapp.Response.Meta;
import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.FriendRequest;
import hcmute.hhkt.messengerapp.domain.InvitationNotification;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.AccountStatus;
import hcmute.hhkt.messengerapp.domain.enums.InvitationNotificationType;
import hcmute.hhkt.messengerapp.repository.InvitationNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class InvitationNotificationServiceImpl implements IInvitationNotificationService{
    private final Logger log = LoggerFactory.getLogger(InvitationNotificationServiceImpl.class);
    private final InvitationNotificationRepository invitationNotificationRepository;
    @Override
    public ResultPaginationResponse findUserInvitationNotification(User currentUser, Pageable pageable) {
        Page<InvitationNotification> invitationNotificationPage = invitationNotificationRepository.findInvitationNotificationsByReceiverAndIsDeletedIsFalse(currentUser, pageable);
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

    @Override
    public InvitationNotification createInvitationNotification(User toUser, FriendRequest friendRequest, InvitationNotificationType notificationType) {
        if(invitationNotificationRepository.existsByFriendRequestAndType(friendRequest, notificationType)){
            throw new IllegalArgumentException(ExceptionMessage.NOTIFICATION_EXIST);
        }
        InvitationNotification invitationNotification = InvitationNotification.builder()
                .receiver(toUser)
                .friendRequest(friendRequest)
                .type(notificationType)
                .build();
        return invitationNotificationRepository.save(invitationNotification);
    }

    @Override
    public void deleteInvitationNotification(InvitationNotification invitationNotification) {
        invitationNotification.setDeleted(true);
        invitationNotificationRepository.save(invitationNotification);
    }

    @Override
    public void deleteInvitationNotification(FriendRequest friendRequest, InvitationNotificationType type) {
        InvitationNotification notification = findInvitationNotification(friendRequest, type);
        if(notification == null){
            throw new IllegalArgumentException(ExceptionMessage.INVITATION_NOTIFICATION_NOT_EXIST);
        }
        deleteInvitationNotification(notification);
    }

    @Override
    public InvitationNotification findInvitationNotification(FriendRequest friendRequest, InvitationNotificationType type) {
        return invitationNotificationRepository.findInvitationNotificationByFriendRequestAndTypeAndIsDeletedIsFalse(friendRequest, type);
    }

    @Async
    @Scheduled(cron = "0 0 3 * * ?") //run at 3am daily
    public void removeUnactivatedAccountsScheduler() {
        log.debug("Remove deleted invitation notifications scheduler starts {}", Date.from(Instant.now()));
        invitationNotificationRepository
                //find all notifications that were last updated 14 days ago
                // and has set isDeleted to true
                .findAllByLastModifiedDateBeforeAndIsDeletedIsTrue(Instant.now().minus(14, ChronoUnit.DAYS))

                .forEach(notification -> {
                    log.debug("Removing deleted invitation notification {}", notification.getId());
                    invitationNotificationRepository.delete(notification);
                });
        log.debug("Remove deleted invitation notifications scheduler ends {}", Date.from(Instant.now()));
    }
}
