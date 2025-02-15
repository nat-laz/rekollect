package org.example.rekollectapi.service;


import org.example.rekollectapi.dto.request.CategoryRequestDTO;
import org.example.rekollectapi.dto.response.CategoryResponseDTO;

import java.util.List;


public interface CategoryService {

    CategoryResponseDTO createCategory(CategoryRequestDTO request);

    List<CategoryResponseDTO> getAllCategories();
}
