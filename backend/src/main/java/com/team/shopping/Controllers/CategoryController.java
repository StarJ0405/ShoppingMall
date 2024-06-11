package com.team.shopping.Controllers;

import com.team.shopping.DTOs.AuthResponseDTO;
import com.team.shopping.DTOs.CategoryRequestDTO;
import com.team.shopping.DTOs.CategoryResponseDTO;
import com.team.shopping.Exceptions.DataDuplicateException;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final MultiService multiService;


    @PostMapping
    public ResponseEntity<?> createCategory(@RequestHeader("Authorization") String accessToken,
                                            @RequestBody CategoryRequestDTO requestDto) {
        try {
            TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
            if (tokenRecord.isOK()) {
                String username = tokenRecord.username();
                this.multiService.saveCategory(username, requestDto);
                return tokenRecord.getResponseEntity("문제 없음");
            }
            return tokenRecord.getResponseEntity();
        } catch (DataDuplicateException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCategory(@RequestHeader("Authorization") String accessToken,
                                            @RequestBody CategoryRequestDTO categoryRequestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            multiService.updateCategory(username, categoryRequestDTO);
            return tokenRecord.getResponseEntity("문제 없음");
        }
        return tokenRecord.getResponseEntity();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCategory(@RequestHeader("Authorization") String accessToken,
                                            @RequestBody CategoryRequestDTO categoryRequestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            try {multiService.deleteCategory(tokenRecord.username(), categoryRequestDTO.getId());
                return ResponseEntity.ok("카테고리가 성공적으로 삭제되었습니다.");
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.status(404).body(ex.getMessage());
            } catch (Exception ex) {
                return ResponseEntity.status(500).body("카테고리 삭제 중 오류가 발생했습니다.");
            }
        }
        return tokenRecord.getResponseEntity();
    }

    @GetMapping
    public ResponseEntity<?> getCategory() {
        try {
            List<CategoryResponseDTO> categoryResponseDTOList = multiService.getCategoryList();
            return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDTOList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }







}
