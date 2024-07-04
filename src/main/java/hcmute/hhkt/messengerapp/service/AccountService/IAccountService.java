package hcmute.hhkt.messengerapp.service.AccountService;

import hcmute.hhkt.messengerapp.domain.Account;

public interface IAccountService {
    Account findAccountByUserName(String username);
}
