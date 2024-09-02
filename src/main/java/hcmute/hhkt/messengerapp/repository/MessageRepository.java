package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
}
