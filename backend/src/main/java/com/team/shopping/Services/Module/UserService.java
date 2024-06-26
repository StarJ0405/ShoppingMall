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
    @Transactional
    public SiteUser addToPoint(SiteUser user, PaymentLogResponseDTO paymentLog) {

        BigDecimal totalPrice = BigDecimal.valueOf(paymentLog.getTotalPrice());
        BigDecimal onePercent = totalPrice.multiply(BigDecimal.valueOf(0.01)).setScale(0, RoundingMode.HALF_UP);
        long onePercentAsLong = onePercent.longValueExact(); // 변환 시 예외 처리

        long newPoint = user.getPoint() + onePercentAsLong;
        user.setPoint(newPoint);
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
    public void otherEmailCheck(String email) {
        if (!userRepository.isDuplicateEmail(email).isEmpty()) throw new DataDuplicateException("email");

    }
    public void otherNickNameCheck(String nickname) {
        if (!userRepository.isDuplicateNickname(nickname).isEmpty()) throw new DataDuplicateException("nickname");
    }
    public void otherPhoneCheck(String phone) {
        if (!userRepository.isDuplicatePhone(phone).isEmpty()) throw new DataDuplicateException("phone");
    }

    public boolean isMatch(String password1, String password2) {
        return passwordEncoder.matches(password1, password2);
    }


    @Transactional
    public void deleteUser(SiteUser user) {
        userRepository.deleteByUsername(user.getUsername());
    }

    @Transactional
    public void useToPoint(SiteUser user, Long useToPoint) {
        user.setPoint(user.getPoint() - useToPoint);
        this.userRepository.save(user);
    }

}

