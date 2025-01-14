package org.example.chatservice.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.chatservice.dto.MessageDto;
    import org.example.chatservice.mapper.MessageMapper;
    import org.example.chatservice.model.Message;
import org.example.chatservice.model.User;
import org.example.chatservice.repository.MessageRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    private final MessageMapper mapper;

    private final EntityManager entityManager;

    @Transactional
    @PreAuthorize("hasRole('USER')")
    public MessageDto saveMessage(MessageDto messageDto, Authentication authentication){

        log.info("{}",authentication.getName());

        Message message = new Message();
        message.setUser(entityManager.getReference(User.class,messageDto.getUserId()));
        message.setContent(messageDto.getContent());

        Message savedMessage = messageRepository.save(message);

        log.info(savedMessage.toString());

        return mapper.toMessageDto(savedMessage);
    }
}
