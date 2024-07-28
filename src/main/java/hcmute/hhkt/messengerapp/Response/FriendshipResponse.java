package hcmute.hhkt.messengerapp.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.hhkt.messengerapp.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class FriendshipResponse {
    @JsonProperty("friend_id")
    private UUID friendId;

    @JsonProperty("image")
    private String image;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    public static FriendshipResponse generateFriendshipResponse(User user){
        return FriendshipResponse.builder()
                .friendId(user.getId())
                .image(user.getImage())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public static List<FriendshipResponse> generateFriendshipListResponse(List<User> userList){
        return userList.stream().map(FriendshipResponse::generateFriendshipResponse).toList();
    }
}
