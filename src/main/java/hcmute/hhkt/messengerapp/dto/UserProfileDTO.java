package hcmute.hhkt.messengerapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Getter
@Setter
public class UserProfileDTO {
    @NotBlank(message = "First name cannot be blank")
    @Length(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Length(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    @JsonProperty("last_name")
    private String lastName;
}
