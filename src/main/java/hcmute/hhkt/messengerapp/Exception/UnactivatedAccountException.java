package hcmute.hhkt.messengerapp.Exception;

import lombok.Getter;

@Getter
public class UnactivatedAccountException extends RuntimeException {
    private final String unactivatedEmail;
    public UnactivatedAccountException(String message, String unactivatedEmail) {
        super(message);
        this.unactivatedEmail = unactivatedEmail;
    }
}
