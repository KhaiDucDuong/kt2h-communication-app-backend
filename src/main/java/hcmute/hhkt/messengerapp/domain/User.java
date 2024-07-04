package hcmute.hhkt.messengerapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hcmute.hhkt.messengerapp.domain.enums.SystemRole;
import hcmute.hhkt.messengerapp.domain.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="users")
public class User extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @Size(max = 100)
    @Column(name="image", nullable = false)
    private String image;

    @Size(max = 20)
    @Column(name="first_name", nullable = false)
    private String firstName;

    @Size(max = 20)
    @Column(name="last_name", nullable = false)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true, nullable = false)
    private String email;

    @Pattern(regexp="(^$|[0-9]{10})") //phone number length must be 10 and contains digits only
    @Column(length = 10, unique = true)
    private String phone;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.OFFLINE;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    @Builder.Default
    private SystemRole role = SystemRole.USER;

//    @JsonIgnore
//    @NotNull
//    @Size(min = 60, max = 60)
//    @Column(name = "password", length = 60, nullable = false)
//    private String password;
//
//    @Column(name = "is_activated")
//    @Builder.Default
//    private Boolean isActivated = true;
//
//    @Column(name = "is_banned")
//    @Builder.Default
//    private Boolean isBanned = false;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id")
    private List<RefreshToken> refreshTokens;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", nullable = false),
//            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false))
//    private List<Role> roles;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
}
