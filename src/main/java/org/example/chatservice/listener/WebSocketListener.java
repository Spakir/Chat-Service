package org.example.chatservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.chatservice.dto.MessageDto;
import org.example.chatservice.model.ResponseMessage;
import org.example.chatservice.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketListener {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send")
    @Transactional
    public void sendMessage(@Payload MessageDto messageDto, Authentication authentication) {

        log.info("Получено сообщение: {}", messageDto);

        messageService.saveMessage(messageDto);

        ResponseMessage responseMessage = new ResponseMessage(messageDto.getContent(),authentication.getName());
        messagingTemplate.convertAndSend( "/client/send", responseMessage);
    }
}
