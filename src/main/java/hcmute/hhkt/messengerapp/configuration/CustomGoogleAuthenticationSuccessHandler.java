package hcmute.hhkt.messengerapp.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.SpecialAuthority;
import hcmute.hhkt.messengerapp.dto.RegisterUserDTO;
import hcmute.hhkt.messengerapp.service.UserService.CustomOAuth2UserService;
import hcmute.hhkt.messengerapp.service.UserService.IUserService;
import hcmute.hhkt.messengerapp.service.UserService.UserServiceImpl;
import hcmute.hhkt.messengerapp.util.SecurityUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomGoogleAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String INCOMPLETE_AUTH_RESULT = "INCOMPLETE_REQUIRE_ACCOUNT_REGISTRATION";
    private static final String COMPLETE_AUTH_RESULT = "COMPLETE_AUTHENTICATION";
    @Autowired
    private IUserService userService;
    @Autowired
    private  SecurityUtil securityUtil;
    @Value("${front-end.website.url}")
    private String frontendURL;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = userService.findUserByOAuth2User(oAuth2User);
        if(user == null){
            user = userService.createUser(oAuth2User);
        }

        String targetUrl;
        List<String> authorities = new ArrayList<String>();
        if(user.getAccount() == null){
            authorities.add(SpecialAuthority.CREATE_ACCOUNT.name());
            String accessToken = securityUtil.createAccessToken(authorities, user.getEmail());
            targetUrl = String.format("%s/sign-in?token=%s&auth_result=%s", frontendURL, accessToken, INCOMPLETE_AUTH_RESULT);
        } else {
            authorities.add(user.getRole().toString());
            String accessToken = securityUtil.createAccessToken(authorities, user.getEmail());
            targetUrl = String.format("%s/sign-in?token=%s&auth_result=%s", frontendURL, accessToken, COMPLETE_AUTH_RESULT);
        }

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
