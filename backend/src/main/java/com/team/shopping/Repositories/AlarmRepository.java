package com.team.shopping.Repositories;

import com.team.shopping.Domains.Alarm;
import com.team.shopping.Repositories.Custom.AlarmRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm,Long>, AlarmRepositoryCustom {
}
