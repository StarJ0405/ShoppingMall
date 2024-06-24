package com.team.shopping.Services.Module;


import com.team.shopping.DTOs.PaymentLogResponseDTO;
import com.team.shopping.DTOs.SignupRequestDTO;
import com.team.shopping.DTOs.UserRequestDTO;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Enums.Gender;
import com.team.shopping.Enums.UserRole;
import com.team.shopping.Exceptions.DataDuplicateException;
import com.team.shopping.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void save(SignupRequestDTO signupRequestDTO) {

        userRepository.save(SiteUser.builder()
                .username(signupRequestDTO.getUsername())
                .name(signupRequestDTO.getName())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .nickname(signupRequestDTO.getNickname())
                .email(signupRequestDTO.getEmail())
                .gender(Gender.values()[signupRequestDTO.getGender()])
                .role(UserRole.values()[signupRequestDTO.getRole()])
                .birthday(signupRequestDTO.getBirthday())
                .phoneNumber(signupRequestDTO.getPhoneNumber())
                .build());
    }

    @Transactional
    public SiteUser updateProfile(String username, UserRequestDTO userRequestDTO) {
        SiteUser siteUser = get(username);

        siteUser.setName(userRequestDTO.getName());
        siteUser.setPhoneNumber(userRequestDTO.getPhoneNumber());
        siteUser.setNickname(userRequestDTO.getNickname());
        {
        }
        siteUser.setEmail(userRequestDTO.getEmail());

        return this.userRepository.save(siteUser);
    }

    @Transactional
    public SiteUser updatePassword(SiteUser user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }


    @Transactional
    public SiteUser get(String value) throws IllegalArgumentException {
        return this.userRepository.findById(value).orElse(null);
    }

    public SiteUser addToPoint (SiteUser user, PaymentLogResponseDTO paymentLog) {

        BigDecimal totalPrice = BigDecimal.valueOf(paymentLog.getTotalPrice());
        BigDecimal onePercent = totalPrice.multiply(BigDecimal.valueOf(0.01)).setScale(0, RoundingMode.HALF_UP);
        int onePercentInt = onePercent.intValue();

        user.setPoint(user.getPoint() + onePercentInt);
        return this.userRepository.save(user);
    }

    public Optional<SiteUser> getOptional(String value) {
        return this.userRepository.findById(value);
    }

    @Transactional
    public void delete(SiteUser user) {
        this.userRepository.delete(user);
    }

    public void usernameCheck(String username) {
        if (userRepository.isDuplicateUsername(username)) throw new DataDuplicateException("username");
    }

    public void otherCheck(String email, String nickname, String phone) {
        if (userRepository.isDuplicateEmail(email)) throw new DataDuplicateException("email");
        if (userRepository.isDuplicateNickname(nickname)) throw new DataDuplicateException("nickname");
        if (userRepository.isDuplicatePhone(phone)) throw new DataDuplicateException("phone");
    }

    public boolean isMatch(String password1, String password2) {
        return passwordEncoder.matches(password1, password2);
    }


    @Transactional
    public void deleteUser(SiteUser user) {
        userRepository.deleteByUsername(user.getUsername());
    }


}

