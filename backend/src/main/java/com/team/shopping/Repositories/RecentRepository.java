package com.team.shopping.Repositories;

import com.team.shopping.Domains.Recent;
import com.team.shopping.Repositories.Custom.RecentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentRepository extends JpaRepository<Recent, Long>, RecentRepositoryCustom {
}
