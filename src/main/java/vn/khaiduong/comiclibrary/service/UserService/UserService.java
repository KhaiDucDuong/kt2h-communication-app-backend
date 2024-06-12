package vn.khaiduong.comiclibrary.service.UserService;

import vn.khaiduong.comiclibrary.domain.User;
import vn.khaiduong.comiclibrary.dto.UserDTO;

import java.util.List;

public interface UserService {
    User createUser(UserDTO userDTO) throws IllegalArgumentException;
    User findUserByUsername(String username);

    void updateUserRefreshToken(String username, String refreshToken);
    List<User> getAllUsers();
}
