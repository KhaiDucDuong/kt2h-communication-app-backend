package hcmute.hhkt.messengerapp.service.FriendRequestService;

import hcmute.hhkt.messengerapp.Exception.UnauthorizedRequestException;
import hcmute.hhkt.messengerapp.Response.FriendRequestResponse;
import hcmute.hhkt.messengerapp.Response.Meta;
import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.FriendRequest;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.FriendRequestStatus;
import hcmute.hhkt.messengerapp.repository.FriendRequestRepository;
import hcmute.hhkt.messengerapp.service.FriendshipService.FriendshipServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FriendRequestServiceImpl implements IFriendRequestService {
    private final FriendRequestRepository friendRequestRepository;

    @Override
    public ResultPaginationResponse findUserOnGoingFriendRequestList(User user, Pageable pageable) {
        List<FriendRequestStatus> queryStatuses = List.of(FriendRequestStatus.PENDING, FriendRequestStatus.REJECTED);
        Page<FriendRequest> friendRequestPage = friendRequestRepository.findFriendRequestsBySenderAndStatusIn(user, queryStatuses, pageable);
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
        Page<FriendRequest> friendRequestPage = friendRequestRepository.findFriendRequestsByReceiverAndStatus(user, FriendRequestStatus.PENDING, pageable);
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
        if (senderUser == recipientUser) {
            throw new IllegalArgumentException(ExceptionMessage.FRIEND_REQUEST_IDENTICAL_SENDER_RECEIVER);
        }

        if (friendRequestRepository.existsFriendRequestBySenderAndReceiver(senderUser, recipientUser)) {
            throw new IllegalArgumentException(ExceptionMessage.FRIEND_REQUEST_EXIST);
        }

        //scenario when user B rejects user A friend request
        //later on user B sends a friend request to user A (user A cannot send another fr if it has been rejected)
        if (friendRequestRepository.existsFriendRequestBySenderAndReceiver(recipientUser, senderUser)) {
            FriendRequest existingFriendRequest = friendRequestRepository.findFriendRequestBySenderAndReceiver(recipientUser, senderUser);
            if (existingFriendRequest.getStatus() == FriendRequestStatus.REJECTED) {
                friendRequestRepository.delete(existingFriendRequest);
            } else {
                throw new IllegalArgumentException(ExceptionMessage.FRIEND_REQUEST_EXIST);
            }
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
    public FriendRequest findFriendRequestBySenderAndRecipient(User sender, User recipient) {
        FriendRequest friendRequest = friendRequestRepository.findFriendRequestBySenderAndReceiver(sender, recipient);
        return friendRequest != null ? friendRequest : friendRequestRepository.findFriendRequestBySenderAndReceiver(recipient, sender);
    }

    @Override
    public FriendRequest updateFriendRequestStatus(UUID friendRequestId, String newStatus) {
        FriendRequest updatedFriendRequest = findFriendRequestById(friendRequestId);
        if (updatedFriendRequest == null) {
            throw new IllegalArgumentException(ExceptionMessage.FRIEND_REQUEST_NOT_FOUND);
        }
        if (FriendRequestStatus.PENDING.name().equals(newStatus) ||
                updatedFriendRequest.getStatus() == FriendRequestStatus.ACCEPTED) {
            throw new IllegalArgumentException(ExceptionMessage.FRIEND_REQUEST_INVALID_NEW_STATUS);
        }
        updatedFriendRequest.setStatus(FriendRequestStatus.valueOf(newStatus));
        return friendRequestRepository.save(updatedFriendRequest);
    }

    @Override
    public void deleteFriendRequest(UUID friendRequestId, User requestedUser) {
        FriendRequest deletedFriendRequest = findFriendRequestById(friendRequestId);
        if (deletedFriendRequest == null) {
            throw new IllegalArgumentException(ExceptionMessage.FRIEND_REQUEST_NOT_FOUND);
        }
        deleteFriendRequest(deletedFriendRequest, requestedUser);
    }

    @Override
    public void deleteFriendRequest(FriendRequest friendRequest, User requestedUser) {
        if (requestedUser != friendRequest.getSender()) {
            throw new UnauthorizedRequestException(ExceptionMessage.ILLEGAL_FRIEND_REQUEST_DELETE_CALLER);
        }
        friendRequestRepository.delete(friendRequest);
    }
}
