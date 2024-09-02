package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
}
