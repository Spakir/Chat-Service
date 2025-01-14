package org.example.chatservice.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.chatservice.authentication.CustomAuthentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Component
public class JwtHandshakeInterceptor extends HttpSessionHandshakeInterceptor {


    private JwtDecoder jwtDecoder;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {


        ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;

        HttpServletRequest servletRequest = servletServerHttpRequest.getServletRequest();


        Cookie[] cookies = servletRequest.getCookies();

        String token = null;

        Jwt jwtToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT")) {

                    token = cookie.getValue();

                    System.out.println("JWT FROM COOKIE " + token);
                    break;
                }
            }
        }
        try {
            jwtToken = jwtDecoder.decode(token);
            List<String> roles = jwtToken.getClaimAsStringList("roles");

            Collection<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            Authentication authentication = new CustomAuthentication(jwtToken, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("Authentication set in SecurityContext: {}", SecurityContextHolder.getContext().getAuthentication());

            return true;

        } catch (Exception e) {
            log.info("Invalid token: {}", e.getMessage());
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
    }
}
