package hcmute.hhkt.messengerapp.controller;

import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.service.ConversationService.IConversationService;
import hcmute.hhkt.messengerapp.service.UserService.UserServiceImpl;
import hcmute.hhkt.messengerapp.util.SecurityUtil;
import hcmute.hhkt.messengerapp.util.annotation.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {
    private final Logger log = LoggerFactory.getLogger(ConversationController.class);
    private final IConversationService conversationService;
    private final UserServiceImpl userService;

    @GetMapping("/me")
    @ApiMessage("Fetched logged in user conversations")
    public ResponseEntity<?> getCurrentUserConversations(Pageable pageable) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        log.debug("REST request to get conversations of user {}", email);
        User currentUser = userService.findUserByEmail(email);
        ResultPaginationResponse response = conversationService.findUserConversations(currentUser, pageable);
        return ResponseEntity.ok().body(response);
    }
}
