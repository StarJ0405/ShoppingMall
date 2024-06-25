package com.team.shopping.DTOs;


import com.team.shopping.Domains.Article;
import com.team.shopping.Domains.SiteUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleResponseDTO {
    private Long id;

    private String title;

    private String content;

    private Long createDate;

    private Long modifyDate;

    private String authorName;


    @Builder
    public ArticleResponseDTO (Article article, SiteUser siteUser, Long createDate, Long modifyDate) {
        this.id= article.getId();
        this.title=article.getTitle();
        this.content= article.getContent();
        this.createDate = createDate;
        this.authorName= article.getAuthor().getNickname();
        this.modifyDate= modifyDate;
    }
}
