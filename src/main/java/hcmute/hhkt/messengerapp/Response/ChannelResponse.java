package hcmute.hhkt.messengerapp.Response;


import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.hhkt.messengerapp.domain.Channel;
import hcmute.hhkt.messengerapp.domain.Groupchat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ChannelResponse {

    @JsonProperty("channel_id")
    private String channel_id;

    @JsonProperty("channel_name")
    private String channel_name;

    @JsonProperty("channel_type")
    private String channel_type;

    @JsonProperty("group_id")
    private String group_id;

    @JsonProperty("is_private")
    private Boolean is_private;

    public static ChannelResponse generateChannelResponse (Channel channel){
        return ChannelResponse.builder()
                .channel_id(channel.getChanenlid().toString())
                .group_id(channel.getGroupchat().getGroupid().toString())
                .channel_name(channel.getChannelname())
                .channel_type(channel.getChannelType().toString())
                .is_private(channel.getIsPrivate())
                .build();
    }

    public static List<ChannelResponse> generateChannelListResponse(List<Channel> channel){
        return channel.stream().map(ChannelResponse::generateChannelResponse).toList();
    }
}
