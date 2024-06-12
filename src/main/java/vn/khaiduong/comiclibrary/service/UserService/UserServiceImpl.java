package vn.khaiduong.comiclibrary.service.UserService;

import org.apache.commons.lang3.StringUtils;
import vn.khaiduong.comiclibrary.constant.ExceptionMessage;
import vn.khaiduong.comiclibrary.domain.User;
import vn.khaiduong.comiclibrary.dto.UserDTO;
import vn.khaiduong.comiclibrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(UserDTO userDTO) throws IllegalArgumentException {
        if(userRepository.existsUserByEmail(userDTO.getEmail())){
            throw new IllegalArgumentException(ExceptionMessage.EMAIL_IS_TAKEN);
        }

        String harshPassword = passwordEncoder.encode(userDTO.getPassword());
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .password(harshPassword)
                .build();
        return userRepository.save(newUser);
    }

    @Override
    public User findUserByUsername(String username) {
        User queryUser = userRepository.findUserByEmail(username);
        if(queryUser != null){
            return queryUser;
        }

        return null;
    }

    @Override
    public void updateUserRefreshToken(String username, String refreshToken) {
        User queryUser = findUserByUsername(username);

        if(queryUser == null){
            throw new IllegalArgumentException(ExceptionMessage.USER_NOT_EXIST);
        }

        if(StringUtils.isBlank(refreshToken)){
            throw new IllegalArgumentException(ExceptionMessage.MISSING_TOKEN);
        }

        queryUser.setRefreshToken(refreshToken);
        userRepository.save(queryUser);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
