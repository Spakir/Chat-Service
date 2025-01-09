package org.example.chatservice.service;

import lombok.RequiredArgsConstructor;
import org.example.chatservice.dto.UserDto;
import org.example.chatservice.mapper.UserMapper;
import org.example.chatservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserDto findById(Long id){
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow();
    }
}
