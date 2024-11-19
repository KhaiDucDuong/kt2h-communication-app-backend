package hcmute.hhkt.messengerapp.domain;

import jakarta.persistence.*;
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
@Table(name ="conversations")
public class Conversation extends AbstractDateAuditingEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @Size(min = 1, max = 50)
    @Column(name="creator_nickname")
    private String creatorNickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

    @Size(min = 1, max = 50)
    @Column(name="target_nickname")
    private String targetNickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", referencedColumnName = "id")
    private User target;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;
}
