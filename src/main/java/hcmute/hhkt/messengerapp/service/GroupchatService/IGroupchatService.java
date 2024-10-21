package hcmute.hhkt.messengerapp.service.GroupchatService;

import hcmute.hhkt.messengerapp.domain.Groupchat;
import hcmute.hhkt.messengerapp.dto.GroupchatDTO;

public interface IGroupchatService {

    Groupchat createGroupchat(GroupchatDTO groupchatDTO);

}
