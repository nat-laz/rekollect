package org.example.rekollectapi.service.impl;

import lombok.AllArgsConstructor;
import org.example.rekollectapi.exceptions.ValidationException;
import org.example.rekollectapi.model.entity.CategoryEntity;
import org.example.rekollectapi.repository.CategoryRepository;
import org.example.rekollectapi.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryEntity getOrCreateCategory(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new ValidationException("Category name cannot be null or empty.");
        }

        return categoryRepository.findByCategoryNameIgnoreCase(categoryName)
                .orElseGet(() -> categoryRepository.save(new CategoryEntity(null, categoryName)));
    }
}
