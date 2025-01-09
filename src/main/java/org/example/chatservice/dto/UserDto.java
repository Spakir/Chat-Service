package org.example.chatservice.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@Builder
public class UserDto {

    private Long id;

    private String username;

    private String password;

    private Set<String> roles = new HashSet<>();
}
