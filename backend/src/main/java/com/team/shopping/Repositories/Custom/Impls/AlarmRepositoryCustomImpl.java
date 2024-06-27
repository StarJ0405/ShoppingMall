package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.Alarm;
import com.team.shopping.Domains.QAlarm;
import com.team.shopping.Repositories.Custom.AlarmRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AlarmRepositoryCustomImpl implements AlarmRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QAlarm qAlarm = QAlarm.alarm;

    @Override
    public List<Alarm> getList(String username) {
        return jpaQueryFactory.selectFrom(qAlarm).where(qAlarm.target.username.eq(username)).orderBy(qAlarm.createDate.desc()).fetch();
    }
}
