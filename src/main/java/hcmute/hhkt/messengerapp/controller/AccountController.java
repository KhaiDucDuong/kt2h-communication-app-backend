package hcmute.hhkt.messengerapp.controller;

import hcmute.hhkt.messengerapp.Response.AccountActivationResponse;
import hcmute.hhkt.messengerapp.Response.LoginResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.domain.RefreshToken;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.Device;
import hcmute.hhkt.messengerapp.domain.enums.SpecialAuthority;
import hcmute.hhkt.messengerapp.dto.RegisterAccountDTO;
import hcmute.hhkt.messengerapp.dto.RegisterUserDTO;
import hcmute.hhkt.messengerapp.service.AccountService.IAccountService;
import hcmute.hhkt.messengerapp.service.RefreshToken.IRefreshTokenService;
import hcmute.hhkt.messengerapp.service.UserService.IUserService;
import hcmute.hhkt.messengerapp.util.SecurityUtil;
import hcmute.hhkt.messengerapp.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final Logger log = LoggerFactory.getLogger(AccountController.class);
    private final IAccountService accountService;
    private final IUserService userService;
    private final IRefreshTokenService refreshTokenService;
    private final SecurityUtil securityUtil;
    @Value("${jwt.refresh-token-expiration-in-seconds}")
    private long jwtRefreshTokenExpiration;
    private static final String abc ="abc";

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation code.
     */
    @PostMapping("/activate")
    @ApiMessage("Activated account successfully")
    public ResponseEntity<?> activateAccount(@RequestParam(value = "key") String key) {
        log.debug("Activating user for activation key {}", key);
        Optional<Account> account = accountService.activateRegistration(key);
        if (account.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessage.ACCOUNT_NOT_FOUND_BY_ACTIVATION_KEY);
        }
        return ResponseEntity.ok().body(AccountActivationResponse.builder()
                        .username(account.get().getUsername())
                        .status(account.get().getStatus().name())
                        .build());
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority(@accountAuthorityService.createAccountAuthority)")
    @Transactional
    @ApiMessage("Create account successfully")
    public ResponseEntity<?> createAccount(@Valid @RequestBody RegisterAccountDTO registerAccountDTO) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        log.debug("REST request to create account of user {}", email);
        User user = userService.findUserByEmail(email);
        if(user == null){
            throw new IllegalArgumentException(ExceptionMessage.USER_NOT_EXIST);
        }
        Account account = accountService.createAccount(registerAccountDTO, true);
        user = userService.setUserAccount(user, account);

        LoginResponse.UserLogin userLoginData = LoginResponse.UserLogin.fromUser(user);

        //create access token
        List<String> authorities = new ArrayList<String>();
        authorities.add(user.getRole().name());
        String accessToken = securityUtil.createAccessToken(authorities, user.getEmail());

        //create refresh token for user
        RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(user, Device.BROWSER); //Default device is browser for now
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

        log.debug("REST request to create account from user {} successfully", email);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(loginResponse);
    }

    @Service("accountAuthorityService")
    protected class AccountAuthorityService {

        public String getCreateAccountAuthority(){
            return SpecialAuthority.CREATE_ACCOUNT.name();
        }

    }
}
