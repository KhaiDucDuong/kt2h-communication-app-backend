package vn.khaiduong.comiclibrary.util;

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
