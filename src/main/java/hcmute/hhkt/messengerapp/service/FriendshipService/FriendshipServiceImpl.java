package hcmute.hhkt.messengerapp.service.FriendshipService;

import hcmute.hhkt.messengerapp.Response.FriendshipResponse;
import hcmute.hhkt.messengerapp.Response.Meta;
import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.Friendship;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements IFriendshipService {
    private final FriendshipRepository friendshipRepository;

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
    public Friendship createFriendship(UUID requestedUser, UUID acceptedUser) {
        return null;
    }

    @Override
    public Friendship findFriendshipById(User user, User friend) {
        return friendshipRepository.findById(new Friendship.FriendshipId(user, friend))
                .orElse(friendshipRepository.findById(new Friendship.FriendshipId(friend, user)) //reverse keys
                        .orElse(null));
    }

    @Override
    public void deleteFriendship(User requestedUser, User deletedUser) {
        Friendship.FriendshipId id = new Friendship.FriendshipId(requestedUser, deletedUser);
        Friendship friendship = findFriendshipById(requestedUser, deletedUser);
        if(friendship == null){
            throw new IllegalArgumentException(ExceptionMessage.FRIENDSHIP_NOT_EXIST);
        }

        friendshipRepository.delete(friendship);
    }
}
