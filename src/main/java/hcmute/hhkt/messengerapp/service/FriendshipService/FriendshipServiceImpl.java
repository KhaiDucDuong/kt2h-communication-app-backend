package hcmute.hhkt.messengerapp.service.FriendshipService;

import hcmute.hhkt.messengerapp.domain.Friendship;

import java.util.List;
import java.util.UUID;

public class FriendshipServiceImpl implements IFriendshipService {
    @Override
    public List<Friendship> getUserFriendList(UUID userId) {
        return null;
    }

    @Override
    public Friendship createFriendship(UUID requestedUser, UUID acceptedUser) {
        return null;
    }

    @Override
    public boolean deleteFriendship(UUID requestedUser, UUID deletedUser) {
        return false;
    }
}
