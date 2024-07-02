package hcmute.hhkt.messengerapp.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.Response.RestResponse;
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

    private final Logger log = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    private final ObjectMapper mapper;

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        this.delegate.commence(request, response, authException);

        log.error(">>> Processing access token error {} from request method {} at {}", authException.getMessage(), request.getMethod(), request.getRequestURL());
        response.setContentType("application/json; charset=UTF-8");
        RestResponse<Object> res = new RestResponse<Object>();

        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        res.setError(authException.getMessage());

        res.setMessage(authException instanceof InsufficientAuthenticationException ?
                ExceptionMessage.MISSING_TOKEN : ExceptionMessage.INVALID_TOKEN);

        mapper.writeValue(response.getWriter(), res);
    }
}
