package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.FriendRequest;
import hcmute.hhkt.messengerapp.domain.InvitationNotification;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.InvitationNotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface InvitationNotificationRepository extends JpaRepository<InvitationNotification, UUID> {
    Page<InvitationNotification> findInvitationNotificationsByReceiverAndIsDeletedIsFalse(User currentUser, Pageable pageable);
    InvitationNotification findInvitationNotificationByFriendRequestAndTypeAndIsDeletedIsFalse(FriendRequest friendRequest, InvitationNotificationType type);

    List<InvitationNotification> findAllByLastModifiedDateBeforeAndIsDeletedIsTrue(Instant lastModifiedDate);
    boolean existsByFriendRequestAndType(FriendRequest friendRequest, InvitationNotificationType type);
}
