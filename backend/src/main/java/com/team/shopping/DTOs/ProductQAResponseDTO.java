package com.team.shopping.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Setter
@Getter
public class ProductQAResponseDTO {
    private long productId;
    private long productQAId;
    private String title;
    private String content;
    private String author;
    private String answer;
    private long createDate;
    @Builder
    public ProductQAResponseDTO(long productId, long productQAId, String title, String content, String author, String answer, LocalDateTime createDate) {
        this.productId = productId;
        this.productQAId = productQAId;
        this.title = title;
        this.content = content;
        this.author = author;
        this.answer = answer;
        this.createDate= createDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
