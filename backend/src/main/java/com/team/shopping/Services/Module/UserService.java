package com.team.shopping.Services.Module;


import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SiteUser save(String username, String nickname, String password, String phoneNumber, String email) {
        return userRepository.save(SiteUser                 //
                .builder()                                  //
                .username(username)                         //
                .nickname(nickname)                         //
                .password(passwordEncoder.encode(password)) //
                .phoneNumber(phoneNumber)                   //
                .email(email)                               //
                .build());                                  //
    }

    @Transactional
    public SiteUser get(String value) {
        return this.userRepository.findById(value).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. user_id = " + value));
    }

    @Transactional
    public void delete(String value) {
        SiteUser user = this.userRepository.findById(value).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. user_id = " + value));
        this.userRepository.delete(user);
    }

    public boolean isMatch(String password1, String password2) {
        return passwordEncoder.matches(password1, password2);
    }
}
