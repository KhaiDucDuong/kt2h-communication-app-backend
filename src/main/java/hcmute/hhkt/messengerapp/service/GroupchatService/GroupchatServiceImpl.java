package hcmute.hhkt.messengerapp.service.GroupchatService;

import hcmute.hhkt.messengerapp.domain.Groupchat;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.repository.GroupchatRepository;
import org.springframework.stereotype.Service;
import hcmute.hhkt.messengerapp.dto.GroupchatDTO;

import java.util.UUID;

@Service
public class GroupchatServiceImpl implements  IGroupchatService{
    private final GroupchatRepository groupchatRepository;

    public GroupchatServiceImpl(GroupchatRepository groupchatRepository) {
        this.groupchatRepository = groupchatRepository;
    }

    @Override
    public Groupchat createGroupchat(GroupchatDTO groupchatDTO) {
        User user = new User();
        user.setId(UUID.fromString(groupchatDTO.getOwner_id()));
        Groupchat groupchat = Groupchat.builder()
                .groupname(groupchatDTO.getGroup_name())
                .groupIMG(groupchatDTO.getGroup_img())
                .QRcode("")
                .owner(user).build();
        return groupchatRepository.save(groupchat);
    }
}
