package com.team.shopping.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecentResponseDTO {

    private Long id;

    private String url;

    private Long createDate;

    @Builder
    public RecentResponseDTO(Long id, String url, Long createDate) {
        this.id = id;
        this.url = url;
        this.createDate = createDate;
    }
}
