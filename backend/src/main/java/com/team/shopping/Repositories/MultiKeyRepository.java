package com.team.shopping.Repositories;

import com.team.shopping.Domains.MultiKey;
import com.team.shopping.Repositories.Custom.MultiKeyRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultiKeyRepository extends JpaRepository<MultiKey, String>, MultiKeyRepositoryCustom {
}
