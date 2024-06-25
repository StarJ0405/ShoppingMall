package com.team.shopping.Repositories;

import com.team.shopping.Domains.Article;
import com.team.shopping.Enums.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
    Page<Article> findByType(Type type, Pageable pageable);
}
