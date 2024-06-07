package com.team.shopping.Repositories;

import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.Custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<SiteUser, String>, UserRepositoryCustom {
    void deleteByUsername(String username);
}
