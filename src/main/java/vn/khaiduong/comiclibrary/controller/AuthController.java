package vn.khaiduong.comiclibrary.controller;

import vn.khaiduong.comiclibrary.Response.LoginResponse;
import vn.khaiduong.comiclibrary.domain.User;
import vn.khaiduong.comiclibrary.dto.LoginDTO;
import vn.khaiduong.comiclibrary.dto.UserDTO;
import vn.khaiduong.comiclibrary.service.UserService.UserService;
import vn.khaiduong.comiclibrary.util.SecurityUtil;
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
import vn.khaiduong.comiclibrary.util.annotation.ApiMessage;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    @PostMapping("/login")
    @ApiMessage("Login successfully")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(),
                loginDTO.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //create token
        String access_token = securityUtil.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User userLogin = userService.findUserByUsername(loginDTO.getUsername());
        LoginResponse.UserLogin userLoginData = LoginResponse.UserLogin.builder()
                .userId(String.valueOf(userLogin.getId()))
                .email(userLogin.getEmail())
                .fullName(userLogin.getFullName())
                .build();

        LoginResponse loginResponse = LoginResponse.builder()
                .access_token(access_token)
                .user(userLoginData)
                .build();
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/register")
    @ApiMessage("Created account successfully")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO) throws IllegalArgumentException {
        User newUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
