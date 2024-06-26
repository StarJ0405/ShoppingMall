package com.team.shopping.Controllers;

import com.team.shopping.DTOs.ArticleRequestDTO;
import com.team.shopping.DTOs.ArticleResponseDTO;
import com.team.shopping.Domains.Article;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/article")
public class ArticleController {
    private final MultiService multiService;

    @PostMapping
    public ResponseEntity<?> createArticle(@RequestHeader("Authorization") String accessToken, @RequestBody ArticleRequestDTO articleRequestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            ArticleResponseDTO articleResponseDTO = this.multiService.saveArticle(username, articleRequestDTO);
            return tokenRecord.getResponseEntity(articleResponseDTO);
        }
        return tokenRecord.getResponseEntity();
    }


    @PutMapping
    public ResponseEntity<?> updateArticle(@RequestHeader("Authorization") String accessToken, @RequestBody ArticleRequestDTO articleRequestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            ArticleResponseDTO articleResponseDTO = this.multiService.updateArticle(username, articleRequestDTO);
            return tokenRecord.getResponseEntity(articleResponseDTO);
        }
        return tokenRecord.getResponseEntity();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteArticle(@RequestHeader("Authorization") String accessToken, @RequestHeader("ArticleId") Long articleId) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            this.multiService.deleteArticle(username, articleId);
            return tokenRecord.getResponseEntity("article delete complete");
        }
        return tokenRecord.getResponseEntity();
    }

    @GetMapping
    public ResponseEntity<?> GetArticleList(@RequestHeader("Type") int type, @RequestHeader("Page") int page) {
        Page<ArticleResponseDTO> articleList = this.multiService.getArticleList(type, page);
        return ResponseEntity.status(HttpStatus.OK).body(articleList);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyArticle (@RequestHeader("Authorization") String accessToken,
                                           @RequestHeader("Type") int type) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            List<ArticleResponseDTO> articleResponseDTOList = this.multiService.getMyArticle(username, type);
            return tokenRecord.getResponseEntity(articleResponseDTOList);
        }
        return tokenRecord.getResponseEntity();
    }
}



