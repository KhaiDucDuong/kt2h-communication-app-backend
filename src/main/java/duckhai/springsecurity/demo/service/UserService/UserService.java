package duckhai.springsecurity.demo.service.UserService;

import duckhai.springsecurity.demo.Exception.InvalidParameterException;
import duckhai.springsecurity.demo.domain.User;
import duckhai.springsecurity.demo.dto.UserDTO;

import java.util.List;

public interface UserService {
    User createUser(UserDTO userDTO) throws IllegalArgumentException;
    User findUserByUsername(String username);

    List<User> getAllUsers();
}
