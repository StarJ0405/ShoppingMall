package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.QCategory;
import com.team.shopping.Repositories.Custom.CategoryRepositoryCustom;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QCategory qCategory = QCategory.category;

    @Override
    public boolean isDuplicateNameAndParent(String name, String parent) {
        return !jpaQueryFactory.select(qCategory).from(qCategory).where(qCategory.name.eq(name).and(qCategory.parent.name.eq(parent))).fetch().isEmpty();
    }
    @Override
    public boolean isDuplicateName(String name) {
        return !jpaQueryFactory.select(qCategory).from(qCategory).where(qCategory.parent.isNull().and(qCategory.name.eq(name))).fetch().isEmpty();
    }
}
