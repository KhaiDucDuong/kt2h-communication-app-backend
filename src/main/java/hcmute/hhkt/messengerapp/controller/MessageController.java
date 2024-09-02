package hcmute.hhkt.messengerapp.controller;

import hcmute.hhkt.messengerapp.domain.Message;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Logger log = LoggerFactory.getLogger(MessageController.class);

    @MessageMapping("/private-message")
    public Message receivePrivateMessage(@Payload Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getConversation().getId().toString(),"/private",message);
        log.debug("Sending private message {}", message);
        return message;
    }
}
