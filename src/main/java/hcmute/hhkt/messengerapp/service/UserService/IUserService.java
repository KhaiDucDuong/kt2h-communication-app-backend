package hcmute.hhkt.messengerapp.service.UserService;

import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.dto.RegisterUserDTO;

import java.util.List;

public interface IUserService {
    User createUser(RegisterUserDTO registerUserDTO) throws IllegalArgumentException;
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    User findUserByAccount(Account account);

    //void updateUserRefreshToken(String username, String refreshToken);
    List<User> getAllUsers();
}
