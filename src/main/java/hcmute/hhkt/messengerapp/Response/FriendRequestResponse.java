package hcmute.hhkt.messengerapp.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.hhkt.messengerapp.domain.FriendRequest;
import hcmute.hhkt.messengerapp.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class FriendRequestResponse {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("sender_id")
    private UUID senderId;

    @JsonProperty("sender_image")
    private String senderImage;

    @JsonProperty("sender_first_name")
    private String senderFirstName;

    @JsonProperty("sender_last_name")
    private String senderLastName;

    @JsonProperty("receiver_id")
    private UUID receiverId;

    @JsonProperty("receiver_image")
    private String receiverImage;

    @JsonProperty("receiver_first_name")
    private String receiverFirstName;

    @JsonProperty("receiver_last_name")
    private String receiverLastName;

    @JsonProperty("sent_date_time")
    private Instant sentDateTime;

    @JsonProperty("status")
    private String status;

    public static FriendRequestResponse generateFriendRequestResponse(FriendRequest friendRequest){
        User sender = friendRequest.getSender();
        User receiver = friendRequest.getReceiver();
        return FriendRequestResponse.builder()
                .id(friendRequest.getId())
                .senderId(sender.getId())
                .senderImage(sender.getImage())
                .senderFirstName(sender.getFirstName())
                .senderLastName(sender.getLastName())
                .receiverId(receiver.getId())
                .receiverImage(receiver.getImage())
                .receiverFirstName(receiver.getFirstName())
                .receiverLastName(receiver.getLastName())
                .sentDateTime(friendRequest.getCreatedDate())
                .status(friendRequest.getStatus().name())
                .build();
    }

    public static List<FriendRequestResponse> generateFriendRequestResponseList(List<FriendRequest> friendRequestList){
        return friendRequestList.stream().map(FriendRequestResponse::generateFriendRequestResponse).toList();
    }
}
