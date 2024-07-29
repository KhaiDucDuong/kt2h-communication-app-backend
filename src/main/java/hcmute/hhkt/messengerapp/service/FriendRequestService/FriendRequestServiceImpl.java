package hcmute.hhkt.messengerapp.service.FriendRequestService;

import hcmute.hhkt.messengerapp.Response.FriendRequestResponse;
import hcmute.hhkt.messengerapp.Response.Meta;
import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.FriendRequest;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.FriendRequestStatus;
import hcmute.hhkt.messengerapp.repository.FriendRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FriendRequestServiceImpl implements IFriendRequestService{
    private final FriendRequestRepository friendRequestRepository;
    @Override
    public ResultPaginationResponse findUserOnGoingFriendRequestList(User user, Pageable pageable) {
        Page<FriendRequest> friendRequestPage = friendRequestRepository.findFriendRequestsBySender(user, pageable);
        Meta meta = Meta.builder()
                .page(friendRequestPage.getNumber() + 1)
                .pageSize(friendRequestPage.getSize())
                .pages(friendRequestPage.getTotalPages())
                .total(friendRequestPage.getTotalElements())
                .build();

        return ResultPaginationResponse.builder()
                .meta(meta)
                .result(FriendRequestResponse.generateFriendRequestResponseList(friendRequestPage.getContent()))
                .build();
    }

    @Override
    public ResultPaginationResponse findUserIncomingFriendRequestList(User user, Pageable pageable) {
        Page<FriendRequest> friendRequestPage = friendRequestRepository.findFriendRequestsByReceiver(user, pageable);
        Meta meta = Meta.builder()
                .page(friendRequestPage.getNumber() + 1)
                .pageSize(friendRequestPage.getSize())
                .pages(friendRequestPage.getTotalPages())
                .total(friendRequestPage.getTotalElements())
                .build();

        return ResultPaginationResponse.builder()
                .meta(meta)
                .result(FriendRequestResponse.generateFriendRequestResponseList(friendRequestPage.getContent()))
                .build();
    }

    @Override
    public FriendRequest sendFriendRequest(User senderUser, User recipientUser) {
        if(friendRequestRepository.existsFriendRequestBySenderAndReceiver(senderUser, recipientUser) ||
                friendRequestRepository.existsFriendRequestBySenderAndReceiver(recipientUser, senderUser)){
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
        if(FriendRequestStatus.PENDING.name().equals(newStatus)){
            throw new IllegalArgumentException(ExceptionMessage.FRIEND_REQUEST_INVALID_NEW_STATUS);
        }
        updatedFriendRequest.setStatus(FriendRequestStatus.valueOf(newStatus));
        return friendRequestRepository.save(updatedFriendRequest);
    }

    @Override
    public void deleteFriendRequest(UUID friendRequestId) {
        FriendRequest deletedFriendRequest = findFriendRequestById(friendRequestId);
        if(deletedFriendRequest == null){
            throw new IllegalArgumentException(ExceptionMessage.FRIEND_REQUEST_NOT_FOUND);
        }
        friendRequestRepository.delete(deletedFriendRequest);
    }
}
