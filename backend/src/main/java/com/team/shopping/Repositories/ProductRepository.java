package com.team.shopping.Repositories;

import com.team.shopping.Domains.Product;
import com.team.shopping.Repositories.Custom.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
}
