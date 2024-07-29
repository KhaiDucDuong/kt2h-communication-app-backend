package hcmute.hhkt.messengerapp.service.FriendshipService;

import hcmute.hhkt.messengerapp.Response.FriendshipResponse;
import hcmute.hhkt.messengerapp.Response.Meta;
import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.FriendRequest;
import hcmute.hhkt.messengerapp.domain.Friendship;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.repository.FriendRequestRepository;
import hcmute.hhkt.messengerapp.repository.FriendshipRepository;
import hcmute.hhkt.messengerapp.service.FriendRequestService.FriendRequestServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements IFriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final FriendRequestServiceImpl friendRequestService;
    private final FriendRequestRepository friendRequestRepository;

    @Override
    public ResultPaginationResponse findUserFriendList(User user, Pageable pageable) {
        Page<Friendship> friendshipPage = friendshipRepository.findFriendshipsByUserOrFriend(user, user, pageable);
        Meta meta = Meta.builder()
                .page(friendshipPage.getNumber() + 1)
                .pageSize(friendshipPage.getSize())
                .pages(friendshipPage.getTotalPages())
                .total(friendshipPage.getTotalElements())
                .build();

        List<User> friendList = friendshipPage.getContent().stream().map(friendship -> {
            if (friendship.getUser() == user) return friendship.getFriend();
            else return friendship.getUser();
        }).toList();

        return ResultPaginationResponse.builder()
                .meta(meta)
                .result(FriendshipResponse.generateFriendshipListResponse(friendList))
                .build();
    }

    @Override
    public Friendship createFriendship(User user, User friend) {
        Friendship newFriendShip = Friendship.builder()
                .user(user)
                .friend(friend)
                .build();
        return friendshipRepository.save(newFriendShip);
    }

    @Override
    public Friendship findFriendshipById(User user, User friend) {
        return friendshipRepository.findById(new Friendship.FriendshipId(user, friend))
                .orElse(friendshipRepository.findById(new Friendship.FriendshipId(friend, user)) //reverse keys
                        .orElse(null));
    }

    @Override
    @Transactional
    public void deleteFriendship(User requestedUser, User deletedUser) {
        Friendship.FriendshipId id = new Friendship.FriendshipId(requestedUser, deletedUser);
        Friendship friendship = findFriendshipById(requestedUser, deletedUser);
        if(friendship == null){
            throw new IllegalArgumentException(ExceptionMessage.FRIENDSHIP_NOT_EXIST);
        }

        FriendRequest existingFriendRequest = friendRequestService.findFriendRequestBySenderAndRecipient(friendship.getUser(), friendship.getFriend());
        if(existingFriendRequest != null){
            friendRequestRepository.delete(existingFriendRequest);
        }

        friendshipRepository.delete(friendship);
    }

    @Override
    public boolean existFriendshipById(User user, User friend) {
        Friendship.FriendshipId id = new Friendship.FriendshipId(user, friend);
        Friendship.FriendshipId reversedId = new Friendship.FriendshipId(friend, user);

        return friendshipRepository.existsById(id) || friendshipRepository.existsById(reversedId);
    }
}
