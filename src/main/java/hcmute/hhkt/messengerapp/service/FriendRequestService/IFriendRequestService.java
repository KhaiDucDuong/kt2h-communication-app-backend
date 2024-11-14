package hcmute.hhkt.messengerapp.service.FriendRequestService;

import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.domain.FriendRequest;
import hcmute.hhkt.messengerapp.domain.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IFriendRequestService {
    ResultPaginationResponse findUserOnGoingFriendRequestList(User user, Pageable pageable);
    ResultPaginationResponse findUserIncomingFriendRequestList(User user, Pageable pageable);
    FriendRequest sendFriendRequest(User senderUser, User recipientUser);
    FriendRequest findFriendRequestById(UUID friendRequestId);
    FriendRequest findFriendRequestBySenderAndRecipient(User sender, User recipient);
    FriendRequest updateFriendRequestStatus(UUID friendRequestId, String newStatus);
    void deleteFriendRequest(UUID friendRequestId, User requestedUser);

    void deleteFriendRequest(FriendRequest friendRequest, User requestedUser);
}
