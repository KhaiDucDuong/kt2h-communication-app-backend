package hcmute.hhkt.messengerapp.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.hhkt.messengerapp.domain.Conversation;
import hcmute.hhkt.messengerapp.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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

    private ToUser toUser;

    private ResultPaginationResponse paginationResponse;

    @Builder
    public class ToUser {
        @JsonProperty("to_user_id")
        private UUID id;

        @JsonProperty("to_user_image")
        private String image;

        @JsonProperty("to_user_first_name")
        private String firstName;

        @JsonProperty("to_user_last_name")
        private String lastName;

        @JsonProperty("to_user_email")
        private String email;

        @JsonProperty("to_user_status")
        private String status;

        public static ToUser fromUser(User user){
            return ToUser.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .image(user.getImage())
                    .email(user.getEmail())
                    .status(user.getStatus().name())
                    .build();
        }
    }

    public static ConversationResponse fromConversation(Conversation conversation){
        return ConversationResponse.builder()
                .id(conversation.getId())
                .creatorId(conversation.getCreator().getId())
                .creatorNickname(conversation.getCreatorNickname())
                .targetId(conversation.getTarget().getId())
                .targetNickname(conversation.getTargetNickname())
                .build();
    }

    public static ConversationResponse fromConversation(Conversation conversation, User toUser){
        return ConversationResponse.builder()
                .id(conversation.getId())
                .creatorId(conversation.getCreator().getId())
                .creatorNickname(conversation.getCreatorNickname())
                .targetId(conversation.getTarget().getId())
                .targetNickname(conversation.getTargetNickname())
                .toUser(ToUser.fromUser(toUser))
                .build();
    }

    public static ConversationResponse fromConversation(Conversation conversation, User toUser, ResultPaginationResponse paginationResponse){
        return ConversationResponse.builder()
                .id(conversation.getId())
                .creatorId(conversation.getCreator().getId())
                .creatorNickname(conversation.getCreatorNickname())
                .targetId(conversation.getTarget().getId())
                .targetNickname(conversation.getTargetNickname())
                .toUser(ToUser.fromUser(toUser))
                .paginationResponse(paginationResponse)
                .build();
    }

    public static List<ConversationResponse> fromConversationList(List<Conversation> conversationList, List<User> toUserList){
        List<ConversationResponse> conversationResponseList = new ArrayList<>();
        for(int i = 0; i < conversationList.size(); i++){
            conversationResponseList.addLast(ConversationResponse.fromConversation(conversationList.get(i), toUserList.get(i)));
        }
        return conversationResponseList;
    }
}
