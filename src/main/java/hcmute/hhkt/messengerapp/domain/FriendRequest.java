package hcmute.hhkt.messengerapp.domain;

import hcmute.hhkt.messengerapp.domain.enums.FriendRequestStatus;
import hcmute.hhkt.messengerapp.domain.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="friend_requests")
public class FriendRequest extends AbstractDateAuditingEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    @Builder.Default
    private FriendRequestStatus status = FriendRequestStatus.PENDING;

    @OneToMany(mappedBy = "friendRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvitationNotification> invitationNotification;
}
