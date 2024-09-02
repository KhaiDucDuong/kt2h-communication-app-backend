package hcmute.hhkt.messengerapp.controller;

import hcmute.hhkt.messengerapp.domain.Conversation;
import hcmute.hhkt.messengerapp.domain.Message;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.dto.MessageDTO;
import hcmute.hhkt.messengerapp.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Logger log = LoggerFactory.getLogger(MessageController.class);
    private final ConversationRepository conversationRepository;

    @MessageMapping("/private-message")
    public MessageDTO receivePrivateMessage(@Payload MessageDTO messageDTO){
        Conversation conversation = conversationRepository.findById(UUID.fromString(messageDTO.getConversation_id())).orElseGet(null);
        User toUser = UUID.fromString(messageDTO.getSender_id()).equals(conversation.getCreator().getId()) ? conversation.getTarget() : conversation.getCreator();
        simpMessagingTemplate.convertAndSendToUser(toUser.getId().toString(),"/private", messageDTO);
        log.debug("Sending private message to {} with content {}", toUser.getId(), messageDTO.getMessage());
        return messageDTO;
    }
}
