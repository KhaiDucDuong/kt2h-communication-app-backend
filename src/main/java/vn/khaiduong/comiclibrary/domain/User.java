package vn.khaiduong.comiclibrary.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @NotBlank(message = "Name cannot be blank")
    @Column(name="full_name")
    private String fullName;

    @Email(message = "Email is invalid")
    @Column(name="email")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Column(name="password")
    private String password;

    @Column(name="refresh_token", columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

//    @Column(name="created_at")
//    private Instant createdAt;
//
//    @Column(name="updated_at")
//    private Instant updatedAt;
//
//    @Column(name="created_by")
//    private String createdBy;
//
//    @Column(name="updated_by")
//    private String updatedBy;
}
