package com.team.shopping.Domains;

import com.team.shopping.Enums.Type;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(length = 200)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser author;

    private Type type;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;



}
