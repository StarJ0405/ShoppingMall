package com.team.shopping.Enums;

import lombok.Getter;

@Getter
public enum ImageKey {
    PRODUCT, ARTICLE, USER, REVIEW, TEMP, PAYMENT
    //
    ;
    public String getKey(String value){
        return this.name()+"."+value;
    }
}