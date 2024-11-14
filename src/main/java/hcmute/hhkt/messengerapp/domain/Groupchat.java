package hcmute.hhkt.messengerapp.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="Groupchat")
public class Groupchat extends AbstractDateAuditingEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="groupid")
    private UUID groupid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @Column(name = "group_name", nullable = false, unique = false, columnDefinition = "TEXT")
    private String groupname;

    @Column(name = "group_image", nullable = false, unique = false, columnDefinition = "TEXT")
    private String groupIMG;

    @Column(name = "QRCode", nullable = false, unique = false, columnDefinition = "TEXT")
    private String QRcode;

    @ManyToMany(mappedBy = "groups")
    private Set<User> users;

}
