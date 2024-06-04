package com.team.shopping.Enums;

import lombok.Getter;

@Getter
public enum Gender {
    MAN("남성"), WOMAN("여성")
    //
    ;
    private final String name;

    Gender(String name) {
        this.name = name;
    }
}
