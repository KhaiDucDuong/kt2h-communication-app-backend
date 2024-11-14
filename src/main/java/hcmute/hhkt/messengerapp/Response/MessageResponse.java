package hcmute.hhkt.messengerapp.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.hhkt.messengerapp.domain.Message;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class MessageResponse {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("message")
    private String message;
    @JsonProperty("sender_id")
    private UUID senderId;
    @JsonProperty("message_type")
    private String messageType;
    @JsonProperty("is_reacted")
    private Boolean isReacted;
    @JsonProperty("sent_at")
    private Instant sentAt;
    @JsonProperty("image_urls")
    private List<String> imageUrls;

    public static MessageResponse fromMessage(Message message){
        return MessageResponse.builder()
                .id(message.getId())
                .message(message.getMessage())
                .senderId(message.getSender().getId())
                .messageType(message.getMessageType().name())
                .isReacted(message.getIsReacted())
                .sentAt(message.getCreatedDate())
                .imageUrls(message.getImageUrls())
                .build();
    }

    public static List<MessageResponse> fromMessageList(List<Message> messageList){
        return messageList.stream().map(MessageResponse::fromMessage).toList();
    }
}
