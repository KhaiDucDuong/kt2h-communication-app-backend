package hcmute.hhkt.messengerapp.configuration;

import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.service.UserService.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
public class WebSocketEventListener {
    private final Logger log = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final IUserService userService;

    public WebSocketEventListener(IUserService userService) {
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
        if(user != null)
            userService.updateUserLastActivity(user);
        log.debug("Connected event [sessionId: " + sha.getSessionId() +";" + "]");
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        final String email = Objects.requireNonNull(sha.getUser()).getName();
        User user = userService.findUserByEmail(email);
        if(user != null)
            userService.updateUserLastActivity(user);
        log.debug("Disconnect event [sessionId: " + sha.getSessionId() +";" + "]");
    }
}
