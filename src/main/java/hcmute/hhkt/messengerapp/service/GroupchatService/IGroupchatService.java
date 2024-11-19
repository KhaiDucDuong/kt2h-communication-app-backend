package hcmute.hhkt.messengerapp.service.GroupchatService;

import hcmute.hhkt.messengerapp.domain.Groupchat;
import hcmute.hhkt.messengerapp.dto.GroupchatDTO;

import java.util.List;

public interface IGroupchatService {

    Groupchat createGroupchat(GroupchatDTO groupchatDTO);
    List<Groupchat> getAllgroupChat(String userID);
    boolean delGroupchat(String groupID);

    boolean editGroupchat (GroupchatDTO groupchatDTO);

}
