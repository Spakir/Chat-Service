package org.example.chatservice.config;

import org.example.chatservice.interceptor.JwtHandshakeInterceptor;
import org.example.chatservice.interceptor.RegistrationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@EnableMethodSecurity
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private RegistrationInterceptor registrationInterceptor;

    @Autowired
    private JwtHandshakeInterceptor jwtHandshakeInterceptor;


    @Override
    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry) {
        messageBrokerRegistry.enableSimpleBroker("/client");
        messageBrokerRegistry.setApplicationDestinationPrefixes("/server");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
         registration.interceptors(registrationInterceptor);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOrigins("http://localhost:8080")
                .addInterceptors(jwtHandshakeInterceptor)
                .withSockJS();
    }
}
