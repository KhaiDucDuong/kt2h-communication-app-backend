package hcmute.hhkt.messengerapp.service.AccountService;

import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.dto.RegisterUserDTO;

public interface IAccountService {
    Account findAccountByUserName(String username);
    Account createAccount(RegisterUserDTO registerUserDTO);
}
