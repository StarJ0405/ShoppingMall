package com.StarJ.Social.Repositories;

import com.StarJ.Social.Domains.Auth;
import com.StarJ.Social.Domains.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
    boolean existsByUser(SiteUser user);
    Optional<Auth> findByRefreshToken(String refreshToken);
}
