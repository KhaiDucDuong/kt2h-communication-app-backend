package hcmute.hhkt.messengerapp.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomGoogleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Create the response body
        Map<String, Object> responseBody = new HashMap<>();
        //responseBody.put("token", jwtToken);
        responseBody.put("name", oAuth2User.getAttribute("name"));
        responseBody.put("email", oAuth2User.getAttribute("email"));
        responseBody.put("picture", oAuth2User.getAttribute("picture"));

        // Set response headers and content type
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        // Write the response body
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
