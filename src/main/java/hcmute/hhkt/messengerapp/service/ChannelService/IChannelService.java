package hcmute.hhkt.messengerapp.service.ChannelService;

import hcmute.hhkt.messengerapp.domain.Channel;
import hcmute.hhkt.messengerapp.dto.ChannelDTO;
import java.util.List;

public interface IChannelService {
    List<Channel> GetAllChannel(String groupID);

    boolean CreateChannel(ChannelDTO channel);

    boolean DeleteChannel(String channelID);

    boolean EditChannel(ChannelDTO channel);

}
