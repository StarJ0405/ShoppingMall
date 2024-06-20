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
    private int price;
    private Double grade;
    private String title;

    private Long createDate;

    @Builder
    public RecentResponseDTO(Long recentId,Long productId, String url, Long createDate, String title, int price, Double grade) {
        this.recentId = recentId;
        this.grade = grade;
        this.price = price;
        this.title = title;
        this.productId = productId;
        this.url = url;
        this.createDate = createDate;
    }
}
