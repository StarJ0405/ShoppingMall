package com.team.shopping.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO {

    private String username;

    private String name;

    private String email;

    private String nickname;

    private String phoneNumber;

    private String birthday;

    private String gender;

    private int point;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    private String address;

    @Builder
    public UserResponseDTO (String username, String name, String email, String nickname,
                            String phoneNumber, String birthday, String gender, int point,
                            LocalDateTime createDate, LocalDateTime modifyDate) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.gender = gender;
        this.point = point;
        this.createDate = createDate;
        this.modifyDate = modifyDate;

    }
}
