package hcmute.hhkt.messengerapp.controller;

import hcmute.hhkt.messengerapp.Response.ConversationResponse;
import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.Conversation;
import hcmute.hhkt.messengerapp.domain.Message;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.service.ConversationService.IConversationService;
import hcmute.hhkt.messengerapp.service.MessageService.IMessageService;
import hcmute.hhkt.messengerapp.service.UserService.UserServiceImpl;
import hcmute.hhkt.messengerapp.util.SecurityUtil;
import hcmute.hhkt.messengerapp.util.annotation.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {
    private final Logger log = LoggerFactory.getLogger(ConversationController.class);
    private final IConversationService conversationService;
    private final IMessageService messageService;
    private final UserServiceImpl userService;
    private final int CONVERSATION_MESSAGE_PAGE_SIZE = 40;
    private final String MESSAGE_SORT_BY = "createdDate";

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('USER')")
    @ApiMessage("Fetched logged in user conversations")
    public ResponseEntity<?> getCurrentUserConversations(Pageable pageable) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        log.debug("REST request to get conversations of user {}", email);
        User currentUser = userService.findUserByEmail(email);
        ResultPaginationResponse response = conversationService.findUserConversations(currentUser, pageable);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('USER')")
    @ApiMessage("Fetched conversation with requested user")
    public ResponseEntity<?> getConversationWithUser(@PathVariable String userId) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        log.debug("REST request to get conversations with user {} from user email {}", userId, email);
        User currentUser = userService.findUserByEmail(email);
        UUID parsedId = UUID.fromString(userId);
        User toUser = userService.findById(parsedId);

        if(currentUser == null || toUser == null){
            throw new IllegalArgumentException(ExceptionMessage.USER_NOT_EXIST);
        }

        Conversation conversation = conversationService.findByTwoUsers(currentUser, toUser);
        //find messages
        Sort sort =  Sort.by(Sort.Direction.DESC, MESSAGE_SORT_BY);
        Pageable pageable = PageRequest.of(0, CONVERSATION_MESSAGE_PAGE_SIZE, sort);
        ResultPaginationResponse messagesPaginationResponse = messageService.getConversationMessages(conversation, pageable);

        return ResponseEntity.ok().body(ConversationResponse.fromConversation(conversation, toUser, messagesPaginationResponse));
    }

}
