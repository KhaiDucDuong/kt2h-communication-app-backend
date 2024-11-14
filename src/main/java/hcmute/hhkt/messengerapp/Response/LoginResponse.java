package hcmute.hhkt.messengerapp.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.UserStatus;
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

        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;

        @JsonProperty("user_id")
        private String userId;

        @JsonProperty("image")
        private String image;

        @JsonProperty("phone")
        private String phone;

        @JsonProperty("role")
        private String role;

        @JsonProperty("status")
        private String status;

        @JsonProperty("default_status")
        private String defaultStatus;

        public static UserLogin fromUser(User user){
            return UserLogin.builder()
                    .userId(String.valueOf(user.getId()))
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .phone(user.getPhone())
                    .image(user.getImage())
                    .role(user.getRole().name())
                    .status(UserStatus.ONLINE.name())
                    .defaultStatus(user.getDefaultStatus().name())
                    .build();
        }
    }
}
