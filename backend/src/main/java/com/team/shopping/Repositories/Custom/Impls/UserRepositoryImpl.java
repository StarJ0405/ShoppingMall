package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.QSiteUser;
import com.team.shopping.Repositories.Custom.UserRepositoryCustom;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QSiteUser qSiteUser = QSiteUser.siteUser;

    @Override
    public boolean isDuplicateUsername(String username) {
        return !jpaQueryFactory.select(qSiteUser).from(qSiteUser).where(qSiteUser.username.eq(username)).fetch().isEmpty();
    }

    @Override
    public boolean isDuplicateNickname(String nickname) {
        return !jpaQueryFactory.select(qSiteUser).from(qSiteUser).where(qSiteUser.nickname.eq(nickname)).fetch().isEmpty();
    }

    @Override
    public boolean isDuplicateEmail(String email) {
        return !jpaQueryFactory.select(qSiteUser).from(qSiteUser).where(qSiteUser.email.eq(email)).fetch().isEmpty();
    }

    @Override
    public boolean isDuplicatePhone(String phone) {
        return !jpaQueryFactory.select(qSiteUser).from(qSiteUser).where(qSiteUser.password.eq(phone)).fetch().isEmpty();
    }
}
