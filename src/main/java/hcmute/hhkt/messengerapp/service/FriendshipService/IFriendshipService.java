package hcmute.hhkt.messengerapp.service.FriendshipService;

import hcmute.hhkt.messengerapp.domain.Friendship;

import java.util.List;
import java.util.UUID;

public interface IFriendshipService {
    List<Friendship> getUserFriendList(UUID userId);
    Friendship createFriendship(UUID requestedUser, UUID acceptedUser);
    boolean deleteFriendship(UUID requestedUser, UUID deletedUser);
}
