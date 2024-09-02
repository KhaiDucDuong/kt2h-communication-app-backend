package hcmute.hhkt.messengerapp.service.MessageService;

import hcmute.hhkt.messengerapp.Response.FriendshipResponse;
import hcmute.hhkt.messengerapp.Response.MessageResponse;
import hcmute.hhkt.messengerapp.Response.Meta;
import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.Conversation;
import hcmute.hhkt.messengerapp.domain.Friendship;
import hcmute.hhkt.messengerapp.domain.Message;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.MessageType;
import hcmute.hhkt.messengerapp.dto.MessageDTO;
import hcmute.hhkt.messengerapp.repository.MessageRepository;
import hcmute.hhkt.messengerapp.service.ConversationService.IConversationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService{
    private final MessageRepository messageRepository;
    private final IConversationService conversationService;
    @Override
    public Message createMessage(MessageDTO messageDTO) {
        if(!MessageType.valueOf(messageDTO.getMessageType()).equals(MessageType.TEXT)){
            throw new IllegalArgumentException(ExceptionMessage.INVALID_MESSAGE_TYPE);
        }

        if(StringUtils.isBlank(messageDTO.getMessage())){
            throw new IllegalArgumentException(ExceptionMessage.MESSAGE_IS_BLANK);
        }

        if(!StringUtils.isBlank(messageDTO.getChannelId()) && !StringUtils.isBlank(messageDTO.getConversationId())){
            throw new IllegalArgumentException(ExceptionMessage.ILLEGAL_MESSAGE_ORIGIN);
        }

        Message message = Message.builder()
                .message(messageDTO.getMessage())
                .messageType(MessageType.valueOf(messageDTO.getMessageType()))
                .build();

        if(StringUtils.isBlank(messageDTO.getConversationId())){
            Conversation conversation = conversationService.findById(UUID.fromString(messageDTO.getConversationId()));
            message.setConversation(conversation);
            //set channel to null
        }
        //message comes from a channel
        //else if (StringUtils.isBlank(messageDTO.getChannelId())){}

        return messageRepository.save(message);
    }

    @Override
    public ResultPaginationResponse getConversationMessages(Conversation conversation, Pageable pageable) {
        Page<Message> messagePage = messageRepository.findMessagesByConversationAndIsDeletedIsFalse(conversation, pageable);
        Meta meta = Meta.builder()
                .page(messagePage.getNumber() + 1)
                .pageSize(messagePage.getSize())
                .pages(messagePage.getTotalPages())
                .total(messagePage.getTotalElements())
                .build();

        return ResultPaginationResponse.builder()
                .meta(meta)
                .result(MessageResponse.fromMessageList(messagePage.getContent()))
                .build();
    }

    @Override
    public Message updateMessage(MessageDTO messageDTO) {
        return null;
    }

    @Override
    public Message findMessageById(UUID messageId) {
        return null;
    }

    @Override
    public void deleteMessage(Message message) {

    }
}
