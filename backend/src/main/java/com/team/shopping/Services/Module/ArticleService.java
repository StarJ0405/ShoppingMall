package com.team.shopping.Services.Module;

import com.team.shopping.DTOs.ArticleRequestDTO;
import com.team.shopping.Domains.Article;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Enums.Type;
import com.team.shopping.Repositories.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    // set 방식
//    public Article save(String title , String content , SiteUser author , Type type) { // 저장
//        Article article = new Article();
//        article.setTitle(title);
//        article.setContent(content);
//        article.setAuthor(author);
//        article.setType(type);
//        article.setCreateDate(LocalDateTime.now());
//
//        return articleRepository.save(article);
//
//    }

    //builder 방식
    public Article save(ArticleRequestDTO articleRequestDTO, SiteUser siteUser) {
        return this.articleRepository.save(Article.builder()
                .articleRequestDTO(articleRequestDTO)
                .siteUser(siteUser)
                .build());
    }

    public Article update(ArticleRequestDTO articleRequestDTO) {
        Article article = this.articleRepository.findById(articleRequestDTO.getArticleId()).orElseThrow();// 기존에있는 걸 찾아오고
        article.setTitle(articleRequestDTO.getTitle());
        article.setContent(articleRequestDTO.getContent());
        article.setModifyDate(LocalDateTime.now());
        return this.articleRepository.save(article);

    }


    public void delete(Long articleId) {
        this.articleRepository.deleteById(articleId);
    }

    public Article get(Long articleId) { // article id 로 게시물 하나만 찾아오는 메서드
        return this.articleRepository.findById(articleId).orElseThrow();
    }

    public List<Article> getArticleList(Type type) {

        return articleRepository.findByType(type);
    }


}
