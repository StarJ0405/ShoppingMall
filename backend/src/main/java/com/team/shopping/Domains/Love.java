package com.team.shopping.Domains;

import com.team.shopping.Enums.Type;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Love {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser user;

    @OneToMany
    private List<Article> targetList = new ArrayList<>();

    private Type targetType;
}
