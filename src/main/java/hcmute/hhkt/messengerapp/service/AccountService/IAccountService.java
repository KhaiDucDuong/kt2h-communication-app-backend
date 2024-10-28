package hcmute.hhkt.messengerapp.service.AccountService;

import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.dto.RegisterAccountDTO;
import hcmute.hhkt.messengerapp.dto.RegisterUserDTO;

import java.util.Optional;

public interface IAccountService {
    Account findAccountByUserName(String username);
    Account createAccount(RegisterUserDTO registerUserDTO);
    Account createAccount(RegisterAccountDTO registerAccountDTO, boolean isActivated);
    Optional<Account> activateRegistration(String key);
    boolean removeUnactivatedAccount(Account account);
    String renewActivationCode(Account account);
}
