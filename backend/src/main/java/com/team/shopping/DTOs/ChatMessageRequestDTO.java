package com.team.shopping.DTOs;

import lombok.Getter;

@Getter
public class ChatMessageRequestDTO {
    private String sender;
    private String message;
    private int type;
}
