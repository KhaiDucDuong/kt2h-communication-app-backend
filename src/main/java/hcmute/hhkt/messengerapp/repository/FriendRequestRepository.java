package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.FriendRequest;
import hcmute.hhkt.messengerapp.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, UUID> {
    Page<FriendRequest> findFriendRequestsBySender(User sender, Pageable pageable);
    Page<FriendRequest> findFriendRequestsByReceiver(User receiver, Pageable pageable);
    FriendRequest findFriendRequestBySenderAndReceiver(User receiver, User sender);
    boolean existsFriendRequestBySenderAndReceiver(User receiver, User sender);
}
