package com.team.shopping.DTOs;

import com.team.shopping.Domains.Auth;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponseDTO {

    private String tokenType;

    private String accessToken;

    private String refreshToken;

    @Builder
    public AuthResponseDTO(Auth auth) {
        this.tokenType = auth.getTokenType();
        this.accessToken = auth.getAccessToken();
        this.refreshToken = auth.getRefreshToken();
    }
}