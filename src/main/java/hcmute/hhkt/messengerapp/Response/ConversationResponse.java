package hcmute.hhkt.messengerapp.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.hhkt.messengerapp.domain.Conversation;
import hcmute.hhkt.messengerapp.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ConversationResponse {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("requester_id")
    private UUID requesterId;

    @JsonProperty("requester_nickname")
    private String requesterNickname;

    private ToUser toUser;

    private ResultPaginationResponse paginationResponse;

    @Builder
    public static class ToUser {
        @JsonProperty("to_user_id")
        private UUID id;

        @JsonProperty("to_user_image")
        private String image;

        @JsonProperty("to_user_first_name")
        private String firstName;

        @JsonProperty("to_user_last_name")
        private String lastName;

        @JsonProperty("to_user_nickname")
        private String nickname;

        @JsonProperty("to_user_status")
        private String status;

        @JsonProperty("to_user_last_activity_at")
        private Instant toUserLastActivityAt;

        public static ToUser fromUser(User user, String nickname){
            return ToUser.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .nickname(nickname)
                    .image(user.getImage())
                    .status(user.getStatus().name())
                    .toUserLastActivityAt((user.getLastActivityAt()))
                    .build();
        }
    }

    public static ConversationResponse fromConversation(Conversation conversation, User toUser){
        boolean isToUserCreator = toUser == conversation.getCreator();
        ConversationResponse response = ConversationResponse.builder().id(conversation.getId()).build();
        if(isToUserCreator){
            response.setRequesterId(conversation.getTarget().getId());
            response.setRequesterNickname(conversation.getTargetNickname());
            response.setToUser(ToUser.fromUser(toUser, conversation.getCreatorNickname()));
        } else {
            response.setRequesterId(conversation.getCreator().getId());
            response.setRequesterNickname(conversation.getCreatorNickname());
            response.setToUser(ToUser.fromUser(toUser, conversation.getTargetNickname()));
        }
        return response;
    }

    public static ConversationResponse fromConversation(Conversation conversation, User toUser, ResultPaginationResponse paginationResponse){
        ConversationResponse response = ConversationResponse.fromConversation(conversation, toUser);
        response.setPaginationResponse(paginationResponse);
        return response;
    }

    public static List<ConversationResponse> fromConversationList(List<Conversation> conversationList, List<User> toUserList){
        List<ConversationResponse> conversationResponseList = new ArrayList<>();
        for(int i = 0; i < conversationList.size(); i++){
            conversationResponseList.add(ConversationResponse.fromConversation(conversationList.get(i), toUserList.get(i)));
        }
        return conversationResponseList;
    }
}
