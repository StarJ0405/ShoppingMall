package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.Alarm;

import java.util.List;

public interface AlarmRepositoryCustom {
    List<Alarm> getList(String username);
}
