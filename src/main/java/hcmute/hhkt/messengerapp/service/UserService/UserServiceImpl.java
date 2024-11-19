package hcmute.hhkt.messengerapp.service.UserService;

import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.AccountStatus;
import hcmute.hhkt.messengerapp.domain.enums.UserCreationType;
import hcmute.hhkt.messengerapp.domain.enums.UserDefaultStatus;
import hcmute.hhkt.messengerapp.domain.enums.UserStatus;
import hcmute.hhkt.messengerapp.dto.RegisterUserDTO;
import hcmute.hhkt.messengerapp.dto.SearchUserDTO;
import hcmute.hhkt.messengerapp.dto.UserProfileDTO;
import hcmute.hhkt.messengerapp.repository.UserRepository;
import hcmute.hhkt.messengerapp.service.AccountService.AccountServiceImpl;
import hcmute.hhkt.messengerapp.service.AccountService.IAccountService;
import hcmute.hhkt.messengerapp.service.MailService.MailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final IAccountService accountService;
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
                .creationType(UserCreationType.GOOGLE_AUTH)
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
    public User findUserByOAuth2User(OAuth2User oAuth2User) {
        return userRepository.findUserByEmail(oAuth2User.getAttribute(OAUTH2_USER_EMAIL));
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

    @Override
    public User setUserAccount(User user, Account account) {
        if(user.getAccount() != null && user.getCreationType() == UserCreationType.GOOGLE_AUTH){
            throw new IllegalArgumentException(ExceptionMessage.ACCOUNT_EXISTS);
        }
        user.setAccount(account);
        return userRepository.save(user);
    }

    @Override
    @Async
    public void updateUserStatus(User user, UserStatus status) {
        user.setLastActivityAt(Instant.now());
        user.setStatus(status);
        userRepository.save(user);
    }

    @Override
    public User updateUserDefaultStatus(User user, UserDefaultStatus status) {
        user.setDefaultStatus(status);
        return userRepository.save(user);
    }

    @Override
    public User updateUserImg(User user, String imgPath) {
        user.setImage(imgPath);
        user.setLastActivityAt(Instant.now());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, UserProfileDTO userProfileDTO) {
        user.setFirstName(userProfileDTO.getFirstName());
        user.setLastName(userProfileDTO.getLastName());
        user.setLastActivityAt(Instant.now());
        return userRepository.save(user);
    }

    @Override
    public List<SearchUserDTO> findUserbyUsername (String username, String currentuserid) {
        return userRepository.findUserWithFriendStatusByUsername(username, UUID.fromString(currentuserid));
    }

    @Async
    @Scheduled(cron = "0 0 2 * * ?") //run at 2am every day
//    @Scheduled(fixedRate = 30000) //every 30 seconds - for testing
    public void removeUserWithoutAccountScheduler() {
        log.debug("Remove users without account scheduler starts {}", Date.from(Instant.now()));
        userRepository
                .findAllByAccountIsNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
                .forEach(user -> {
                    log.debug("Deleting  user without account {}", user.getEmail());
                    userRepository.delete(user);
                });
        log.debug("Remove users without account scheduler ends {}", Date.from(Instant.now()));
    }
}
