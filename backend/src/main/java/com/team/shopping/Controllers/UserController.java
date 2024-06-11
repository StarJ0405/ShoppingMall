package com.team.shopping.Controllers;

import com.team.shopping.DTOs.*;
import com.team.shopping.Exceptions.DataDuplicateException;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.Module.UserService;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final MultiService multiService;

    @PostMapping
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO signupRequestDTO) {
        try {
            multiService.signup(signupRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (DataDuplicateException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }


    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String accessToken, @RequestBody SignupRequestDTO signupRequestDTO) {
        try {
            TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
            if (tokenRecord.isOK()) {
                String username = tokenRecord.username();
                multiService.deleteUser(signupRequestDTO.getUsername(), username);
                return tokenRecord.getResponseEntity("문제 없음");
            }
            return tokenRecord.getResponseEntity();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> profile(@RequestHeader("Authorization") String accessToken) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            // 기능
            UserResponseDTO userResponseDTO = this.multiService.getProfile(username);
            return tokenRecord.getResponseEntity(userResponseDTO);
        }
        return tokenRecord.getResponseEntity();
    }
    

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String accessToken, 
                                           @RequestBody UserRequestDTO userRequestDTO) {

        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            UserResponseDTO userResponseDTO = multiService.updateProfile(username,userRequestDTO);

            return tokenRecord.getResponseEntity(userResponseDTO);
        }
        return tokenRecord.getResponseEntity();

    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestHeader("Authorization") String accessToken , @RequestBody UserRequestDTO userRequestDTO){

        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if(tokenRecord.isOK()){
            String username = tokenRecord.username();
            UserResponseDTO userResponseDTO = multiService.updatePassword(username,userRequestDTO);
            return tokenRecord.getResponseEntity(userResponseDTO);
        }
        return tokenRecord.getResponseEntity();
    }

}