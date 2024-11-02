package hcmute.hhkt.messengerapp.controller;

import hcmute.hhkt.messengerapp.Response.FriendshipResponse;
import hcmute.hhkt.messengerapp.Response.LoginResponse;
import hcmute.hhkt.messengerapp.Response.RegisterUserResponse;
import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.domain.Friendship;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.dto.FriendshipDTO;
import hcmute.hhkt.messengerapp.dto.RegisterUserDTO;
import hcmute.hhkt.messengerapp.service.FriendshipService.FriendshipServiceImpl;
import hcmute.hhkt.messengerapp.service.UserService.UserServiceImpl;
import hcmute.hhkt.messengerapp.util.SecurityUtil;
import hcmute.hhkt.messengerapp.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
public class FriendshipController {
    private final Logger log = LoggerFactory.getLogger(FriendshipController.class);
    private final SecurityUtil securityUtil;
    private final UserServiceImpl userService;
    private final FriendshipServiceImpl friendshipService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('USER')")
    @ApiMessage("Fetched logged in user friend list successfully")
    public ResponseEntity<?> getCurrentUserFriendships(Pageable pageable) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        log.debug("REST request to get friendships of user {}", email);
        User currentUser = userService.findUserByEmail(email);
        ResultPaginationResponse response = friendshipService.findUserFriendList(currentUser, pageable);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasAnyAuthority('USER')")
    @ApiMessage("Deleted friendship successfully")
    public ResponseEntity<?> deleteFriendship(@Valid @RequestBody FriendshipDTO friendshipDTO) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        log.debug("REST request to get friendship {} of user {}", friendshipDTO.getFriendId(), email);
        User currentUser = userService.findUserByEmail(email);
        User userFriend = userService.findById(friendshipDTO.getFriendId());
        friendshipService.deleteFriendship(currentUser, userFriend);
        return ResponseEntity.ok().body(FriendshipResponse.generateFriendshipResponse(userFriend));
    }
}
