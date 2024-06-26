package com.team.shopping.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomResponseDTO {
    private Long id;
    private String sendUsername;
    private String acceptUsername;
    private Long createDate;
    private Long modifyDate;
    private List<ChatMessageResponseDTO> chats;

    @Builder
    public ChatRoomResponseDTO(Long id, String sendUsername, String acceptUsername, Long createDate, Long modifyDate, List<ChatMessageResponseDTO> chats) {
        this.id = id;
        this.sendUsername = sendUsername;
        this.acceptUsername = acceptUsername;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.chats = chats;
    }
}
