package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.MultiKey;

import java.util.Optional;

public interface MultiKeyRepositoryCustom {
    Optional<MultiKey> findByKey(String k);
}
