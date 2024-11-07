package hcmute.hhkt.messengerapp.configuration;

import hcmute.hhkt.messengerapp.Response.UserStatusResponse;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.UserDefaultStatus;
import hcmute.hhkt.messengerapp.domain.enums.UserStatus;
import hcmute.hhkt.messengerapp.service.UserService.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
public class WebSocketEventListener {
    private final Logger log = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final IUserService userService;

    public WebSocketEventListener(SimpMessagingTemplate simpMessagingTemplate, IUserService userService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userService = userService;
    }

    @EventListener
    private void handleSessionConnect(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        log.debug("Connect event [sessionId: " + sha.getSessionId() +";" + "]");
    }

    @EventListener
    private void handleSessionConnected(SessionConnectedEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        final String email = Objects.requireNonNull(sha.getUser()).getName();
        User user = userService.findUserByEmail(email);
        if(user != null){
            userService.updateUserStatus(user, UserStatus.ONLINE);
            if(user.getDefaultStatus() == UserDefaultStatus.ONLINE) {
                UserStatusResponse response = UserStatusResponse.fromUser(user, user.getStatus());
                simpMessagingTemplate.convertAndSend("/user/" + user.getId().toString() + "/status", response);
                log.debug("Sending message to topic {} about user new status {}", "/user/" + user.getId().toString() + "/status", response.getStatus());
            }
        }

        log.debug("Connected event [sessionId: " + sha.getSessionId() +";" + "]");
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        final String email = Objects.requireNonNull(sha.getUser()).getName();
        User user = userService.findUserByEmail(email);
        if(user != null){
            userService.updateUserStatus(user, UserStatus.OFFLINE);
            if(user.getDefaultStatus() != UserDefaultStatus.INVISIBLE) {
                UserStatusResponse response = UserStatusResponse.fromUser(user, user.getStatus());
                simpMessagingTemplate.convertAndSend("/user/" + user.getId().toString() + "/status", response);
                log.debug("Sending message to topic {} about user new status {}", "/user/" + user.getId().toString() + "/status", response.getStatus());
            }
        }
        log.debug("Disconnect event [sessionId: " + sha.getSessionId() +";" + "]");
    }
}
