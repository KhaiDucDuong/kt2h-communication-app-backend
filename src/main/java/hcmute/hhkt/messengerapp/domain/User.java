package hcmute.hhkt.messengerapp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import hcmute.hhkt.messengerapp.domain.enums.SystemRole;
import hcmute.hhkt.messengerapp.domain.enums.UserCreationType;
import hcmute.hhkt.messengerapp.domain.enums.UserDefaultStatus;
import hcmute.hhkt.messengerapp.domain.enums.UserStatus;
import hcmute.hhkt.messengerapp.util.RegrexUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.List;
import java.util.Set;
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

    @Size(max = 254)
    @Column(name="image")
    private String image;

    @Size(min = 1, max = 50)
    @Column(name="first_name", nullable = false, length = 50)
    private String firstName;

    @Size(min = 1, max = 50)
    @Column(name="last_name", nullable = false, length = 50)
    private String lastName;

    @Email(regexp= RegrexUtil.emailRegrexRFC5322)
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true, nullable = false)
    private String email;

    @Pattern(regexp="(^$|[0-9]{10})") //phone number length must be 10 and contains digits only
    @Column(length = 10, unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.OFFLINE;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_status", length = 20, nullable = false)
    @Builder.Default
    private UserDefaultStatus defaultStatus = UserDefaultStatus.ONLINE;

    @LastModifiedDate
    @Column(name = "last_activity_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    @Builder.Default
    private Instant lastActivityAt = Instant.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "creation_type", length = 20, nullable = false)
    @Builder.Default
    private UserCreationType creationType = UserCreationType.SELF_REGISTRATION;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = true)
    @Builder.Default
    private SystemRole role = SystemRole.USER;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "receiver", orphanRemoval = true)
    private List<InvitationNotification> invitationNotifications;

    @ManyToMany
    @JoinTable(
            name = "groupchat_members",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Groupchat> groups;
}
