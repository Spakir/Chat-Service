package org.example.chatservice.config;

import org.example.chatservice.converter.JwtAuthenticationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class ChatServiceConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String keySetUri;

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(keySetUri).build();
    }

    @Autowired
    private JwtAuthenticationConverter jwtAuthenticationConverter;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.oauth2ResourceServer(c -> c.jwt(
                jwtConfigurer -> jwtConfigurer.jwkSetUri(keySetUri)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter)
        ));

        http.authorizeHttpRequests(c -> c
                .requestMatchers("/websocket/**").permitAll()
                .anyRequest().authenticated()
        );

        http.cors(cors -> cors
                .configurationSource(corsConfigurationSource));

        http.csrf(Customizer.withDefaults());

        return http.build();
    }
}
