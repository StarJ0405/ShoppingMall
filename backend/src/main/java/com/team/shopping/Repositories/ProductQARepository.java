package com.team.shopping.Repositories;

import com.team.shopping.Domains.ProductQA;
import com.team.shopping.Repositories.Custom.ProductQARepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductQARepository extends JpaRepository<ProductQA, Long>, ProductQARepositoryCustom {
}
