package com.team.shopping.Domains;


import com.team.shopping.Enums.Gender;
import com.team.shopping.Enums.UserRole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SiteUser {

    @Id
    @Column(length = 24, unique = true)
    private String username;

    @Column(length = 24)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String password;

    @Column(columnDefinition = "TEXT", unique = true)
    private String email;

    @Column(length = 24, unique = true)
    private String nickname;

    @Column(length = 11 , unique = true)
    private String phoneNumber;

    private UserRole role;

    @Column(length = 10)
    private LocalDate birthday;

    private Gender gender;

    private int point;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Auth auth;

    @Builder
    public SiteUser(String username, String name, String password, String nickname, String email, UserRole role, Gender gender, LocalDate birthday, String phoneNumber){
        this.username = username;
        this.name = name;
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
