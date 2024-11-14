package hcmute.hhkt.messengerapp.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountActivationResponse {
    @JsonProperty("username")
    private String username;

    @JsonProperty("status")
    private String status;
}
