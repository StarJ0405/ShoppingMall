package com.team.shopping.Services.Module;

import com.team.shopping.DTOs.ArticleRequestDTO;
import com.team.shopping.Domains.Article;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Enums.Type;
import com.team.shopping.Repositories.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    // set 방식
//    public Article saveCartItemDetail(String title , String content , SiteUser author , Type type) { // 저장
//        Article article = new Article();
//        article.setTitle(title);
//        article.setContent(content);
//        article.setAuthor(author);
//        article.setType(type);
//        article.setCreateDate(LocalDateTime.now());
//
//        return articleRepository.saveCartItemDetail(article);
//
//    }

    //builder 방식
    public Article save(ArticleRequestDTO articleRequestDTO, SiteUser siteUser) {
        return this.articleRepository.save(Article.builder()
                .articleRequestDTO(articleRequestDTO)
                .siteUser(siteUser)
                .build());
    }

    public Article update(Article article, ArticleRequestDTO articleRequestDTO) {// 기존에있는 걸 찾아오고
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

    public Page<Article> getArticleList(Type type, int page) {
        return articleRepository.findByType(type, PageRequest.of(page,5));
    }


}
