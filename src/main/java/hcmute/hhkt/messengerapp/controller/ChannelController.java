package hcmute.hhkt.messengerapp.controller;


import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.dto.ChannelDTO;
import hcmute.hhkt.messengerapp.service.ChannelService.IChannelService;
import hcmute.hhkt.messengerapp.service.UserService.UserServiceImpl;
import hcmute.hhkt.messengerapp.util.SecurityUtil;
import hcmute.hhkt.messengerapp.util.annotation.ApiMessage;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static hcmute.hhkt.messengerapp.Response.GroupchatResponse.generateGroupchatListResponse;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final IChannelService channelService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<?> addChannel(@RequestBody ChannelDTO channel){
        try {
            channelService.CreateChannel(channel);
            return ResponseEntity.ok().body("Channel have been created");
        }
        catch(Exception e){
            return ResponseEntity.status(404).body("Channel have not been created");
        }
    }

}
