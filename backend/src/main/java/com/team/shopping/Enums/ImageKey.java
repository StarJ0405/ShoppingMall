package com.team.shopping.Enums;

import lombok.Getter;

@Getter
public enum ImageKey {
    Product, Article, User, Review, Temp
    //
    ;
    public String getKey(String value){
        return this.name()+"."+value;
    }
}