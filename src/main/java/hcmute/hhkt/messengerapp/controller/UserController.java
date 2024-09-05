package hcmute.hhkt.messengerapp.controller;

import hcmute.hhkt.messengerapp.Response.UserResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.service.UserService.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import hcmute.hhkt.messengerapp.util.annotation.ApiMessage;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping("")
    @ApiMessage("Fetched all users")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getAllUsers(){
        List<User> userList = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @GetMapping("/{id}")
    @ApiMessage("Fetched user successfully")
    public ResponseEntity<?> getUserById(@PathVariable String id){
        UUID userId = UUID.fromString(id);
        User queryUser = userService.findById(userId);
        if(queryUser == null){
            throw new IllegalArgumentException(ExceptionMessage.USER_NOT_EXIST);
        }
        return ResponseEntity.status(HttpStatus.OK).body(UserResponse.fromUser(queryUser));
    }
}
