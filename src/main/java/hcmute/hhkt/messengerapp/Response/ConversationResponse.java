package hcmute.hhkt.messengerapp.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.hhkt.messengerapp.domain.Conversation;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ConversationResponse {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("creator_id")
    private UUID creatorId;

    @JsonProperty("creator_nickname")
    private String creatorNickname;

    @JsonProperty("target_id")
    private UUID targetId;

    @JsonProperty("target_nickname")
    private String targetNickname;

    public static ConversationResponse fromConversation(Conversation conversation){
        return ConversationResponse.builder()
                .id(conversation.getId())
                .creatorId(conversation.getCreator().getId())
                .creatorNickname(conversation.getCreatorNickname())
                .targetId(conversation.getTarget().getId())
                .targetNickname(conversation.getTargetNickname())
                .build();
    }

    public static List<ConversationResponse> fromConversationList(List<Conversation> conversationList){
        return conversationList.stream().map(ConversationResponse::fromConversation).toList();
    }
}
