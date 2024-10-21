package hcmute.hhkt.messengerapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class GroupchatDTO {
    @JsonProperty("group_id")
    private String group_id;

    @JsonProperty("owner_id")
    private String owner_id;

    @JsonProperty("group_name")
    private String group_name;

    @JsonProperty("group_img")
    private String group_img;

}
