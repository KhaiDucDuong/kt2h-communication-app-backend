package hcmute.hhkt.messengerapp.service.UserService;

import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.domain.enums.AccountStatus;
import hcmute.hhkt.messengerapp.service.AccountService.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import hcmute.hhkt.messengerapp.domain.Authority;
import hcmute.hhkt.messengerapp.domain.Role;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Component("userDetailsService")
public class UserDetailsCustom implements UserDetailsService {
    private final IUserService userService;
    private final IAccountService accountService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //hcmute.hhkt.messengerapp.domain.User user = userService.findUserByUsername(username);
        Account account = accountService.findAccountByUserName(username);
        if(account == null){
            throw new UsernameNotFoundException(ExceptionMessage.LOGIN_FAILED);
        }

//        List<Authority> userAuthorities = user.getRoles().stream().map(Role::getAuthorities).toList().stream().flatMap(Collection::stream).toList();
//        List<SimpleGrantedAuthority> userGrantedAuthorities = userAuthorities.stream().map(Authority::getName).map(SimpleGrantedAuthority::new).toList();

        SimpleGrantedAuthority userRole = new SimpleGrantedAuthority("ROLE_USER");

        boolean isAccountBanned = account.getStatus().equals(AccountStatus.BANNED);
        boolean isAccountUnactivated = account.getStatus().equals(AccountStatus.UNACTIVATED);

        return new User(
                account.getUsername(),
                account.getPassword(),
                //!isAccountUnactivateed,
                true,
                true,
                true,
                !isAccountBanned,
                //userGrantedAuthorities);
                Collections.singleton(userRole));
    }
}
