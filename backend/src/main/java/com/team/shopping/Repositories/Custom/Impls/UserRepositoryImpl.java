package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.QSiteUser;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.Custom.UserRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QSiteUser qSiteUser = QSiteUser.siteUser;

    @Override
    public boolean isDuplicateUsername(String username) {
        return !jpaQueryFactory.select(qSiteUser).from(qSiteUser).where(qSiteUser.username.eq(username)).fetch().isEmpty();
    }

    @Override
    public List<SiteUser> isDuplicateNickname(String nickname) {
        return jpaQueryFactory.select(qSiteUser).from(qSiteUser).where(qSiteUser.nickname.eq(nickname)).fetch();
    }

    @Override
    public List<SiteUser> isDuplicateEmail(String email) {
        return jpaQueryFactory.select(qSiteUser).from(qSiteUser).where(qSiteUser.email.eq(email)).fetch();
    }

    @Override
    public List<SiteUser> isDuplicatePhone(String phone) {
        return jpaQueryFactory.select(qSiteUser).from(qSiteUser).where(qSiteUser.phoneNumber.eq(phone)).fetch();
    }
}
