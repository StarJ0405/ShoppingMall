package com.team.shopping.Domains;


import com.team.shopping.Enums.Gender;
import com.team.shopping.Enums.UserRole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SiteUser {
    @Id
    @Column(length = 24, unique = true)
    private String username;

    private String password;

    @Column(length = 50 , unique = true)
    private String email;

    @Column(length = 24, unique = true)
    private String nickname;

     @Column(length = 11 , unique = true)
    private String phoneNumber;

    private UserRole role;
    @Column(length = 10)
    private String birthday;

    private Gender gender;

    private int point;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    private String address;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Auth auth;

    @Builder
    public SiteUser(String username, String password, String nickname, String email, UserRole role, Gender gender, String birthday, String phoneNumber){
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.gender = gender;
        this.birthday= birthday;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.createDate = LocalDateTime.now();
    }
}
