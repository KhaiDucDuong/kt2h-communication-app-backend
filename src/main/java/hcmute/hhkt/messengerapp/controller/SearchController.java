package hcmute.hhkt.messengerapp.controller;

import hcmute.hhkt.messengerapp.Response.SearchUserResponse;
import hcmute.hhkt.messengerapp.dto.SearchUserDTO;
import hcmute.hhkt.messengerapp.service.UserService.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hcmute.hhkt.messengerapp.Response.SearchUserResponse.generateSearchUserListResponse;
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final IUserService userService;
    @GetMapping("/find")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<?> searchUsername(@RequestParam String username,String currentId ){
        try {
            List<SearchUserDTO> users = userService.findUserbyUsername(username,currentId);
            List<SearchUserResponse> usersResponse = generateSearchUserListResponse(users);
            return ResponseEntity.status(HttpStatus.OK).body(usersResponse);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.valueOf(404)).body(e);
        }
    }
}
