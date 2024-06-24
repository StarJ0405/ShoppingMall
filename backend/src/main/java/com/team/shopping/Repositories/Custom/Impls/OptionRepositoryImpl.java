package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.OptionList;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.QOptionList;
import com.team.shopping.Repositories.Custom.OptionListRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class OptionRepositoryImpl implements OptionListRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    QOptionList qOptionList = QOptionList.optionList;
    @Override
    public Optional<OptionList> getOptionListByProduct(Product product) {
        OptionList optionList = jpaQueryFactory.selectFrom(qOptionList).where(qOptionList.product.eq(product)).fetchOne();
        return Optional.ofNullable(optionList);
    }
}
