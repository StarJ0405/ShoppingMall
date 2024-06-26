package com.team.shopping.Repositories;

import com.team.shopping.Domains.Article;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Enums.Type;
import com.team.shopping.Repositories.Custom.ArticleRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long>, ArticleRepositoryCustom {
    Page<Article> findByType(Type type, Pageable pageable);
}
