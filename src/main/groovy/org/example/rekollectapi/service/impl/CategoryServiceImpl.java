package org.example.rekollectapi.service.impl;

import lombok.AllArgsConstructor;
import org.example.rekollectapi.dto.request.CategoryRequestDTO;
import org.example.rekollectapi.dto.response.CategoryResponseDTO;
import org.example.rekollectapi.exceptions.BadRequestException;
import org.example.rekollectapi.model.entity.CategoryEntity;
import org.example.rekollectapi.repository.CategoryRepository;
import org.example.rekollectapi.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException("Category already exists: " + request.getName());
        }

        CategoryEntity category = new CategoryEntity();
        category.setName(request.getName());
        categoryRepository.save(category);

        return new CategoryResponseDTO(category.getId(), category.getName());
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> new CategoryResponseDTO(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }
}
