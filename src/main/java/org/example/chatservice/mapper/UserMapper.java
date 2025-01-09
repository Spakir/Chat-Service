package org.example.chatservice.mapper;

import org.example.chatservice.dto.UserDto;
import org.example.chatservice.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    User toUser(UserDto userDto);
}
