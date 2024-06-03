package com.StarJ.Social.Service.Modules;

import com.StarJ.Social.Domains.SiteUser;
import com.StarJ.Social.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    public List<SiteUser> getList(String value, String username) {
        return this.userRepository.list(value, username);
    }
    @Transactional
    public List<SiteUser> getRecentList(String username) {
        return this.userRepository.recentList(username);
    }
    @Transactional
    public void update(String username, String nickname, String email, String phoneNumber, String password, String description) {
        SiteUser user = get(username);
        user.setNickname(nickname);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(passwordEncoder.encode(password));
        user.setDescription(description);
        this.userRepository.save(user);
    }

    @Transactional
    public void updateActive(SiteUser user) {
        user.setActiveDate(LocalDateTime.now());
        this.userRepository.save(user);
    }

    @Transactional
    public void delete(String value) {
        SiteUser user = this.userRepository.find(value).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. user_id = " + value));
        this.userRepository.delete(user);
    }


    public boolean isMatch(String password1, String password2) {
        return passwordEncoder.matches(password1, password2);
    }
}
