package hcmute.hhkt.messengerapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
    @JsonProperty("sender_id")
    private String sender_id;

    @JsonProperty("conversation_id")
    private String conversation_id;

    @JsonProperty("message")
    private String message;

    @JsonProperty("message_type")
    private String messageType;
}
