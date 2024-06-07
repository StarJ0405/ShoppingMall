package com.team.shopping.DTOs;


import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDTO {

    private String username;

    private String password;

    private String name;

    @Email
    private String email;

    private String nickname;

    private String phoneNumber;

    private int role;

    private LocalDate birthday;

    private int gender;
}
