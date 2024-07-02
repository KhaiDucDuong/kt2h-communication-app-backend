package hcmute.hhkt.messengerapp.Exception;

public class TokenExpiredException extends Exception{
    public TokenExpiredException(String errorMessage) {
        super(errorMessage);
    }
}
