package vn.khaiduong.comiclibrary.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResultPaginationResponse {
    private Meta meta;
    private Object result;
}
