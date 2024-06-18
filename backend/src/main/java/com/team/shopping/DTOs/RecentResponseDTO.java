package com.team.shopping.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RecentResponseDTO {
    private Long id;
    private String url;
    private LocalDateTime createDate;

    @Builder
    public RecentResponseDTO(Long id, String url, LocalDateTime createDate) {
        this.id = id;
        this.url = url;
        this.createDate = createDate;
    }
}
