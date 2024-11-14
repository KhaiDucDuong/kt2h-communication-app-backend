package hcmute.hhkt.messengerapp.domain;


import hcmute.hhkt.messengerapp.domain.enums.ChannelType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="Channel")
public class Channel extends AbstractDateAuditingEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="channelid")
    private UUID chanenlid;

    @Column(name = "channel_name", nullable = false, unique = false, columnDefinition = "TEXT")
    private String channelname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "groupid")
    private Groupchat groupchat;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel_type", length = 20, nullable = false)
    private ChannelType channelType;

    @Column(name = "is_private")
    @Builder.Default
    private Boolean isPrivate = false;

}
