package hcmute.hhkt.messengerapp.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.UserDefaultStatus;
import hcmute.hhkt.messengerapp.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
public class UserStatusResponse {
    @JsonProperty("user_id")
    private UUID id;

    @JsonProperty("status")
    private String status;

    @JsonProperty("last_activity_at")
    private Instant lastActivityAt;

    public static UserStatusResponse fromUser(User user, UserStatus newStatus){
        return UserStatusResponse.builder()
                .id(user.getId())
                .status(newStatus.name())
                .lastActivityAt(user.getLastActivityAt())
                .build();
    }

    public static UserStatusResponse fromUser(User user, UserDefaultStatus newStatus){
        return UserStatusResponse.builder()
                .id(user.getId())
                .status(newStatus.name())
                .lastActivityAt(user.getLastActivityAt())
                .build();
    }
}
