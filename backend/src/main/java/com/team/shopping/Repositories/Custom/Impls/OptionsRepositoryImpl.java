package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.OptionList;
import com.team.shopping.Domains.Options;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.QOptions;
import com.team.shopping.Repositories.Custom.OptionsRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class OptionsRepositoryImpl implements OptionsRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    QOptions qOptions = QOptions.options;


    @Override
    public Optional<Options> getOptionByOptionList(OptionList optionList) {
        Options options = jpaQueryFactory.selectFrom(qOptions).where(qOptions.optionList.eq(optionList)).fetchOne();
        return Optional.ofNullable(options);
    }
}
