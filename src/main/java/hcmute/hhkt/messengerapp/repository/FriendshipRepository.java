package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.Friendship;
import hcmute.hhkt.messengerapp.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship, Friendship.FriendshipId> {
    Page<Friendship> findFriendshipsByUserOrFriend(User user, User friend, Pageable pageable);
}
