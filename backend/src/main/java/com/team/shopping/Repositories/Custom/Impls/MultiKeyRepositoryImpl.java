package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.MultiKey;
import com.team.shopping.Domains.QMultiKey;
import com.team.shopping.Enums.ImageKey;
import com.team.shopping.Repositories.Custom.MultiKeyRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
public class MultiKeyRepositoryImpl implements MultiKeyRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    QMultiKey qMultiKey = QMultiKey.multiKey;

    @Override
    public Optional<MultiKey> findByKey(String k) {
        MultiKey multiKey = jpaQueryFactory.selectFrom(qMultiKey).where(qMultiKey.k.eq(k)).fetchOne();
        return Optional.ofNullable(multiKey);
    }
}
