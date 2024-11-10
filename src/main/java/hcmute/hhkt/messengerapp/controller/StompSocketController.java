package hcmute.hhkt.messengerapp.controller;

import hcmute.hhkt.messengerapp.Response.MessageResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.Conversation;
import hcmute.hhkt.messengerapp.domain.Message;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.dto.MessageDTO;
import hcmute.hhkt.messengerapp.service.ConversationService.IConversationService;
import hcmute.hhkt.messengerapp.service.MessageService.IMessageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class StompSocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Logger log = LoggerFactory.getLogger(StompSocketController.class);
    private final IConversationService conversationService;
    private final IMessageService messageService;

    public void receivePrivateMessage(@Payload MessageDTO messageDTO) {
        log.debug("Stomp private message from {}", messageDTO.getSenderId());
        UUID conversationId = UUID.fromString(messageDTO.getConversationId());
        Conversation conversation = conversationService.findById(conversationId);
        User toUser;

        // Xác định người nhận dựa trên ID của người gửi
        if (UUID.fromString(messageDTO.getSenderId()).equals(conversation.getCreator().getId())) {
            toUser = conversation.getTarget();
        } else if (UUID.fromString(messageDTO.getSenderId()).equals(conversation.getTarget().getId())) {
            toUser = conversation.getCreator();
        } else {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_MESSAGE_SENDER);
        }

        // Tạo và lưu tin nhắn với các loại tin nhắn được gửi đồng thời
        Message message = messageService.createMessage(messageDTO);
        MessageResponse response = MessageResponse.fromMessage(message);

        // Gửi thông điệp cho cả người nhận và người gửi
        simpMessagingTemplate.convertAndSendToUser(toUser.getId().toString(), "/private", response);
        simpMessagingTemplate.convertAndSendToUser(response.getSenderId().toString(), "/private", response);
        log.debug("Sending private message from {} to {} with content {}", messageDTO.getSenderId(), toUser.getId(), message.getMessage());
    }




}
