package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.InvitationNotification;
import hcmute.hhkt.messengerapp.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvitationNotificataionRepository extends JpaRepository<InvitationNotification, UUID> {
    Page<InvitationNotification> findInvitationNotificationsByReceiver(User currentUser, Pageable pageable);
}
