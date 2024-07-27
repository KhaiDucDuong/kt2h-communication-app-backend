package hcmute.hhkt.messengerapp.service.FriendRequestService;

import hcmute.hhkt.messengerapp.domain.FriendRequest;

import java.util.List;
import java.util.UUID;

public interface IFriendRequestService {
    List<FriendRequest> getUserFriendRequestList(UUID userId);
    FriendRequest sendFriendRequest(UUID senderUser, UUID recipientUser);
    FriendRequest updateFriendRequestStatus(UUID friendRequestId, String newStatus);
}
