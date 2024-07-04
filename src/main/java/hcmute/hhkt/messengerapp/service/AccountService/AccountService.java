package hcmute.hhkt.messengerapp.service.AccountService;

import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService{
    private final AccountRepository accountRepository;
    @Override
    public Account findAccountByUserName(String username) {
        return accountRepository.findAccountByUsername(username);
    }
}
