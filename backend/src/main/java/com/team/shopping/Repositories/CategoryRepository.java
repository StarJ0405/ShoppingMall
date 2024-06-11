package com.team.shopping.Repositories;

import com.team.shopping.Domains.Category;
import com.team.shopping.Repositories.Custom.CategoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category , Long>, CategoryRepositoryCustom {
    Optional<Category> findByName(String parent);
    List<Category> findByParentIsNull();
}
