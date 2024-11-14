package hcmute.hhkt.messengerapp.service.ChannelService;

import hcmute.hhkt.messengerapp.domain.Channel;
import hcmute.hhkt.messengerapp.domain.Groupchat;
import hcmute.hhkt.messengerapp.domain.enums.ChannelType;
import hcmute.hhkt.messengerapp.dto.ChannelDTO;
import hcmute.hhkt.messengerapp.dto.GroupchatDTO;
import hcmute.hhkt.messengerapp.repository.ChannelRepository;
import hcmute.hhkt.messengerapp.repository.GroupchatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChannelServiceImpl implements  IChannelService{
    private final ChannelRepository channelRepository;
    private  final GroupchatRepository groupchatRepository;

    @Override
    public List<Channel> GetAllChannel(String groupID) {
        return channelRepository.getChannelByGroupchat_Groupid(UUID.fromString(groupID));
    }

    @Override
    public boolean CreateChannel(ChannelDTO channel) {
        try{
        Channel channel1 = new Channel();
        Groupchat groupchat = groupchatRepository.getGroupchatByGroupid(UUID.fromString(channel.getGroup_id()));
        channel1.setChannelname(channel.getChannel_name());
        channel1.setGroupchat(groupchat);
        channel1.setChannelType(ChannelType.valueOf(channel.getChannel_type()));
        channel1.setIsPrivate(channel.getIs_private());
        channelRepository.save(channel1);
        return true;
        }
        catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean EditChannel(ChannelDTO channel){
        try{
            Channel editchannel = channelRepository.findById(UUID.fromString(channel.getChannel_id())).orElse(null);
            if (editchannel != null){
                editchannel.setChannelname(channel.getChannel_name());
                editchannel.setIsPrivate(channel.getIs_private());
                channelRepository.save(editchannel);
                return true;
            }
            return false;

        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean DeleteChannel(String channelID) {
        try{
        channelRepository.deleteById(UUID.fromString(channelID));
        return true;
    }
        catch(Exception e) {
            return false;
        }
    }
}
