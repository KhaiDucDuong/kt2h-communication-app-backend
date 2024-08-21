package hcmute.hhkt.messengerapp.service.AccountService;

import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.dto.RegisterUserDTO;

import java.util.Optional;

public interface IAccountService {
    Account findAccountByUserName(String username);
    Account createAccount(RegisterUserDTO registerUserDTO);
    Optional<Account> activateRegistration(String key);
    boolean removeUnactivatedAccount(Account account);
}
