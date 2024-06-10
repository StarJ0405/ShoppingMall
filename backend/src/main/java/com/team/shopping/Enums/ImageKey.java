package com.team.shopping.Enums;

import com.team.shopping.Domains.Article;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum ImageKey {
    Product, Article, User, Review
    //
    ;
    public String getKey(Long value){
        return this.name()+"."+value;
    }
}
