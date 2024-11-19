package hcmute.hhkt.messengerapp.controller;

import hcmute.hhkt.messengerapp.Response.MessageResponse;
import hcmute.hhkt.messengerapp.Response.UserResponse;
import hcmute.hhkt.messengerapp.Response.UserStatusResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.Conversation;
import hcmute.hhkt.messengerapp.domain.Message;
import hcmute.hhkt.messengerapp.domain.enums.UserDefaultStatus;
import hcmute.hhkt.messengerapp.domain.enums.UserStatus;
import hcmute.hhkt.messengerapp.dto.MessageDTO;
import hcmute.hhkt.messengerapp.dto.UpdateStatusDTO;
import hcmute.hhkt.messengerapp.dto.UserProfileDTO;
import hcmute.hhkt.messengerapp.service.FirebaseService.FirebaseServiceImpl;
import hcmute.hhkt.messengerapp.service.FirebaseService.IFirebaseService;
import hcmute.hhkt.messengerapp.util.SecurityUtil;
import io.lettuce.core.dynamic.annotation.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.service.UserService.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import hcmute.hhkt.messengerapp.util.annotation.ApiMessage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

import static hcmute.hhkt.messengerapp.Response.UserResponse.fromUserList;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final IUserService userService;
    private final IFirebaseService firebaseService;
    private final String USER_PROFILE_IMG_FOLDER = "user_profiles/";

    @GetMapping("")
    @ApiMessage("Fetched all users")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getAllUsers(){
        log.debug("Api request to get all users");
        List<User> userList = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiMessage("Fetched user successfully")
    public ResponseEntity<?> getUserById(@PathVariable String id){
        log.debug("Api request to find user with id {}", id);
        UUID userId = UUID.fromString(id);
        User queryUser = userService.findById(userId);
        if(queryUser == null){
            throw new IllegalArgumentException(ExceptionMessage.USER_NOT_EXIST);
        }
        return ResponseEntity.status(HttpStatus.OK).body(UserResponse.fromUser(queryUser));
    }

    @GetMapping("/activityStatus/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    @ApiMessage("Fetched user activity status successfully")
    public ResponseEntity<?> getUserActivityStatusById(@PathVariable String id){
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        log.debug("Api request to find user activity status with id {} from {}", id, email);
        UUID userId = UUID.fromString(id);
        User user = userService.findById(userId);
        if(user == null){
            throw new IllegalArgumentException(ExceptionMessage.USER_NOT_EXIST);
        }

        UserStatusResponse response;
        if(user.getDefaultStatus() == UserDefaultStatus.ONLINE){ //if user's default status is online, use the current status
            response = UserStatusResponse.fromUser(user, user.getStatus());
        } else if(user.getDefaultStatus() == UserDefaultStatus.INVISIBLE){ //if user's default status is invisible, use the offline status
            response = UserStatusResponse.fromUser(user, UserStatus.OFFLINE);
        } else { //if user's default status is others (idle, DND), use the default status
            response = UserStatusResponse.fromUser(user, user.getDefaultStatus());
        }
        response.setLastActivityAt(null);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/image")
    @PreAuthorize("hasAnyAuthority('USER')")
    @ApiMessage("Update user's profile picture successfully")
    public ResponseEntity<?> updateUserProfilePic(@RequestParam("image") MultipartFile image) throws IOException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        log.debug("Api request to update user profile image from {}", email);
        User user = userService.findUserByEmail(email);
        if(user == null){
            throw new IllegalArgumentException(ExceptionMessage.USER_NOT_EXIST);
        }

        final String fileName = user.getId().toString();
        String imgPath = firebaseService.resizeAndUploadImage(image, fileName, USER_PROFILE_IMG_FOLDER);
        user = userService.updateUserImg(user, imgPath);
        return ResponseEntity.status(HttpStatus.OK).body(UserResponse.fromUser(user));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAnyAuthority('USER')")
    @ApiMessage("Update user's profile successfully")
    public ResponseEntity<?> updateUserProfile(@RequestBody UserProfileDTO userProfileDTO) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        log.debug("Api request to update user profile from {}", email);
        User user = userService.findUserByEmail(email);
        if(user == null){
            throw new IllegalArgumentException(ExceptionMessage.USER_NOT_EXIST);
        }

        user = userService.updateUser(user, userProfileDTO);
        return ResponseEntity.status(HttpStatus.OK).body(UserResponse.fromUser(user));
    }
}
