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

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final IUserService userService;

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
}
