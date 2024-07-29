package hcmute.hhkt.messengerapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FriendshipDTO {
    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("friend_id")
    private UUID friendId;
}
