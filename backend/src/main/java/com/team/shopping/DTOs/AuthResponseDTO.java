package com.StarJ.Social.DTOs;

import com.StarJ.Social.Domains.Auth;
import lombok.*;

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