package duckhai.springsecurity.demo.controller;

import duckhai.springsecurity.demo.domain.User;
import duckhai.springsecurity.demo.dto.UserDTO;
import duckhai.springsecurity.demo.service.UserService.UserService;
import duckhai.springsecurity.demo.service.UserService.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDTO userDTO){
        User newUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
