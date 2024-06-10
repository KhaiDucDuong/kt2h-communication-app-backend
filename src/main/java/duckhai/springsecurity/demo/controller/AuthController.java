package duckhai.springsecurity.demo.controller;

import com.mysql.cj.log.Log;
import duckhai.springsecurity.demo.Exception.InvalidParameterException;
import duckhai.springsecurity.demo.Response.LoginResponse;
import duckhai.springsecurity.demo.domain.User;
import duckhai.springsecurity.demo.dto.LoginDTO;
import duckhai.springsecurity.demo.dto.UserDTO;
import duckhai.springsecurity.demo.service.UserService.UserService;
import duckhai.springsecurity.demo.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(),
                loginDTO.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //create token
        String access_token = securityUtil.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginResponse loginResponse = LoginResponse.builder()
                .access_token(access_token)
                .build();
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO) throws IllegalArgumentException {
        User newUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
