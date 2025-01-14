package org.example.chatservice.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
public class RegistrationInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.SEND.equals(accessor.getCommand())) {
            List<String> authList = accessor.getNativeHeader("Authorization");

            log.info("AUTH PRE SEND : {}", authList);

            if (authList == null || authList.isEmpty()) {
                log.error("Authorization header is missing");
                return null;
            }

            String accessToken = authList.get(0).substring(7);
            Jwt jwt = null;
            try {
                jwt = jwtDecoder.decode(accessToken);
            } catch (JwtException jwtException) {
                log.error("JWT decoding error: {}", jwtException.getMessage());
                return null;
            }
        }
        return message;
    }

}
