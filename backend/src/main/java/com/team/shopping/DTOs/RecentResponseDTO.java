package com.team.shopping.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecentResponseDTO {

    private Long recentId;
  
    private Long productId;
  
    private String url;

    private Long createDate;

    @Builder
    public RecentResponseDTO(Long recentId,Long productId, String url, Long createDate) {
        this.recentId = recentId;
        this.productId = productId;
        this.url = url;
        this.createDate = createDate;
    }
}
