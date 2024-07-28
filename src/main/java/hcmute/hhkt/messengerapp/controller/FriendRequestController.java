package hcmute.hhkt.messengerapp.controller;

import hcmute.hhkt.messengerapp.Response.FriendRequestResponse;
import hcmute.hhkt.messengerapp.Response.LoginResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.FriendRequest;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.dto.FriendRequestDTO;
import hcmute.hhkt.messengerapp.service.FriendRequestService.FriendRequestServiceImpl;
import hcmute.hhkt.messengerapp.service.FriendshipService.FriendshipServiceImpl;
import hcmute.hhkt.messengerapp.service.UserService.UserServiceImpl;
import hcmute.hhkt.messengerapp.util.SecurityUtil;
import hcmute.hhkt.messengerapp.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friendRequests")
@RequiredArgsConstructor
public class FriendRequestController {
    private final Logger log = LoggerFactory.getLogger(FriendRequestController.class);
    private final UserServiceImpl userService;
    private final FriendRequestServiceImpl friendRequestService;
    private final FriendshipServiceImpl friendshipService;

    @PostMapping("")
    @ApiMessage("Created friend request successfully")
    public ResponseEntity<?> createFriendRequest(@Valid @RequestBody FriendRequestDTO friendRequestDTO) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        log.debug("REST request to create friend request to user Id {} from {}", friendRequestDTO.getReceiverId(), email);

        User senderUser = userService.findUserByEmail(email);
        User receiverUser = userService.findById(friendRequestDTO.getReceiverId());
        if (senderUser == null || receiverUser == null) {
            throw new IllegalArgumentException(ExceptionMessage.USER_NOT_EXIST);
        }
        if (friendshipService.existFriendshipById(senderUser, receiverUser)) {
            throw new IllegalArgumentException(ExceptionMessage.FRIENDSHIP_EXIST);
        }

        FriendRequest newFriendRequest = friendRequestService.sendFriendRequest(senderUser, receiverUser);

        return ResponseEntity.ok().body(FriendRequestResponse.generateFriendRequestResponse(newFriendRequest));
    }
}
