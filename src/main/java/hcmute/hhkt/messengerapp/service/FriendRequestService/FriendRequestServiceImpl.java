package hcmute.hhkt.messengerapp.service.FriendRequestService;

import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.FriendRequest;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.FriendRequestStatus;
import hcmute.hhkt.messengerapp.repository.FriendRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FriendRequestServiceImpl implements IFriendRequestService{
    private final FriendRequestRepository friendRequestRepository;
    @Override
    public List<FriendRequest> getUserSentFriendRequestList(User user, Pageable pageable) {
        return friendRequestRepository.findFriendRequestsBySender(user, pageable);
    }

    @Override
    public List<FriendRequest> getUserReceivedFriendRequestList(User user, Pageable pageable) {
        return friendRequestRepository.findFriendRequestsByReceiver(user, pageable);
    }

    @Override
    public FriendRequest sendFriendRequest(User senderUser, User recipientUser) {
        if(friendRequestRepository.existsFriendRequestBySenderAndReceiver(senderUser, recipientUser)){
            throw new IllegalArgumentException(ExceptionMessage.FRIEND_REQUEST_EXIST);
        }
        if(senderUser == recipientUser){
            throw new IllegalArgumentException(ExceptionMessage.FRIEND_REQUEST_IDENTICAL_SENDER_RECEIVER);
        }

        FriendRequest newFriendRequest = FriendRequest.builder()
                .sender(senderUser)
                .receiver(recipientUser)
                .build();
        return friendRequestRepository.save(newFriendRequest);
    }

    @Override
    public FriendRequest findFriendRequestById(UUID friendRequestId) {
        return friendRequestRepository.findById(friendRequestId).orElse(null);
    }

    @Override
    public FriendRequest updateFriendRequestStatus(UUID friendRequestId, String newStatus) {
        FriendRequest updatedFriendRequest = findFriendRequestById(friendRequestId);
        if(updatedFriendRequest == null){
            throw new IllegalArgumentException(ExceptionMessage.FRIEND_REQUEST_NOT_FOUND);
        }
        updatedFriendRequest.setStatus(FriendRequestStatus.valueOf(newStatus));
        return friendRequestRepository.save(updatedFriendRequest);
    }
}
