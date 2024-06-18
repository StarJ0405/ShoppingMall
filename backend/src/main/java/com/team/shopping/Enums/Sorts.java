package com.team.shopping.Enums;

import lombok.Getter;

@Getter
public enum Sorts {
    CREATE_DATE_DESC("최근 등록순"),
    PRICE_DESC("높은 가격순"),
    PRICE_ASC("낮은 가격순"),
    REVIEW_COUNT_DESC("리뷰 많은순");

    private final String sort;

    Sorts(String sort) {
        this.sort = sort;
    }
}
