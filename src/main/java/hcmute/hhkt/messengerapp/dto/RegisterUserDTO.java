package hcmute.hhkt.messengerapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.hhkt.messengerapp.util.RegrexUtil;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterUserDTO {
    @Length(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    @NotBlank(message = "First name cannot be blank")
    @JsonProperty("first_name")
    private String firstName;

    @Length(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    @NotBlank(message = "Last name cannot be blank")
    @JsonProperty("last_name")
    private String lastName;

    @NotBlank(message = "Username cannot be blank")
    @Length(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
    @JsonProperty("username")
    private String username;

    @Email(message = "Email is invalid", regexp= RegrexUtil.emailRegrexRFC5322)
    @NotBlank(message = "Email cannot be blank")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Length(min = 8,message = "Password must be at least 8 characters")
    @JsonProperty("password")
    private String password;
}
