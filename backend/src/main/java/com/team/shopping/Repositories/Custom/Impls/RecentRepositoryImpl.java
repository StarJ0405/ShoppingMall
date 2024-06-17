package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.QRecent;
import com.team.shopping.Domains.Recent;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.Custom.RecentRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RecentRepositoryImpl implements RecentRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QRecent qRecent = QRecent.recent;


    @Override
    public Optional<Recent> findProductByUser(Product product, SiteUser siteUser) {
        Recent recent = jpaQueryFactory.select(qRecent).from(qRecent).where(qRecent.product.eq(product).and(qRecent.user.eq(siteUser))).fetchOne();
        return Optional.ofNullable(recent);
    }

    @Override
    public List<Recent> findUsernameList(SiteUser user) {
        return jpaQueryFactory.select(qRecent).from(qRecent).where(qRecent.user.eq(user)).orderBy(qRecent.createDate.desc()).fetch();
    }


}
