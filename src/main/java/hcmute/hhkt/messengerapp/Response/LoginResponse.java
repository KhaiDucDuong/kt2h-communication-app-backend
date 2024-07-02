package hcmute.hhkt.messengerapp.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
public class LoginResponse {
    @JsonProperty("access_token")
    private String access_token;

    @Builder.Default
    @JsonProperty("token_type")
    private String tokenType = "Bearer";

    @JsonProperty("user")
    private UserLogin user;

    @Getter
    @Setter
    @Builder
    public static class UserLogin {
        @JsonProperty("email")
        private String email;

        @JsonProperty("full_name")
        private String fullName;

        @JsonProperty("user_id")
        private String userId;
    }
}
