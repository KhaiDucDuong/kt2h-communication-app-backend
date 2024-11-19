package hcmute.hhkt.messengerapp.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserDTO {
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



}
