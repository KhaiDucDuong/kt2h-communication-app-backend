package hcmute.hhkt.messengerapp.service.MessageService;

import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.domain.Conversation;
import hcmute.hhkt.messengerapp.domain.Message;
import hcmute.hhkt.messengerapp.dto.MessageDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IMessageService {
    Message createMessage(MessageDTO messageDTO);
    ResultPaginationResponse getConversationMessages(Conversation conversation, Pageable pageable);
    Message updateMessage(MessageDTO messageDTO);
    Message findMessageById(UUID messageId);
    void deleteMessage(Message message);
}
