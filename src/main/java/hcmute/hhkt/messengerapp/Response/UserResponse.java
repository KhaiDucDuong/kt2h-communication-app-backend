package hcmute.hhkt.messengerapp.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.hhkt.messengerapp.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;
import java.util.List;

@Getter
@Setter
@Builder
public class UserResponse {
    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("image")
    private String image;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("status")
    private String status;

    @JsonProperty("last_activity_at")
    private Instant lastActivityAt;

    public static UserResponse fromUser(User user){
        return UserResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .image(user.getImage())
                .email(user.getEmail())
                .status(user.getStatus().name())
                .lastActivityAt(user.getLastActivityAt())
                .build();
    }

    public static List<UserResponse> fromUserList(List<User> userList){
        return userList.stream().map(UserResponse::fromUser).toList();
    }
}
