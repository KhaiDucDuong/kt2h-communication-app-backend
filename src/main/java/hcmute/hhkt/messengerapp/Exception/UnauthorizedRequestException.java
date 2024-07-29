package hcmute.hhkt.messengerapp.Exception;

public class UnauthorizedRequestException extends RuntimeException{
    public UnauthorizedRequestException(String errorMessage) {
        super(errorMessage);
    }
}
