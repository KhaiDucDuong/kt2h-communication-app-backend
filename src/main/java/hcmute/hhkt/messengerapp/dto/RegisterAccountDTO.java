package hcmute.hhkt.messengerapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterAccountDTO {
    @NotBlank(message = "Username cannot be blank")
    @Length(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
    @JsonProperty("username")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Length(min = 8,message = "Password must be at least 8 characters")
    @JsonProperty("password")
    private String password;
}
