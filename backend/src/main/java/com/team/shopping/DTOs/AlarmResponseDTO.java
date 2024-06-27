package com.team.shopping.DTOs;

import lombok.Builder;

@Builder
public record AlarmResponseDTO(Long id, String sender, String message, String url, boolean isRead, long createDate) {

}
