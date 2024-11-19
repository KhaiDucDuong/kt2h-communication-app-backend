package hcmute.hhkt.messengerapp.service.UserService;

import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.UserDefaultStatus;
import hcmute.hhkt.messengerapp.domain.enums.UserStatus;
import hcmute.hhkt.messengerapp.dto.RegisterUserDTO;
import hcmute.hhkt.messengerapp.dto.SearchUserDTO;
import hcmute.hhkt.messengerapp.dto.UserProfileDTO;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    User createUser(RegisterUserDTO registerUserDTO) throws IllegalArgumentException;
    User createUser(OAuth2User oAuth2User) throws IllegalArgumentException;
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    User findUserByAccount(Account account);
    User findUserByOAuth2User(OAuth2User oAuth2User);
    List<User> getAllUsers();
    User findById(UUID id);
    void resendActivationEmail(User user);
    User setUserAccount(User user, Account account);
    void updateUserStatus(User user, UserStatus status);
    User updateUserDefaultStatus(User user, UserDefaultStatus status);

    User updateUserImg(User user, String imgPath);

    User updateUser(User user, UserProfileDTO userProfileDTO);

    List<SearchUserDTO> findUserbyUsername(String username, String currentId);
}
