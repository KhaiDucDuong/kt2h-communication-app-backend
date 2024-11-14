package hcmute.hhkt.messengerapp.repository;
import hcmute.hhkt.messengerapp.domain.Channel;
import hcmute.hhkt.messengerapp.domain.Conversation;
import hcmute.hhkt.messengerapp.domain.Groupchat;
import hcmute.hhkt.messengerapp.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface ChannelRepository extends JpaRepository<Channel, UUID>{

    List<Channel> getChannelByGroupchat_Groupid(UUID uuid);

}
