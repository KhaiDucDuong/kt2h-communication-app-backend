package hcmute.hhkt.messengerapp.service.UserService;

import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.domain.enums.AccountStatus;
import hcmute.hhkt.messengerapp.service.AccountService.IAccountService;
import hcmute.hhkt.messengerapp.util.RegrexUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@RequiredArgsConstructor
@Component("userDetailsService")
public class UserDetailsCustom implements UserDetailsService {
    private final IUserService userService;
    private final IAccountService accountService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        hcmute.hhkt.messengerapp.domain.User user;
        Account account;

        if(RegrexUtil.isEmail(username)){
            user = userService.findUserByEmail(username);
            if(user == null){
                throw new UsernameNotFoundException(ExceptionMessage.LOGIN_FAILED);
            }
            account = user.getAccount();
        } else {
            account = accountService.findAccountByUserName(username);
            if(account == null){
                throw new UsernameNotFoundException(ExceptionMessage.LOGIN_FAILED);
            }
            user = userService.findUserByAccount(account);
        }

        SimpleGrantedAuthority userRole = new SimpleGrantedAuthority(user.getRole().toString());

        boolean isAccountBanned = account.getStatus().equals(AccountStatus.BANNED);
        //boolean isAccountUnactivated = account.getStatus().equals(AccountStatus.UNACTIVATED);
        boolean isAccountDeactivated = account.getStatus().equals(AccountStatus.DEACTIVATED);

        return new User(
                username,
                account.getPassword(),
                !isAccountDeactivated,
//                true,
                true,
                true,
                !isAccountBanned,
                //userGrantedAuthorities);
                Collections.singleton(userRole));
    }
}
