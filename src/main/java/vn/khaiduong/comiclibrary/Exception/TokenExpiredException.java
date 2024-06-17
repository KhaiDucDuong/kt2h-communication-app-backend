package vn.khaiduong.comiclibrary.Exception;

public class TokenExpiredException extends Exception{
    public TokenExpiredException(String errorMessage) {
        super(errorMessage);
    }
}
