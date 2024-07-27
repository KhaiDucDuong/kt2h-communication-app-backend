package hcmute.hhkt.messengerapp.service.FriendRequestService;

import hcmute.hhkt.messengerapp.domain.FriendRequest;

import java.util.List;
import java.util.UUID;

public class FriendRequestServiceImpl implements IFriendRequestService{
    @Override
    public List<FriendRequest> getUserFriendRequestList(UUID userId) {
        return null;
    }

    @Override
    public FriendRequest sendFriendRequest(UUID senderUser, UUID recipientUser) {
        return null;
    }

    @Override
    public FriendRequest updateFriendRequestStatus(UUID friendRequestId, String newStatus) {
        return null;
    }
}
