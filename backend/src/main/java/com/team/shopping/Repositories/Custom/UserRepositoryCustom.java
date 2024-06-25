package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.SiteUser;

import java.util.List;

public interface UserRepositoryCustom {

    boolean isDuplicateUsername(String username);
    List<SiteUser> isDuplicateNickname(String nickname);
    List<SiteUser> isDuplicateEmail(String email);
    List<SiteUser> isDuplicatePhone(String phone);
}
