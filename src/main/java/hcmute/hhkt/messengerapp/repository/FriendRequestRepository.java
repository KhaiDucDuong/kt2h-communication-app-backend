package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, UUID> {
}
