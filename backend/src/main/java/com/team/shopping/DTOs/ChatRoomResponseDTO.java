package com.team.shopping.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomResponseDTO {
    private Long id;
    private String acceptUsername;
    private String acceptUsername_url;
    private Long createDate;
    private Long modifyDate;
    private int type;
    private String lastMessage;

    @Builder
    public ChatRoomResponseDTO(Long id, String acceptUsername, Long createDate, Long modifyDate, int type, String lastMessage, String acceptUsername_url) {
        this.id = id;
        this.acceptUsername = acceptUsername;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.type = type;
        this.lastMessage = lastMessage;
        this.acceptUsername_url = acceptUsername_url;
    }
}
