package com.team.shopping;

import com.team.shopping.Domains.Category;
import com.team.shopping.Repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShoppingApplicationTests {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void insertData() {
        for (int i = 0; i < 5; i++) {
            Category firstCategory = categoryRepository.save(Category.builder().name("first" + i).build());
            for (int j = 0; j < 5; j++) {
                Category secondCategory = categoryRepository.save(Category.builder().name("second" + j).parent(firstCategory).build());
                for (int k = 0; k < 10; k++)
                    categoryRepository.save(Category.builder().name("category" + k).parent(secondCategory).build());
            }
        }
    }

}
