package org.example.chatservice.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ResponseMessage {
    private String content;

    private String username;
}
