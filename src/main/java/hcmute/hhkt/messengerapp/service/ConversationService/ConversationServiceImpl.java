package hcmute.hhkt.messengerapp.service.ConversationService;

import hcmute.hhkt.messengerapp.Response.ConversationResponse;
import hcmute.hhkt.messengerapp.Response.MessageResponse;
import hcmute.hhkt.messengerapp.Response.Meta;
import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.Conversation;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements IConversationService{
    private final ConversationRepository conversationRepository;
    @Override
    public Conversation findById(UUID conversationId) {
        Optional<Conversation> conversationOptional = conversationRepository.findById(conversationId);
        if(conversationOptional.isEmpty()){
            throw new IllegalArgumentException(ExceptionMessage.CONVERSATION_NOT_FOUND);
        }
        return conversationOptional.get();
    }

    @Override
    public Conversation findByTwoUsers(User finder, User toUser) {
        Conversation queryConversation = conversationRepository.findConversationByTwoUsers(finder, toUser);

        //create the conversation if it hasn't existed yet
        if(queryConversation == null){
            queryConversation = createConversation(finder, toUser);
        }

        return queryConversation;
    }

    @Override
    public Conversation createConversation(User creator, User target) {
        if(creator == target){
            throw new IllegalArgumentException(ExceptionMessage.CONVERSATION_IDENTICAL_CREATOR_TARGET);
        }

        Conversation conversation = Conversation.builder()
                .creator(creator)
                .creatorNickname(null)
                .target(target)
                .targetNickname(null)
                .build();

        return conversationRepository.save(conversation);
    }

    @Override
    public ResultPaginationResponse findUserConversations(User user, Pageable pageable) {
        Page<Conversation> conversationPage = conversationRepository.findConversationsByCreatorOrTarget(user, user, pageable);
        Meta meta = Meta.builder()
                .page(conversationPage.getNumber() + 1)
                .pageSize(conversationPage.getSize())
                .pages(conversationPage.getTotalPages())
                .total(conversationPage.getTotalElements())
                .build();

        List<Conversation> conversationList = conversationPage.getContent();
        List<User> toUsers = conversationList.stream().map(conversation ->
        {return conversation.getTarget() == user ? conversation.getCreator() : conversation.getTarget();}).toList();

        return ResultPaginationResponse.builder()
                .meta(meta)
                .result(ConversationResponse.fromConversationList(conversationPage.getContent(), toUsers))
                .build();
    }
}
