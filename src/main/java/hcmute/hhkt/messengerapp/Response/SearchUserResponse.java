package hcmute.hhkt.messengerapp.Response;
import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.hhkt.messengerapp.domain.Groupchat;
import hcmute.hhkt.messengerapp.dto.SearchUserDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class SearchUserResponse {
    @JsonProperty("user_id")
    private UUID user_id;

    @JsonProperty("user_name")
    private  String user_name;

    @JsonProperty("image")
    private String image;

    @JsonProperty("first_name")
    private  String first_name;

    @JsonProperty("last_name")
    private String last_name;

    @JsonProperty("is_Friend")
    private String is_Friend;

    public static SearchUserResponse GenerateSearchUserResponse(SearchUserDTO searchUser){
        return builder().user_id(searchUser.getUser_id())
                .user_name(searchUser.getUser_name())
                .image(searchUser.getImage())
                .first_name(searchUser.getFirst_name())
                .last_name(searchUser.getLast_name())
                .is_Friend(searchUser.getIs_Friend()).build();
    }
    public static List<SearchUserResponse> generateSearchUserListResponse(List<SearchUserDTO> SearchUserDTO){
        return SearchUserDTO.stream().map(SearchUserResponse::GenerateSearchUserResponse).toList();
    }

}
