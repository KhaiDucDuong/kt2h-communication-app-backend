package vn.khaiduong.comiclibrary.controller;

import vn.khaiduong.comiclibrary.domain.User;
import vn.khaiduong.comiclibrary.service.UserService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.khaiduong.comiclibrary.util.annotation.ApiMessage;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("")
    @ApiMessage("Fetched all users")
    public ResponseEntity<?> getAllUsers(){
        List<User> userList = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }
}
