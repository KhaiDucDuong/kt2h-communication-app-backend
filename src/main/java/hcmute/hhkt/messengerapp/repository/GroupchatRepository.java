package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.Groupchat;
import hcmute.hhkt.messengerapp.domain.InvitationNotification;
import hcmute.hhkt.messengerapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GroupchatRepository extends JpaRepository<Groupchat, UUID>
{
        List<Groupchat> getGroupchatByOwnerId(UUID uuid);

        Groupchat getGroupchatByGroupid(UUID uuid);

        List<Groupchat> getGroupchatByGroupname(String Groupname);

}
