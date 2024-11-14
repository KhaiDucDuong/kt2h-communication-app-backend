package hcmute.hhkt.messengerapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    @JsonProperty("message_type")  // Thay đổi tên trường thành message_types
    private String messageType;  // Sử dụng List để lưu nhiều loại tin nhắn

    @JsonProperty("image_urls")
    private List<String> imageUrls;
}
