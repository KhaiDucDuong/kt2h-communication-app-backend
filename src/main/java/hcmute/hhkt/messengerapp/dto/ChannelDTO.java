package hcmute.hhkt.messengerapp.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ChannelDTO {
    @JsonProperty("channel_id")
    private String channel_id;

    @JsonProperty("channel_name")
    private String channel_name;

    @JsonProperty("channel_type")
    private String channel_type;

    @JsonProperty("group_id")
    private String group_id;

    @JsonProperty("is_private")
    private Boolean is_private;
}
