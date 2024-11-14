package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.Conversation;
import hcmute.hhkt.messengerapp.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
    Page<Conversation> findConversationsByCreatorOrTarget(User creator, User target, Pageable pageable);

    @Query("select c from Conversation c where c.creator =  ?1 and c.target = ?2 or c.creator = ?2 and c.target = ?1")
    Conversation findConversationByTwoUsers(User creator, User target);
}
