package org.example.chatservice.mapper;

import org.example.chatservice.dto.MessageDto;
import org.example.chatservice.model.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message toMessage(MessageDto messageDto);

    MessageDto toMessageDto(Message message);
}
