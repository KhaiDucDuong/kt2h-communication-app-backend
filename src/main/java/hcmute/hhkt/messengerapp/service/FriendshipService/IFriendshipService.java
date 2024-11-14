package hcmute.hhkt.messengerapp.service.FriendshipService;

import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.domain.Friendship;
import hcmute.hhkt.messengerapp.domain.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IFriendshipService {
    ResultPaginationResponse findUserFriendList(User userId, Pageable pageable);
    Friendship createFriendship(User user, User friend);
    Friendship findFriendshipById(User user, User friend);
    void deleteFriendship(User requestedUser, User deletedUser);
    boolean existFriendshipById(User user, User friend);
}
