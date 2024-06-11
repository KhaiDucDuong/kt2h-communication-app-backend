package vn.khaiduong.comiclibrary.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
public class LoginResponse {
    @JsonProperty("access_token")
    private String access_token;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @Builder.Default
    @JsonProperty("token_type")
    private String tokenType = "Bearer";
}
