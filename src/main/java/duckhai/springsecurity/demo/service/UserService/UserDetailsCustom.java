package duckhai.springsecurity.demo.service.UserService;

import duckhai.springsecurity.demo.constant.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Component("userDetailsService")
public class UserDetailsCustom implements UserDetailsService {
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        duckhai.springsecurity.demo.domain.User user = userService.findUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(ExceptionMessage.LOGIN_FAILED);
        }
        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
