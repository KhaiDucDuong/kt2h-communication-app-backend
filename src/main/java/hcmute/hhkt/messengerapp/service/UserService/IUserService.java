package hcmute.hhkt.messengerapp.service.UserService;

import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.dto.RegisterUserDTO;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    User createUser(RegisterUserDTO registerUserDTO) throws IllegalArgumentException;
    User createUser(OAuth2User oAuth2User) throws IllegalArgumentException;
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    User findUserByAccount(Account account);
    List<User> getAllUsers();
    User findById(UUID id);
    void resendActivationEmail(User user);
}
