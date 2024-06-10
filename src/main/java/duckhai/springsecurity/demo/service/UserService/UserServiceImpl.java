package duckhai.springsecurity.demo.service.UserService;

import duckhai.springsecurity.demo.domain.User;
import duckhai.springsecurity.demo.dto.UserDTO;
import duckhai.springsecurity.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(UserDTO userDTO){
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
}
