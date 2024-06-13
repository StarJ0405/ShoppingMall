package com.team.shopping.Domains;

import com.team.shopping.DTOs.ArticleRequestDTO;
import com.team.shopping.Enums.Type;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor // 다른곳에서 필드값이 없어도 생성 할 수있다 . @AllArgsConstructor은 모두있어야함 , 저둘다없으면 생성못함
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



    @Builder //이런방식으로하면 엔티티에 저장될거란뜻 , 구조를 정해주는것
    public Article (ArticleRequestDTO articleRequestDTO, SiteUser siteUser ){
        this.title= articleRequestDTO.getTitle();
        this.content= articleRequestDTO.getContent();
        this.author=siteUser;
        this.type= Type.values()[articleRequestDTO.getType()]; //숫자형태를 Type으로 바꿔 저장해준다.
        this.createDate=LocalDateTime.now();


    }

}
