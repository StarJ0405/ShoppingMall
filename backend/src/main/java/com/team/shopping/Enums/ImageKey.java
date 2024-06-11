package com.team.shopping.Enums;

import com.team.shopping.Domains.Article;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum ImageKey {
    Product, Article, User, Review, Temp
    //
    ;
    public String getKey(String value){
        return this.name()+"."+value;
    }
}
