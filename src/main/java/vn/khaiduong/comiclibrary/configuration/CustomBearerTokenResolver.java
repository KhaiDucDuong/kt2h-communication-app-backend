package vn.khaiduong.comiclibrary.configuration;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class CustomBearerTokenResolver implements BearerTokenResolver {
    @Value("${jwt.refresh-token-cookie-name}")
    private String refreshTokenCookieName;
    @Override
    public String resolve(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, refreshTokenCookieName);
        if (cookie != null)
            return cookie.getValue();
        else
            return new DefaultBearerTokenResolver().resolve(request);
    }
}
