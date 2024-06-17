package com.team.shopping.Repositories;

import com.team.shopping.Domains.Article;
import com.team.shopping.Enums.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
    List<Article> findByType(Type type);
}
