package hcmute.hhkt.messengerapp.service.AccountService;

import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.dto.RegisterUserDTO;
import hcmute.hhkt.messengerapp.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService{
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Account findAccountByUserName(String username) {
        return accountRepository.findAccountByUsername(username);
    }

    @Override
    public Account createAccount(RegisterUserDTO registerUserDTO) {
        if(accountRepository.existsAccountByUsername(registerUserDTO.getUsername())){
            throw new IllegalArgumentException(ExceptionMessage.USERNAME_IS_TAKEN);
        }

        String harshPassword = passwordEncoder.encode(registerUserDTO.getPassword());

        Account newAccount = Account.builder()
                .username(registerUserDTO.getUsername())
                .password(harshPassword)
                .build();
        return accountRepository.save(newAccount);
    }
}
