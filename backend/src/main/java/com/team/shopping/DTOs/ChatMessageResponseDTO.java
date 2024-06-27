package com.team.shopping.DTOs;

import com.team.shopping.Enums.ChatType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageResponseDTO {
    private String message;
    private String sender;
    private Long createDate;
    private int type;
    @Builder
    public ChatMessageResponseDTO(String message, String sender, Long createDate, ChatType type) {
        this.message = message;
        this.sender = sender;
        this.createDate = createDate;
        this.type= type.ordinal();
    }
}
