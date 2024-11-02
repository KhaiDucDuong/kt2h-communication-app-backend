package hcmute.hhkt.messengerapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
    @JsonProperty("id")
    private String id;

    @JsonProperty("sender_id")
    private String senderId;

    @JsonProperty("conversation_id")
    private String conversationId;

    @JsonProperty("channel_id")
    private String channelId;

    @JsonProperty("message")
    private String message;

    @JsonProperty("message_type")
    private String messageType;

    @JsonProperty("image_url")
    private String imageUrl;
}
