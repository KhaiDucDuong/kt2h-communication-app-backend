package duckhai.springsecurity.demo.service.UserService;

import duckhai.springsecurity.demo.domain.User;
import duckhai.springsecurity.demo.dto.UserDTO;

public interface UserService {
    User createUser(UserDTO userDTO);
}
