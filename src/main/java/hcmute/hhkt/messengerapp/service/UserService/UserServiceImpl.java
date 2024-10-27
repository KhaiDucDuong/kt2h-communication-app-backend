package hcmute.hhkt.messengerapp.service.UserService;

import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.dto.RegisterUserDTO;
import hcmute.hhkt.messengerapp.repository.UserRepository;
import hcmute.hhkt.messengerapp.service.AccountService.AccountService;
import hcmute.hhkt.messengerapp.service.MailService.MailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final MailService mailService;
    private static final String OAUTH2_USER_FIRST_NAME = "given_name";
    private static final String OAUTH2_USER_LAST_NAME = "family_name";
    private static final String OAUTH2_USER_EMAIL = "email";
    private static final String OAUTH2_USER_IMAGE = "picture";

    @Transactional
    public User createUser(RegisterUserDTO registerUserDTO) throws IllegalArgumentException {

        if(userRepository.existsUserByEmail(registerUserDTO.getEmail())){
            throw new IllegalArgumentException(ExceptionMessage.EMAIL_IS_TAKEN);
        }

        Account newAccount = accountService.createAccount(registerUserDTO);
        User newUser = User.builder()
                .firstName(registerUserDTO.getFirstName())
                .lastName(registerUserDTO.getLastName())
                .email(registerUserDTO.getEmail())
                .account(newAccount)
                .build();

        return userRepository.save(newUser);
    }

    @Override
    @Transactional
    public User createUser(OAuth2User oAuth2User) throws IllegalArgumentException {
        RegisterUserDTO registerUserDTO = RegisterUserDTO.builder()
                .email(oAuth2User.getAttribute(OAUTH2_USER_EMAIL))
                .firstName(oAuth2User.getAttribute(OAUTH2_USER_FIRST_NAME))
                .lastName(oAuth2User.getAttribute(OAUTH2_USER_LAST_NAME))
                .build();

        if(userRepository.existsUserByEmail(registerUserDTO.getEmail())){
            throw new IllegalArgumentException(ExceptionMessage.EMAIL_IS_TAKEN);
        }

        User newUser = User.builder()
                .firstName(registerUserDTO.getFirstName())
                .lastName(registerUserDTO.getLastName())
                .email(registerUserDTO.getEmail())
                .account(null)
                .build();

        return userRepository.save(newUser);
    }

    @Override
    public User findUserByUsername(String username) {
        Account account = accountService.findAccountByUserName(username);
        if(account == null){
            return null;
        }

        return account.getUser();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User findUserByAccount(Account account) {
        return userRepository.findUserByAccount(account);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void resendActivationEmail(User user) {
        String activationCode = accountService.renewActivationCode(user.getAccount());
        mailService.sendActivationEmail(user, activationCode);
    }
}
