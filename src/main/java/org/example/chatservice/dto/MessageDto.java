package org.example.chatservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@Builder
public class MessageDto {

    private Long id;

    private Long userId;

    private LocalDateTime createdAt;

    private String content;
}
