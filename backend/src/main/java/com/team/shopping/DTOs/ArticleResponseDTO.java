package com.team.shopping.DTOs;


import com.team.shopping.Domains.Article;
import com.team.shopping.Domains.SiteUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor // 이건 내가 원하는 필드값 없이  만들어줄수있고
public class ArticleResponseDTO { //  DTO 는 데이터를 객체로 변환한다


    private Long id;

    private String title;

    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    private String authorName; // 내가 보내줘야 화면에 나오니깐 .


    @Builder
    public ArticleResponseDTO (Article article, SiteUser siteUser) { //set 을 대신해준다. (빈상태로 보내면 안되니깐. article을 ArticleResponseDTO 로전환해 보내야함  )
        this.id= article.getId();
        this.title=article.getTitle();
        this.content= article.getContent();
        this.createDate = article.getCreateDate();
        this.authorName= article.getAuthor().getNickname();
        this.modifyDate=article.getModifyDate();
    }
}
