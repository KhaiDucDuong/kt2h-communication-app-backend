package hcmute.hhkt.messengerapp.service.FriendRequestService;

import hcmute.hhkt.messengerapp.domain.FriendRequest;
import hcmute.hhkt.messengerapp.domain.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IFriendRequestService {
    List<FriendRequest> getUserSentFriendRequestList(User user, Pageable pageable);
    List<FriendRequest> getUserReceivedFriendRequestList(User user, Pageable pageable);
    FriendRequest sendFriendRequest(User senderUser, User recipientUser);
    FriendRequest findFriendRequestById(UUID friendRequestId);
    FriendRequest updateFriendRequestStatus(UUID friendRequestId, String newStatus);
}
