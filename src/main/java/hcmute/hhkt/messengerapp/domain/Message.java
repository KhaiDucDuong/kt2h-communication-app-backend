package hcmute.hhkt.messengerapp.domain;

import hcmute.hhkt.messengerapp.domain.enums.MessageType;
import hcmute.hhkt.messengerapp.domain.enums.SystemRole;
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
@Table(name ="messages")
public class Message extends AbstractDateAuditingEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", referencedColumnName = "id")
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", referencedColumnName = "channelid")
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", length = 20, nullable = false)
    private MessageType messageType;

    @Column(name = "message", unique = false, columnDefinition = "TEXT")
    private String message;

    @ElementCollection
    @Column(name = "image_urls", columnDefinition = "TEXT")
    private List<String> imageUrls;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "is_reacted")
    @Builder.Default
    private Boolean isReacted = false;
}
