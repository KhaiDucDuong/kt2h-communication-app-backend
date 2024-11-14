package hcmute.hhkt.messengerapp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.hhkt.messengerapp.domain.enums.AccountStatus;
import hcmute.hhkt.messengerapp.domain.enums.InvitationNotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="invitation_notifications")
public class InvitationNotification extends AbstractAuditingEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50, nullable = false)
    private InvitationNotificationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_request_id", referencedColumnName = "id")
    private FriendRequest friendRequest;

    @Column(name="is_deleted")
    @Builder.Default
    private boolean isDeleted = false;
}
