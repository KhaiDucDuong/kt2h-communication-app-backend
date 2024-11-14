package hcmute.hhkt.messengerapp.controller;

import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.service.FriendRequestService.FriendRequestServiceImpl;
import hcmute.hhkt.messengerapp.service.InvitationNotificationService.InvitationNotificationServiceImpl;
import hcmute.hhkt.messengerapp.service.UserService.UserServiceImpl;
import hcmute.hhkt.messengerapp.util.SecurityUtil;
import hcmute.hhkt.messengerapp.util.annotation.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invitationNotifications")
@RequiredArgsConstructor
public class InvitationNotificationController {
    private final Logger log = LoggerFactory.getLogger(InvitationNotificationController.class);
    private final InvitationNotificationServiceImpl invitationNotificationService;
    private final UserServiceImpl userService;
    private final FriendRequestServiceImpl friendRequestService;
    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('USER')")
    @ApiMessage("Fetched invitation notifications successfully")
    public ResponseEntity<?> getCurrentUserInvitationNotifications(Pageable pageable) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        log.debug("REST request to get invitation notifications of user {}", email);
        User currentUser = userService.findUserByEmail(email);
        ResultPaginationResponse response = invitationNotificationService.findUserInvitationNotification(currentUser, pageable);
        return ResponseEntity.ok().body(response);
    }
}
