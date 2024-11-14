package hcmute.hhkt.messengerapp.controller;

import hcmute.hhkt.messengerapp.Exception.UnauthorizedRequestException;
import hcmute.hhkt.messengerapp.Response.FriendRequestResponse;
import hcmute.hhkt.messengerapp.Response.InvitationNotificationSocketResponse;
import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.FriendRequest;
import hcmute.hhkt.messengerapp.domain.InvitationNotification;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.FriendRequestStatus;
import hcmute.hhkt.messengerapp.domain.enums.InvitationNotificationType;
import hcmute.hhkt.messengerapp.domain.enums.NotificationSocketEvent;
import hcmute.hhkt.messengerapp.dto.FriendRequestDTO;
import hcmute.hhkt.messengerapp.service.ConversationService.ConversationServiceImpl;
import hcmute.hhkt.messengerapp.service.FriendRequestService.FriendRequestServiceImpl;
import hcmute.hhkt.messengerapp.service.FriendshipService.FriendshipServiceImpl;
import hcmute.hhkt.messengerapp.service.InvitationNotificationService.InvitationNotificationServiceImpl;
import hcmute.hhkt.messengerapp.service.UserService.UserServiceImpl;
import hcmute.hhkt.messengerapp.util.RegrexUtil;
import hcmute.hhkt.messengerapp.util.SecurityUtil;
import hcmute.hhkt.messengerapp.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friendRequests")
@RequiredArgsConstructor
public class FriendRequestController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Logger log = LoggerFactory.getLogger(FriendRequestController.class);
    private final UserServiceImpl userService;
    private final FriendRequestServiceImpl friendRequestService;
    private final FriendshipServiceImpl friendshipService;
    private final ConversationServiceImpl conversationService;
    private final InvitationNotificationServiceImpl invitationNotificationService;

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('USER')")
    @ApiMessage("Created friend request successfully")
    @Transactional
    public ResponseEntity<?> createFriendRequest(@Valid @RequestBody FriendRequestDTO friendRequestDTO) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        log.debug("REST request to create friend request to user Id {} from {}", friendRequestDTO.getReceiverId(), email);

        User senderUser = userService.findUserByEmail(email);

        User receiverUser = null;
        if(friendRequestDTO.getReceiverId() != null){
            receiverUser = userService.findById(friendRequestDTO.getReceiverId());
        } else if(StringUtils.isNotBlank(friendRequestDTO.getReceiverUsername())){
            String receiverUsername = friendRequestDTO.getReceiverUsername();
            boolean usernameIsEmail = RegrexUtil.isEmail(receiverUsername);
            receiverUser = usernameIsEmail ? userService.findUserByEmail(receiverUsername) : userService.findUserByUsername(receiverUsername);
        }

        if (senderUser == null || receiverUser == null) {
            throw new IllegalArgumentException(ExceptionMessage.USER_NOT_EXIST);
        }
        if (friendshipService.existFriendshipById(senderUser, receiverUser)) {
            throw new IllegalArgumentException(ExceptionMessage.FRIENDSHIP_EXIST);
        }

        FriendRequest newFriendRequest = friendRequestService.sendFriendRequest(senderUser, receiverUser);

        //create & send notification via web socket
        InvitationNotification newInvitationNotification = invitationNotificationService
                .createInvitationNotification(receiverUser, newFriendRequest, InvitationNotificationType.FRIEND_REQUEST_RECEIVED);
        InvitationNotificationSocketResponse notificationResponse =
                InvitationNotificationSocketResponse.fromInvitationNotificationWithEvent(newInvitationNotification, NotificationSocketEvent.RECEIVE_FRIEND_REQUEST);
        simpMessagingTemplate.convertAndSendToUser(receiverUser.getId().toString(),"/notification", notificationResponse);

        return ResponseEntity.ok().body(FriendRequestResponse.generateFriendRequestResponse(newFriendRequest));
    }

    @GetMapping("/incoming/me")
    @PreAuthorize("hasAnyAuthority('USER')")
    @ApiMessage("Fetched incoming friend requests successfully")
    public ResponseEntity<?> getCurrentUserIncomingFriendRequests(Pageable pageable) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        log.debug("REST request to get incoming friend requests of user {}", email);
        User currentUser = userService.findUserByEmail(email);
        ResultPaginationResponse response = friendRequestService.findUserIncomingFriendRequestList(currentUser, pageable);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/outgoing/me")
    @PreAuthorize("hasAnyAuthority('USER')")
    @ApiMessage("Fetched outgoing friend requests successfully")
    public ResponseEntity<?> getCurrentUserOutgoingFriendRequests(Pageable pageable) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        log.debug("REST request to get outgoing friend requests of user {}", email);
        User currentUser = userService.findUserByEmail(email);
        ResultPaginationResponse response = friendRequestService.findUserOnGoingFriendRequestList(currentUser, pageable);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("")
    @PreAuthorize("hasAnyAuthority('USER')")
    @ApiMessage("Updated friend request status successfully")
    @Transactional
    public ResponseEntity<?> updateFriendRequestStatus(@Valid @RequestBody FriendRequestDTO friendRequestDTO) throws UnauthorizedRequestException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        log.debug("REST request to update friend request status {} from user {}", friendRequestDTO.getStatus(), email);

        if(friendRequestDTO.getId()==null || StringUtils.isBlank(friendRequestDTO.getStatus())){
            throw new IllegalArgumentException(ExceptionMessage.MISSING_PARAMETERS);
        }

        FriendRequest updatedFriendRequest = friendRequestService.updateFriendRequestStatus(friendRequestDTO.getId(), friendRequestDTO.getStatus());

        User currentUser = userService.findUserByEmail(email);
        User friendRequestSender = updatedFriendRequest.getSender();
        if(currentUser == friendRequestSender){
            throw new UnauthorizedRequestException(ExceptionMessage.ILLEGAL_FRIEND_REQUEST_UPDATE_STATUS_CALLER);
        }

        if(FriendRequestStatus.REJECTED == updatedFriendRequest.getStatus()){
            //delete the receive friend request notification
            invitationNotificationService.deleteInvitationNotification(updatedFriendRequest, InvitationNotificationType.FRIEND_REQUEST_RECEIVED);

        }

        //create friendship if the FR is accepted
        if(FriendRequestStatus.ACCEPTED == updatedFriendRequest.getStatus()){
            friendshipService.createFriendship(updatedFriendRequest.getSender(), updatedFriendRequest.getReceiver());
            //find the conversation, if it doesn't exist then create one
            conversationService.findByTwoUsers(updatedFriendRequest.getSender(), updatedFriendRequest.getReceiver());

            //delete the receive friend request notification
//            invitationNotificationService.deleteInvitationNotification(updatedFriendRequest, InvitationNotificationType.FRIEND_REQUEST_RECEIVED);
            //create & send accept fr notification via web socket
            InvitationNotification newInvitationNotification = invitationNotificationService
                    .createInvitationNotification(friendRequestSender, updatedFriendRequest, InvitationNotificationType.FRIEND_REQUEST_ACCEPTED);
            InvitationNotificationSocketResponse notificationResponse =
                    InvitationNotificationSocketResponse.fromInvitationNotificationWithEvent(newInvitationNotification, NotificationSocketEvent.RECEIVER_ACCEPT_FRIEND_REQUEST);
            simpMessagingTemplate.convertAndSendToUser(friendRequestSender.getId().toString(),"/notification", notificationResponse);
        }

        return ResponseEntity.ok().body(FriendRequestResponse.generateFriendRequestResponse(updatedFriendRequest));
    }

    @DeleteMapping("")
    @PreAuthorize("hasAnyAuthority('USER')")
    @ApiMessage("Deleted friend request successfully")
    @Transactional
    public ResponseEntity<?> deleteFriendRequest(@Valid @RequestBody FriendRequestDTO friendRequestDTO) throws UnauthorizedRequestException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        log.debug("REST request to delete friend request id {} from user {}", friendRequestDTO.getId(), email);

        if(friendRequestDTO.getId()==null){
            throw new IllegalArgumentException(ExceptionMessage.MISSING_PARAMETERS);
        }
        User currentUser = userService.findUserByEmail(email);
        FriendRequest friendRequest = friendRequestService.findFriendRequestById(friendRequestDTO.getId());
        if (friendRequest == null) {
            throw new IllegalArgumentException(ExceptionMessage.FRIEND_REQUEST_NOT_FOUND);
        }

        //send notification via web socket to the other user about the deleted fr
        InvitationNotification invitationNotification = invitationNotificationService.findInvitationNotification(friendRequest, InvitationNotificationType.FRIEND_REQUEST_RECEIVED);
        if(invitationNotification != null){
            invitationNotificationService.deleteInvitationNotification(invitationNotification);
            InvitationNotificationSocketResponse notificationResponse =
                    InvitationNotificationSocketResponse.fromInvitationNotificationWithEvent(invitationNotification, NotificationSocketEvent.SENDER_DELETE_FRIEND_REQUEST);
            simpMessagingTemplate.convertAndSendToUser(invitationNotification.getReceiver().getId().toString(),"/notification", notificationResponse);
        }

        friendRequestService.deleteFriendRequest(friendRequest, currentUser);
        return ResponseEntity.ok().body("Deleted friend request id " + friendRequestDTO.getId());
    }
}
