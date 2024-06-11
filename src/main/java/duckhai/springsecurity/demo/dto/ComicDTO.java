package duckhai.springsecurity.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComicDTO {
    @NotBlank(message = "Comic cannot be blank")
    @JsonProperty("name")
    private String name;
}
