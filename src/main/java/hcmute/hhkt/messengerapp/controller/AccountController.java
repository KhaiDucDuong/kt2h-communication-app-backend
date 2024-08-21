package hcmute.hhkt.messengerapp.controller;

import hcmute.hhkt.messengerapp.Response.AccountActivationResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.service.AccountService.IAccountService;
import hcmute.hhkt.messengerapp.service.UserService.IUserService;
import hcmute.hhkt.messengerapp.util.annotation.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final Logger log = LoggerFactory.getLogger(AccountController.class);
    private final IAccountService accountService;

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation code.
     */
    @GetMapping("/activate")
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
}
