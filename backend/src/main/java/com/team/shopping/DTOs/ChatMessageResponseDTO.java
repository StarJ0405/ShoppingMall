package com.team.shopping.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageResponseDTO {
    private String message;
    private String sender;
    private Long createDate;

    @Builder
    public ChatMessageResponseDTO(String message, String sender, Long createDate) {
        this.message = message;
        this.sender = sender;
        this.createDate = createDate;
    }
}
