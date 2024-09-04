package hcmute.hhkt.messengerapp.service.ConversationService;

import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.domain.Conversation;
import hcmute.hhkt.messengerapp.domain.User;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IConversationService {
    Conversation findById(UUID conversationId);
    Conversation findByTwoUsers(User finder, User toUser);
    Conversation createConversation(User creator, User target);
    ResultPaginationResponse findUserConversations(User user, Pageable pageable);
}
