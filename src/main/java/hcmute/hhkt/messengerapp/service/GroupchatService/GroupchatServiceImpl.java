package hcmute.hhkt.messengerapp.service.GroupchatService;

import hcmute.hhkt.messengerapp.domain.Groupchat;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.repository.GroupchatRepository;
import hcmute.hhkt.messengerapp.service.UserService.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import hcmute.hhkt.messengerapp.dto.GroupchatDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GroupchatServiceImpl implements  IGroupchatService{
    private final GroupchatRepository groupchatRepository;
    private final IUserService userService;


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
    @Override
    public List<Groupchat> getAllgroupChat(String userID){
        return groupchatRepository.getGroupchatByOwnerId(UUID.fromString(userID));
    }
    @Override
    public boolean delGroupchat (String groupID)
    {
        try {
            groupchatRepository.deleteById(UUID.fromString(groupID));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean editGroupchat(GroupchatDTO groupchat){
        try{
            Groupchat editgroupchat = groupchatRepository.findById(UUID.fromString(groupchat.getGroup_id())).orElse(null);
            if (editgroupchat != null){
                editgroupchat.setGroupname(groupchat.getGroup_name());
                editgroupchat.setGroupIMG(groupchat.getGroup_img());
                groupchatRepository.save(editgroupchat);
                return true;
            }
            return false;

        }
        catch(Exception e){
            return false;
        }
    }
}
