package com.team.shopping.Services.Module;


import com.team.shopping.DTOs.CategoryRequestDTO;
import com.team.shopping.Domains.Category;
import com.team.shopping.Exceptions.DataDuplicateException;
import com.team.shopping.Repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    //

    public void save(CategoryRequestDTO requestDto) {
        Optional<Category> parent = categoryRepository.findByName(requestDto.getParent());
        categoryRepository.save(Category.builder()
                .name(requestDto.getName())
                .parent(parent.orElse(null))
                .build());
    }

    public Category get(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("아이디가 일치하지 않습니다."));
    }

    public void check(CategoryRequestDTO categoryRequestDTO) {
        if (categoryRequestDTO.getParent() != null && categoryRepository.isDuplicateNameAndParent(categoryRequestDTO.getName(), categoryRequestDTO.getParent()))
            throw new DataDuplicateException("name, parent 중복");
        if (categoryRepository.isDuplicateName(categoryRequestDTO.getName()))
            throw new DataDuplicateException("name 중복");
    }

    public void updateCheck(CategoryRequestDTO requestDto) {
        Optional<Category> _category = categoryRepository.findByName(requestDto.getParent());
        if (_category.isPresent()) {
            Category category = _category.get();
            for (Category child : category.getChildren())
                if (child.getName().equals(requestDto.getName()))
                    throw new DataDuplicateException("name 중복");
        }

        if (categoryRepository.isDuplicateName(requestDto.getNewName()))
            throw new DataDuplicateException("name 중복");
    }

    public void update(Category category, String newName) {
        category.setName(newName);
        category.setModifyDate(LocalDateTime.now());
        categoryRepository.save(category);

    }

    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }

    public List<Category> findByParentIsNull() {
        return categoryRepository.findByParentIsNull();
    }
}
