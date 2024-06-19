package vn.khaiduong.comiclibrary.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import vn.khaiduong.comiclibrary.Exception.TokenExpiredException;
import vn.khaiduong.comiclibrary.Response.LoginResponse;
import vn.khaiduong.comiclibrary.domain.Authority;
import vn.khaiduong.comiclibrary.domain.RefreshToken;
import vn.khaiduong.comiclibrary.domain.Role;
import vn.khaiduong.comiclibrary.domain.User;
import vn.khaiduong.comiclibrary.dto.LoginDTO;
import vn.khaiduong.comiclibrary.dto.RegisterUserDTO;
import vn.khaiduong.comiclibrary.service.RefreshToken.IRefreshTokenService;
import vn.khaiduong.comiclibrary.service.UserService.IUserService;
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

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final IUserService userService;
    private final IRefreshTokenService refreshTokenService;

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
        List<Authority> userAuthorities = userLogin.getRoles().stream().map(Role::getAuthorities).toList().stream().flatMap(Collection::stream).toList();

        LoginResponse.UserLogin userLoginData = LoginResponse.UserLogin.builder()
                .userId(String.valueOf(userLogin.getId()))
                .email(userLogin.getEmail())
                .fullName(userLogin.getFullName())
                .build();

        //create access token
        String accessToken = securityUtil.createAccessToken(authentication, userAuthorities);

        //create refresh token for user
        RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(userLogin, false); //isMobile is default to false for now
        String refreshTokenValue = refreshToken.getToken();

        LoginResponse loginResponse = LoginResponse.builder()
                .access_token(accessToken)
                .user(userLoginData)
                .build();

        //set refresh token in response cookie
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", refreshTokenValue)
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
    public ResponseEntity<?> getUserByRefreshToken(@CookieValue(name = "refresh_token") String refreshToken) throws TokenExpiredException {
        log.debug("REST request to refresh token");

        //recycle refresh token
        RefreshToken newRefreshToken = refreshTokenService.recycleRefreshToken(refreshToken);
        User currentUser = newRefreshToken.getUser();
        String refreshTokenValue = newRefreshToken.getToken();

        LoginResponse.UserLogin userLoginData = LoginResponse.UserLogin.builder()
                .userId(String.valueOf(currentUser.getId()))
                .email(currentUser.getEmail())
                .fullName(currentUser.getFullName())
                .build();

        //create access token
        List<Authority> userAuthorities = currentUser.getRoles().stream()
                .map(Role::getAuthorities).toList().stream().flatMap(Collection::stream).toList();
        String accessToken = securityUtil.createAccessToken(SecurityUtil.getAuthentication(), userAuthorities);

        LoginResponse loginResponse = LoginResponse.builder()
                .access_token(accessToken)
                .user(userLoginData)
                .build();

        //set refresh token in response cookie
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", refreshTokenValue)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtRefreshTokenExpiration)
                //.domain()
                .build();

        log.debug("REST request to refresh token with username {} successfully", currentUser);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(loginResponse);
    }

    @PostMapping("/logout")
    @ApiMessage("Logged user out successfully")
    public ResponseEntity<?> logoutUser(@CookieValue(name = "refresh_token") String refreshToken) throws TokenExpiredException {
        log.debug("REST request to logout");

        RefreshToken newRefreshToken = refreshTokenService.invalidateToken(refreshToken);
        User loggedOutUser = newRefreshToken.getUser();

        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                //.domain()
                .build();

        log.debug("REST request to refresh token with username {} successfully", loggedOutUser.getEmail());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body("Logout user " + loggedOutUser.getEmail());
    }
}
