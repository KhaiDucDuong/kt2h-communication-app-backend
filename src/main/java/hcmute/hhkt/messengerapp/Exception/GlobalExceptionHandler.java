package hcmute.hhkt.messengerapp.Exception;

import hcmute.hhkt.messengerapp.Response.UnactivatedAccountLoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.Response.RestResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<Object> handleAnyException(Exception ex, HttpServletRequest request){
        log.error("Exception type {} occurred with message {} from request method {} at {}", ex.getClass(), ex.getMessage(),
                request.getMethod(), request.getRequestURL());
        if(ex instanceof IllegalArgumentException){
            return handleIllegalArgumentExceptions(ex);
        } else if (ex instanceof ConstraintViolationException){
            return handleConstraintViolationException((ConstraintViolationException) ex, request);
        } else if (ex instanceof MethodArgumentNotValidException){
            return handleMethodArgumentNotValidException((MethodArgumentNotValidException) ex, request);
        } else if (ex instanceof UsernameNotFoundException || ex instanceof BadCredentialsException){
            return handleFailedAuthenticationException(ex);
        } else if (ex instanceof UnactivatedAccountException) {
            return handleUnactivatedAccountException((UnactivatedAccountException) ex);
        } else if (ex instanceof HttpRequestMethodNotSupportedException){
            return handleHttpRequestMethodNotSupportedException(ex);
        } else if (ex instanceof PropertyReferenceException){
            return handlePropertyReferenceException(ex);
        } else if (ex instanceof TokenExpiredException){
            return handleTokenExpiredException(ex);
        } else if (ex instanceof MissingRequestCookieException){
            return handleMissingRequestCookieException(ex);
        } else if (ex instanceof AuthorizationDeniedException){
            return handleAuthorizationDeniedException(ex);
        } else if (ex instanceof UnauthorizedRequestException){
            return handleUnauthorizedRequestException(ex);
        } else if (ex instanceof DisabledException){
            return handleDisabledException(ex);
        } else if (ex instanceof IOException){
            return handleIOException(ex);
        }

        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setError(ex.getMessage());
        res.setMessage(HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }


    private ResponseEntity<Object> handleIllegalArgumentExceptions(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("Illegal Argument Exception");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    private ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        List<String> messages = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(messages);
        res.setMessage("Constraint Violation Exception");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    private ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> message = ex.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList();
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(message);
        res.setMessage("Method Argument Not Valid Exception");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    private ResponseEntity<Object> handleFailedAuthenticationException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        res.setError(ex.getMessage());
        res.setMessage(ExceptionMessage.LOGIN_FAILED);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }

    private ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setError(ex.getMessage());
        res.setMessage("Method Not Found Exception");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    private ResponseEntity<Object> handlePropertyReferenceException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("Property Reference Exception");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    private ResponseEntity<Object> handleTokenExpiredException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        res.setError(ex.getMessage());
        res.setMessage("Token Expired Exception");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }

    private ResponseEntity<Object> handleMissingRequestCookieException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("Missing Request Cookie Exception");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    private ResponseEntity<Object> handleAuthorizationDeniedException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.FORBIDDEN.value());
        res.setError(ex.getMessage());
        res.setMessage("Authorization Denied Exception");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
    }

    private ResponseEntity<Object> handleUnauthorizedRequestException(Exception ex){
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.FORBIDDEN.value());
        res.setError(ex.getMessage());
        res.setMessage("Unauthorized Request Exception");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
    }

    private ResponseEntity<Object> handleDisabledException(Exception ex){
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.FORBIDDEN.value());
        res.setError(ex.getMessage());
        res.setMessage("Account is disabled");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
    }

    private ResponseEntity<Object> handleUnactivatedAccountException(UnactivatedAccountException ex){
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.FORBIDDEN.value());
        res.setError(ex.getMessage());
        res.setMessage("Unactivated Account");
        res.setData(new UnactivatedAccountLoginResponse(ex.getUnactivatedEmail()));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
    }

    private ResponseEntity<Object> handleIOException(Exception ex){
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("Error processing files");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
