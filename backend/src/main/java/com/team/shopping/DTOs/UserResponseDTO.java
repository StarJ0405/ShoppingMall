package com.team.shopping.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO {

    private String username;

    private String name;

    private String email;

    private String nickname;

    private String phoneNumber;

    private Long birthday;

    private String gender;

    private String role;

    private Long point;

    private Long createDate;

    private Long modifyDate;

    private String address;

    private String url;



    @Builder
    public UserResponseDTO (String username, String name, String email, String nickname,
                            String phoneNumber, String gender, String role, Long point,
                            String url, Long createDate, Long modifyDate, Long birthday ) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.gender = gender;
        this.role = role;
        this.point = point;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.url = url;

    }
}
