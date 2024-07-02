package hcmute.hhkt.messengerapp.service.UserService;

import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import hcmute.hhkt.messengerapp.domain.Authority;
import hcmute.hhkt.messengerapp.domain.Role;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Component("userDetailsService")
public class UserDetailsCustom implements UserDetailsService {
    private final IUserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        hcmute.hhkt.messengerapp.domain.User user = userService.findUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(ExceptionMessage.LOGIN_FAILED);
        }

        List<Authority> userAuthorities = user.getRoles().stream().map(Role::getAuthorities).toList().stream().flatMap(Collection::stream).toList();
        List<SimpleGrantedAuthority> userGrantedAuthorities = userAuthorities.stream().map(Authority::getName).map(SimpleGrantedAuthority::new).toList();

        return new User(
                user.getEmail(),
                user.getPassword(),
                user.getIsActivated(),
                true,
                true,
                !user.getIsBanned(),
                userGrantedAuthorities);
    }
}
