package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.FriendRequest;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.FriendRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, UUID> {
    Page<FriendRequest> findFriendRequestsBySenderAndStatusIn(User sender, List<FriendRequestStatus> statuses, Pageable pageable);
    Page<FriendRequest> findFriendRequestsByReceiverAndStatus(User receiver, FriendRequestStatus status, Pageable pageable);
    FriendRequest findFriendRequestBySenderAndReceiver(User receiver, User sender);
    boolean existsFriendRequestBySenderAndReceiver(User receiver, User sender);
}
