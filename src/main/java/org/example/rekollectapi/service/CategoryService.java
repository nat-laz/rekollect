package org.example.rekollectapi.service;

import org.example.rekollectapi.model.entity.CategoryEntity;

public interface CategoryService {

    CategoryEntity getOrCreateCategory(String categoryName);
}
