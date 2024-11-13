package hcmute.hhkt.messengerapp.controller;

import hcmute.hhkt.messengerapp.Exception.UnauthorizedRequestException;
import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.Conversation;
import hcmute.hhkt.messengerapp.service.ConversationService.IConversationService;
import hcmute.hhkt.messengerapp.service.MessageService.IMessageService;
import hcmute.hhkt.messengerapp.util.SecurityUtil;
import hcmute.hhkt.messengerapp.util.annotation.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final Logger log = LoggerFactory.getLogger(MessageController.class);
    private final IConversationService conversationService;
    private final IMessageService messageService;

    @GetMapping("/{conversationId}")
    @PreAuthorize("hasAnyAuthority('USER')")
    @ApiMessage("Fetched messages in conversation")
    public ResponseEntity<?> getMessagesInConversation(@PathVariable String conversationId, Pageable pageable){
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        log.debug("REST request to get messages in conversation {} from user {}", conversationId, email);
        UUID parsedId = UUID.fromString(conversationId);
        Conversation conversation = conversationService.findById(parsedId);

        if(!conversation.getCreator().getEmail().equals(email) &&
        !conversation.getTarget().getEmail().equals(email)){
            throw new UnauthorizedRequestException(ExceptionMessage.USER_CANNOT_ACCESS_THIS_CONVERSATION);
        }

        ResultPaginationResponse response = messageService.getConversationMessages(conversation, pageable);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<?> uploadFile(@RequestParam("file") List<MultipartFile> files) {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files){
            if (file != null && !file.isEmpty()) {
                try {
                    String imageUrl = String.valueOf(messageService.uploadImage(file));  // Assuming this returns the URL of the uploaded image
                    imageUrls.add(imageUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        // Trả về file URL trong response
        Map<String, List<String>> response = new HashMap<>();
        response.put("imageUrls", imageUrls);  // Updated to return a list of URLs
        return ResponseEntity.ok(response);
    }
}
