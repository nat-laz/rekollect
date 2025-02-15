package org.example.rekollectapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


import org.example.rekollectapi.dto.request.CategoryRequestDTO;
import org.example.rekollectapi.dto.response.CategoryResponseDTO;
import org.example.rekollectapi.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CategoryRequestDTO request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }


    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
