package com.team.shopping.DTOs;


import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDTO {

    private String username;

    private String password;

    @Email
    private String email;

    private String nickname;

    private String phoneNumber;

    private int role;

    private String birthday;

    private int gender;


}
