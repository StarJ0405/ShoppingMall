package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.QTag;
import com.team.shopping.Domains.Tag;
import com.team.shopping.Repositories.Custom.TagRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QTag qTag = QTag.tag;
    @Override
    public List<String> findTagNamesByProduct(Product product) {
        return jpaQueryFactory.select(qTag.name).from(qTag).where(qTag.product.eq(product)).fetch();
    }

    @Override
    public List<Tag> getTagByProduct(Product product) {
        return jpaQueryFactory.selectFrom(qTag).where(qTag.product.eq(product)).fetch();
    }


}
