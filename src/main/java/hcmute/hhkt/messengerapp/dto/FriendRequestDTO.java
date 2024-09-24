package hcmute.hhkt.messengerapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FriendRequestDTO {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("sender_id")
    private UUID senderId;

    @JsonProperty("receiver_id")
    private UUID receiverId;

    @JsonProperty("receiver_username")
    private String receiverUsername;

    @JsonProperty("status")
    private String status;
}
