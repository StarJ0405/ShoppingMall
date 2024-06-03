package com.team.shopping.Domains;

import com.team.shopping.Enums.Gender;
import com.team.shopping.Enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class SiteUser {
    @Id
    @Column(length = 24)
    private String username;
    private String password;
    @Column(length = 50)
    private String email;
    @Column(length = 24)
    private String nickname;
    @Column(length = 11)
    private String phoneNumber;
    private UserRole role;
    private LocalDate birthday;
    private Gender gender;
    private int point;
    private LocalDate createDate;
    private LocalDate modifyDate;

    private Auth auth;

    @Builder
    public SiteUser(String username, String password, String nickname, String email, UserRole role, String phoneNumber){
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.phoneNumber = phoneNumber;
    }
}
