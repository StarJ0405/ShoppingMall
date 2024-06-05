package com.team.shopping.Services.Module;


import com.team.shopping.DTOs.SignupRequestDTO;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Enums.Gender;
import com.team.shopping.Enums.UserRole;
import com.team.shopping.Exceptions.DataDuplicateException;
import com.team.shopping.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SiteUser save(SignupRequestDTO signupRequestDTO) {
        return userRepository.save(SiteUser                 //
                .builder()                                  //
                .username(signupRequestDTO.getUsername())
                .name(signupRequestDTO.getName())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .nickname(signupRequestDTO.getNickname())
                .email(signupRequestDTO.getEmail())
                .gender(Gender.values()[signupRequestDTO
                .getGender()]).role(UserRole.values()[signupRequestDTO.getRole()])
                .birthday(signupRequestDTO.getBirthday())
                .phoneNumber(signupRequestDTO.getPhoneNumber())
                .build());                                  //
    }

    @Transactional
    public SiteUser get(String value) throws IllegalArgumentException{
        return this.userRepository.findById(value).orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. user_id = " + value));
    }

    public Optional<SiteUser> getOptional(String value) {
        return this.userRepository.findById(value);
    }

    @Transactional
    public void delete(String value) {
        SiteUser user = this.userRepository.findById(value).orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. user_id = " + value));
        this.userRepository.delete(user);
    }

    public void check(SignupRequestDTO signupRequestDTO) {
        if (userRepository.isDuplicateUsername(signupRequestDTO.getUsername()))
            throw new DataDuplicateException("username");
        if (userRepository.isDuplicateEmail(signupRequestDTO.getEmail())) throw new DataDuplicateException("email");
        if (userRepository.isDuplicateNickname(signupRequestDTO.getNickname()))
            throw new DataDuplicateException("nickname");
        if (userRepository.isDuplicatePhone(signupRequestDTO.getPhoneNumber())) throw new DataDuplicateException("phone");
    }

    public boolean isMatch(String password1, String password2) {
        return passwordEncoder.matches(password1, password2);
    }

}
