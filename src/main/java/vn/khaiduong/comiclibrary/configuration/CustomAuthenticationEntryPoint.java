package vn.khaiduong.comiclibrary.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import vn.khaiduong.comiclibrary.constant.ExceptionMessage;
import vn.khaiduong.comiclibrary.Response.RestResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();
    private final ObjectMapper mapper;

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        this.delegate.commence(request, response, authException);

        System.out.println(">>> Processing access token error: " + authException.getMessage());
        response.setContentType("application/json; charset=UTF-8");
        RestResponse<Object> res = new RestResponse<Object>();

        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        res.setError(authException.getMessage());

        res.setMessage(authException instanceof InsufficientAuthenticationException ?
                ExceptionMessage.MISSING_TOKEN : ExceptionMessage.INVALID_TOKEN);

        mapper.writeValue(response.getWriter(), res);
    }
}
