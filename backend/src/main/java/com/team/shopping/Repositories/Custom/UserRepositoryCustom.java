package com.team.shopping.Repositories.Custom;

public interface UserRepositoryCustom {

    boolean isDuplicateUsername(String username);
    boolean isDuplicateNickname(String nickname);
    boolean isDuplicateEmail(String email);
    boolean isDuplicatePhone(String phone);
}
