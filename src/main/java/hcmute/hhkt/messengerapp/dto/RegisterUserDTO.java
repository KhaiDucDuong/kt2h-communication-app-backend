package hcmute.hhkt.messengerapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class RegisterUserDTO {
    @NotBlank(message = "Name cannot be blank")
    @JsonProperty("full_name")
    private String fullName;

    @Email(message = "Email is invalid")
    @NotBlank(message = "Email cannot be blank")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Length(min = 8,message = "Password must be at least 8 characters")
    @JsonProperty("password")
    private String password;
}
