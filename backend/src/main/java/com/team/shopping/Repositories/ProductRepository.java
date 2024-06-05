package com.team.shopping.Repositories;

import com.team.shopping.Domains.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
