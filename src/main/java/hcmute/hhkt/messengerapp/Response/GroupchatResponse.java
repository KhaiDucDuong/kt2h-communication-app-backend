package hcmute.hhkt.messengerapp.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.hhkt.messengerapp.domain.Groupchat;
import hcmute.hhkt.messengerapp.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
public class GroupchatResponse {

        @JsonProperty("group_id")
        private String group_id;

        @JsonProperty("owner_id")
        private String owner_id;

        @JsonProperty("group_name")
        private String group_name;

        @JsonProperty("group_img")
        private String group_img;

    public static GroupchatResponse generateGroupchatResponse (Groupchat groupchat){
        return GroupchatResponse.builder()
                .group_id(groupchat.getGroupid().toString())
                .owner_id(groupchat.getOwner().getId().toString())
                .group_name(groupchat.getGroupname())
                .group_img(groupchat.getGroupIMG())
                .build();
    }
    public static List<GroupchatResponse> generateGroupchatListResponse(List<Groupchat> groupchat){
        return groupchat.stream().map(GroupchatResponse::generateGroupchatResponse).toList();
    }
}
