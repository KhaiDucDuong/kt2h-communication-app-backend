package duckhai.springsecurity.demo.service.UserService;

import duckhai.springsecurity.demo.domain.User;
import duckhai.springsecurity.demo.dto.UserDTO;
import duckhai.springsecurity.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public User createUser(UserDTO userDTO){
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();
        return userRepository.save(newUser);
    }
}
