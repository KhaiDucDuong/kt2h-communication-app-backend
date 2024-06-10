package duckhai.springsecurity.demo.Exception;

public class InvalidParameterException extends Exception{
    public InvalidParameterException(String errorMessage) {
        super(errorMessage);
    }
}
