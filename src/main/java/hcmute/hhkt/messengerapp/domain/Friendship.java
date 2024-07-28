package hcmute.hhkt.messengerapp.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="friendships")
@IdClass(Friendship.FriendshipId.class)
public class Friendship extends AbstractDateAuditingEntity{
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", referencedColumnName = "id")
    private User friend;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FriendshipId implements Serializable {
        private User user;
        private User friend;
    }
}
