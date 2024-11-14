package hcmute.hhkt.messengerapp.service.AccountService;

import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.domain.enums.AccountStatus;
import hcmute.hhkt.messengerapp.dto.RegisterAccountDTO;
import hcmute.hhkt.messengerapp.dto.RegisterUserDTO;
import hcmute.hhkt.messengerapp.repository.AccountRepository;
import hcmute.hhkt.messengerapp.util.RandomUtil;
import hcmute.hhkt.messengerapp.util.RegrexUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService{
    private final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Account findAccountByUserName(String username) {
        return accountRepository.findAccountByUsername(username);
    }

    @Override
    public Account createAccount(RegisterUserDTO registerUserDTO) {
        RegisterAccountDTO registerAccountDTO = RegisterAccountDTO.builder()
                .username(registerUserDTO.getUsername())
                .password(registerUserDTO.getPassword())
                .build();

        return createAccount(registerAccountDTO, false);
    }

    @Override
    public Account createAccount(RegisterAccountDTO registerAccountDTO, boolean isActivated) {
        if(RegrexUtil.isEmail(registerAccountDTO.getUsername())){
            throw new IllegalArgumentException(ExceptionMessage.USERNAME_IS_EMAIL);
        }

        if(accountRepository.existsAccountByUsername(registerAccountDTO.getUsername())){
            throw new IllegalArgumentException(ExceptionMessage.USERNAME_IS_TAKEN);
        }

        String harshPassword = passwordEncoder.encode(registerAccountDTO.getPassword());

        Account newAccount = Account.builder()
                .username(registerAccountDTO.getUsername())
                .password(harshPassword)
                .activationKey(!isActivated ? RandomUtil.generateActivationKey() : null)
                .status(!isActivated ? AccountStatus.UNACTIVATED : AccountStatus.ACTIVATED)
                .build();
        return accountRepository.save(newAccount);
    }

    @Override
    public Optional<Account> activateRegistration(String key) {
        return accountRepository.findAccountByActivationKey(key).map(account -> {
            account.setStatus(AccountStatus.ACTIVATED);
            account.setActivationKey(null);
            log.debug("Activated account: {}", account);
            return accountRepository.save(account);
        });
    }

    @Override
    public boolean removeUnactivatedAccount(Account account) {
        if(account.getStatus() != AccountStatus.UNACTIVATED){
            return false;
        }
        log.debug("Delete unactivated account: {}", account);
        accountRepository.delete(account);
        accountRepository.flush();
        return true;
    }

    @Override
    public String renewActivationCode(Account account) {
        if(account.getStatus() != AccountStatus.UNACTIVATED){
            throw new IllegalArgumentException(ExceptionMessage.ACCOUNT_IS_NOT_UNACTIVATED);
        }
        account.setActivationKey(RandomUtil.generateActivationKey());
        return accountRepository.save(account).getActivationKey();
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired every day, at 01:00 (am).
     */
    @Async
    @Scheduled(cron = "0 0 1 * * ?")
//    @Scheduled(fixedRate = 30000) //every 30 seconds - for testing
    public void removeUnactivatedAccountsScheduler() {
        log.debug("Remove unactivated accounts scheduler starts {}", Date.from(Instant.now()));
        accountRepository
                .findAllByStatusAndCreatedDateBefore(AccountStatus.UNACTIVATED, Instant.now().minus(3, ChronoUnit.DAYS))
                .forEach(account -> {
                    log.debug("Deleting  unactivated account {}", account.getUsername());
                    accountRepository.delete(account);
                });
        log.debug("Remove unactivated accounts scheduler ends {}", Date.from(Instant.now()));
    }
}
