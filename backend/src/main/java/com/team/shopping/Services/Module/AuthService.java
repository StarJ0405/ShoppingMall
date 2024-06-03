package com.team.shopping.Services.Module;


import com.team.shopping.Domains.Auth;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.AuthRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;

    @Transactional
    public Auth save(SiteUser user, String accessToken, String refreshToken) {
        return this.authRepository.save(Auth.builder()
                .user(user)
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
    }

    public Auth get(String refreshToken) {
        return this.authRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new IllegalArgumentException("해당 REFRESH_TOKEN 을 찾을 수 없습니다.\nREFRESH_TOKEN = " + refreshToken));
    }

    public boolean isExist(SiteUser user) {
        return authRepository.existsByUser(user);
    }

}
