package hcmute.hhkt.messengerapp.controller;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import hcmute.hhkt.messengerapp.Exception.UnactivatedAccountException;
import hcmute.hhkt.messengerapp.Response.RegisterUserResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.*;
import hcmute.hhkt.messengerapp.domain.enums.AccountStatus;
import hcmute.hhkt.messengerapp.domain.enums.Device;
import hcmute.hhkt.messengerapp.domain.enums.SpecialAuthority;
import hcmute.hhkt.messengerapp.domain.enums.UserStatus;
import hcmute.hhkt.messengerapp.service.AccountService.IAccountService;
import hcmute.hhkt.messengerapp.service.MailService.MailService;
import hcmute.hhkt.messengerapp.util.RegrexUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import hcmute.hhkt.messengerapp.Exception.TokenExpiredException;
import hcmute.hhkt.messengerapp.Response.LoginResponse;
import hcmute.hhkt.messengerapp.dto.GoogleAuthorizationDTO;
import hcmute.hhkt.messengerapp.dto.LoginDTO;
import hcmute.hhkt.messengerapp.dto.RegisterUserDTO;
import hcmute.hhkt.messengerapp.service.RefreshToken.IRefreshTokenService;
import hcmute.hhkt.messengerapp.service.UserService.IUserService;
import hcmute.hhkt.messengerapp.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import hcmute.hhkt.messengerapp.util.annotation.ApiMessage;

import java.util.ArrayList;
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
    private final IAccountService accountService;
    private final IRefreshTokenService refreshTokenService;
    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final MailService mailService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecrect;

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
        Account loggedInAccount = null;
        User loggedInUser = null;
        if(RegrexUtil.isEmail(loginDTO.getUsername())){
            loggedInUser = userService.findUserByEmail(loginDTO.getUsername());
            loggedInAccount = loggedInUser.getAccount();
        } else {
            loggedInAccount = accountService.findAccountByUserName(loginDTO.getUsername());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        if(loggedInUser == null){
            loggedInUser = loggedInAccount.getUser();
        }

        //check if account is activated
        if(loggedInAccount.getStatus() == AccountStatus.UNACTIVATED) {
            throw new UnactivatedAccountException(ExceptionMessage.UNACTIVATED_ACCOUNT, loggedInUser.getEmail());
        }

        LoginResponse.UserLogin userLoginData = LoginResponse.UserLogin.fromUser(loggedInUser);

        //create access token
        String accessToken = securityUtil.createAccessToken(authentication, loggedInUser.getEmail());

        //create refresh token for user
        RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(loggedInUser, Device.BROWSER); //Default device is browser for now
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

        userService.updateUserStatus(loggedInUser, UserStatus.ONLINE);

        log.debug("REST request to login with username {} successfully", loginDTO.getUsername());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(loginResponse);
    }

    @PostMapping("/register")
    @ApiMessage("Created account successfully")
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterUserDTO registerUserDTO) throws IllegalArgumentException {
        log.debug("REST request to register account with username : {}", registerUserDTO.getUsername());

        User newUser = userService.createUser(registerUserDTO);
        mailService.sendActivationEmail(newUser, newUser.getAccount().getActivationKey());
        RegisterUserResponse responseData = RegisterUserResponse.createResponse(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    @GetMapping("/getAccount")
    @PreAuthorize("hasAnyAuthority('USER', @authAuthorityService.getGetAccountAuthority())")
    @ApiMessage("Fetched account successfully")
    public ResponseEntity<?> getAccount() {
        log.debug("REST request to get current account");
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User loggedInUser = userService.findUserByEmail(email);

        if (loggedInUser == null) {
            throw new IllegalArgumentException(ExceptionMessage.USER_NOT_EXIST);
        }

        LoginResponse.UserLogin userLoginData = LoginResponse.UserLogin.fromUser(loggedInUser);

        //create access token
        List<String> authorities = new ArrayList<String>();
        authorities.add(loggedInUser.getRole().name());
        String accessToken = securityUtil.createAccessToken(authorities, loggedInUser.getEmail());

        //create refresh token for user
        RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(loggedInUser, Device.BROWSER); //Default device is browser for now
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

        userService.updateUserStatus(loggedInUser, UserStatus.ONLINE);

        log.debug("REST request to get current account from user {} successfully", email);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(loginResponse);
    }

    @GetMapping("/refresh")
    @ApiMessage("Get user by refresh token successfully")
    public ResponseEntity<?> getUserByRefreshToken(@CookieValue(name = "refresh_token") String refreshToken,
                                                   HttpServletRequest request) throws TokenExpiredException {
        log.debug("REST request to refresh token");
        Device userDevice;
        if(request.getHeader("User-Agent").contains("Mobi")) {
            //mobile device
            userDevice = Device.MOBILE;
        } else {
            //desktop device
            userDevice = Device.BROWSER;
        }

        //recycle refresh token
        RefreshToken newRefreshToken = refreshTokenService.recycleRefreshToken(refreshToken, userDevice);

        Account currentAccount = newRefreshToken.getAccount();
        User user = currentAccount.getUser();

        String refreshTokenValue = newRefreshToken.getToken();

        LoginResponse.UserLogin userLoginData = LoginResponse.UserLogin.builder()
                .userId(String.valueOf(user.getId()))
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .image(user.getImage())
                .role(user.getRole().name())
                .build();

        //create access token
//        List<Authority> userAuthorities = currentUser.getRoles().stream()
//                .map(Role::getAuthorities).toList().stream().flatMap(Collection::stream).toList();
        List<String> authorities = new ArrayList<String>();
        authorities.add(user.getRole().name());
        String accessToken = securityUtil.createAccessToken(authorities, user.getEmail());

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

        log.debug("REST request to refresh token with username {} successfully", currentAccount.getUsername());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(loginResponse);
    }

    @PostMapping("/logout")
    @ApiMessage("Logged user out successfully")
    public ResponseEntity<?> logoutUser(@CookieValue(name = "refresh_token") String refreshToken) throws TokenExpiredException {
        log.debug("REST request to logout");

        RefreshToken newRefreshToken = refreshTokenService.invalidateToken(refreshToken);
        Account loggedOutAccount = newRefreshToken.getAccount();

        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                //.domain()
                .build();

        log.debug("REST request to log out username {} successfully", loggedOutAccount.getUsername());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body("Logout user " + loggedOutAccount.getUsername());
    }

    @PostMapping("/renewActivationCode")
    public ResponseEntity<?> renewActivationCode(@RequestParam(value = "email") String email){
        User user = userService.findUserByEmail(email);
        if(user == null){
            throw new IllegalArgumentException(ExceptionMessage.USER_NOT_EXIST);
        }
        userService.resendActivationEmail(user);
        return ResponseEntity.ok().body("Renew user activation code successfully");
    }

    @Service("authAuthorityService")
    protected class AuthAuthorityService {
        public String getGetAccountAuthority(){
            return SpecialAuthority.GET_CURRENT_USER.name();
        }
    }
}
