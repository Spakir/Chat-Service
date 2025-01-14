package org.example.chatservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.chatservice.dto.MessageDto;
import org.example.chatservice.model.ResponseMessage;
import org.example.chatservice.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send")
    @Transactional
    public void sendMessage(@Payload MessageDto messageDto,
                            @AuthenticationPrincipal Authentication authentication) {

        log.info("auth : {}", authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("Получено сообщение: {}", messageDto);

        messageService.saveMessage(messageDto, authentication);
        ResponseMessage responseMessage = new ResponseMessage(messageDto.getContent(), authentication.getName());
        messagingTemplate.convertAndSend("/client/send", responseMessage);
    }
}
