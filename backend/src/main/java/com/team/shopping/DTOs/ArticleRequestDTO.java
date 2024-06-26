package com.team.shopping.DTOs;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleRequestDTO {

    private Long articleId;

    private String title;

    private String content;

    private int type;
}
