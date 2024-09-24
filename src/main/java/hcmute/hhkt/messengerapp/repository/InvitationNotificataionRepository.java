package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.InvitationNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvitationNotificataionRepository extends JpaRepository<InvitationNotification, UUID> {
}
