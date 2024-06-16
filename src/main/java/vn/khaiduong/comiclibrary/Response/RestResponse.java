package vn.khaiduong.comiclibrary.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {
    private int statusCode;
    private T error;
    private Object message;
    private T data;
}
