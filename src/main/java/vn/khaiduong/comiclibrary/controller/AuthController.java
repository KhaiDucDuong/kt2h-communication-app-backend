package vn.khaiduong.comiclibrary.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import vn.khaiduong.comiclibrary.Response.LoginResponse;
import vn.khaiduong.comiclibrary.configuration.SecurityConfiguration;
import vn.khaiduong.comiclibrary.domain.RefreshToken;
import vn.khaiduong.comiclibrary.domain.User;
import vn.khaiduong.comiclibrary.dto.LoginDTO;
import vn.khaiduong.comiclibrary.dto.RegisterUserDTO;
import vn.khaiduong.comiclibrary.service.RefreshToken.RefreshTokenService;
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
import vn.khaiduong.comiclibrary.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Value("${jwt.refresh-token-cookie-name}")
    private String refreshTokenCookieName;
    private final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.refresh-token-expiration-in-seconds}")
    private long jwtRefreshTokenExpiration;
    @PostMapping("/login")
    @ApiMessage("Login successfully")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDTO loginDTO) {
        log.debug("REST request to login with username : {}", loginDTO.getUsername());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(),
                loginDTO.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User userLogin = userService.findUserByUsername(loginDTO.getUsername());
        LoginResponse.UserLogin userLoginData = LoginResponse.UserLogin.builder()
                .userId(String.valueOf(userLogin.getId()))
                .email(userLogin.getEmail())
                .fullName(userLogin.getFullName())
                .build();

        //create access token
        String accessToken = securityUtil.createAccessToken(authentication, userLoginData);

        //create refresh token for user
        RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(userLogin, false); //isMobile is default to false for now
        String refreshTokenValue = refreshToken.getToken();

        LoginResponse loginResponse = LoginResponse.builder()
                .access_token(accessToken)
                .user(userLoginData)
                .build();

        //set refresh token in response cookie
        ResponseCookie responseCookie = ResponseCookie
                .from(refreshTokenCookieName, refreshTokenValue)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtRefreshTokenExpiration)
                //.domain()
                .build();

        log.debug("REST request to login with username {} successfully", loginDTO.getUsername());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(loginResponse);
    }

    @PostMapping("/register")
    @ApiMessage("Created account successfully")
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterUserDTO registerUserDTO) throws IllegalArgumentException {
        log.debug("REST request to register account with username : {}", registerUserDTO.getEmail());

        User newUser = userService.createUser(registerUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping("/getAccount")
    @ApiMessage("Fetched account successfully")
    public ResponseEntity<?> getAccount() {
        log.debug("REST request to get current account");
        String username = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User userLogin = userService.findUserByUsername(username);

        if(userLogin != null){
            LoginResponse.UserLogin userLoginData = LoginResponse.UserLogin.builder()
                    .userId(String.valueOf(userLogin.getId()))
                    .email(userLogin.getEmail())
                    .fullName(userLogin.getFullName())
                    .build();
            return ResponseEntity.ok().body(userLoginData);
        }

        return ResponseEntity.internalServerError().body(HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    @GetMapping("/refresh")
    @ApiMessage("Get user by refresh token successfully")
    public ResponseEntity<?> getUserByRefreshToken(@CookieValue(name = "refresh_token") String refreshToken) {
        return ResponseEntity.ok().body(null);
    }
}
