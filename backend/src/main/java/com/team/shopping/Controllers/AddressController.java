package com.team.shopping.Controllers;

import com.team.shopping.DTOs.AddressRequestDTO;
import com.team.shopping.DTOs.AddressResponseDTO;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final MultiService multiService;

    @PostMapping
    public ResponseEntity<?> createAddress (@RequestHeader("Authorization") String accessToken,
                                            @RequestBody AddressRequestDTO addressRequestDTO) {

            TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
            if (tokenRecord.isOK()) {
                String username = tokenRecord.username();
                AddressResponseDTO addressResponseDTO = this.multiService.createAddress(username, addressRequestDTO);
                return tokenRecord.getResponseEntity(addressResponseDTO);
            }
            return tokenRecord.getResponseEntity();
    }

    @GetMapping
    public ResponseEntity<?> addressList (@RequestHeader("Authorization") String accessToken) {
        try {
            TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
            if (tokenRecord.isOK()) {
                String username = tokenRecord.username();
                List<AddressResponseDTO> addressResponseDTOList = this.multiService.getAddressList(username);
                return tokenRecord.getResponseEntity(addressResponseDTOList);
            }
            return tokenRecord.getResponseEntity();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("저장된 주소를 찾을 수 없음");
        }
    }

    @PutMapping
    public ResponseEntity<?> updateAddress (@RequestHeader("Authorization") String accessToken,
                                            @RequestBody AddressRequestDTO addressRequestDTO) {
        try {
            TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
            if (tokenRecord.isOK()) {
                String username = tokenRecord.username();
                AddressResponseDTO addressResponseDTO = this.multiService.updateAddress(username, addressRequestDTO);
                return tokenRecord.getResponseEntity(addressResponseDTO);
            }
            return tokenRecord.getResponseEntity();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("저장된 주소를 찾을 수 없음");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAddress (@RequestHeader("Authorization") String accessToken,
                                         @RequestHeader("AddressIdList") List<Long> addressIdList) {
        try {
            TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
            if (tokenRecord.isOK()) {
                String username = tokenRecord.username();
                this.multiService.deleteAddress(username, addressIdList);
                return tokenRecord.getResponseEntity("정상적으로 삭제됨");
            }
            return tokenRecord.getResponseEntity();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("저장된 주소를 찾을 수 없음");
        }
    }
}
