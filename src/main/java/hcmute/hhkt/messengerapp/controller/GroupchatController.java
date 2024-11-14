package hcmute.hhkt.messengerapp.controller;

import hcmute.hhkt.messengerapp.Response.GroupchatResponse;
import hcmute.hhkt.messengerapp.domain.Groupchat;
import hcmute.hhkt.messengerapp.dto.GroupchatDTO;
import hcmute.hhkt.messengerapp.service.GroupchatService.IGroupchatService;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.service.FriendshipService.FriendshipServiceImpl;
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
@RequestMapping("/api/groupchats")
@RequiredArgsConstructor
public class GroupchatController {
    private final IGroupchatService groupchatService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('USER')")
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

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<?> getGroupchat(@RequestParam String id){
        try {
            System.out.println("Group chat ID: " + id);
            List<Groupchat> groupchatList = groupchatService.getAllgroupChat(id);
            List<GroupchatResponse> response = generateGroupchatListResponse(groupchatList);
        return ResponseEntity.ok().body(response);
        }
        catch (Exception e) {
            return ResponseEntity.status(404).body(e);
        }
        }


    @DeleteMapping("/del")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<?> delGroupchat(@RequestParam String groupid){
        try
        {
            System.out.println("Group chat ID deleted: " + groupid);
            boolean IsDeleted = groupchatService.delGroupchat(groupid);
            if (IsDeleted) return ResponseEntity.ok().body("deleted");
            else  return ResponseEntity.ok().body("not deleted");
        }
        catch (Exception e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @PutMapping("/edit")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<?> editGroupchat(@Valid @RequestBody GroupchatDTO groupchat){
        try{
            boolean isUpdated = groupchatService.editGroupchat(groupchat);
            if (isUpdated) {
                System.out.println("Group chat ID updated: " + groupchat.getGroup_id());
                return ResponseEntity.ok().body("updated");
            }
            else  {
                System.out.println("Group chat ID not updated: " + groupchat.getGroup_id());
                return ResponseEntity.ok().body("not updated");}
        }
        catch (Exception e) {
            return ResponseEntity.status(404).body(e);
        }
    }
}
