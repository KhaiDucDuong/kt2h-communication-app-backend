package hcmute.hhkt.messengerapp.controller;

import hcmute.hhkt.messengerapp.dto.GroupchatDTO;
import hcmute.hhkt.messengerapp.service.GroupchatService.IGroupchatService;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.service.FriendshipService.FriendshipServiceImpl;
import hcmute.hhkt.messengerapp.service.UserService.UserServiceImpl;
import hcmute.hhkt.messengerapp.util.SecurityUtil;
import hcmute.hhkt.messengerapp.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groupchats")
@RequiredArgsConstructor
public class GroupchatController {
    private final IGroupchatService groupchatService;

    @PostMapping("/add")
    public ResponseEntity<?> addGroupchat(@Valid @RequestBody GroupchatDTO groupchat){
        try {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        groupchatService.createGroupchat(groupchat);
        return ResponseEntity.ok().body("Group chat of " + email +" created");

        }
        catch(Exception e){
            return ResponseEntity.status(404).body(e);
        }
    }
}
