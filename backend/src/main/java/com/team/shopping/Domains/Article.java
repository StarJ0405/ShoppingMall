package com.team.shopping.Domains;

import com.team.shopping.DTOs.ArticleRequestDTO;
import com.team.shopping.Enums.Type;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser author;

    private Type type;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;



    @Builder
    public Article (ArticleRequestDTO articleRequestDTO, SiteUser siteUser ){
        this.title= articleRequestDTO.getTitle();
        this.content= articleRequestDTO.getContent();
        this.author=siteUser;
        this.type= Type.values()[articleRequestDTO.getType()];
        this.createDate=LocalDateTime.now();


    }

}
