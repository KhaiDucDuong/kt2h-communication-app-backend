package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {
}
