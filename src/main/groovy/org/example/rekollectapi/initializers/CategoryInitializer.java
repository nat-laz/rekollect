package org.example.rekollectapi.initializers;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.rekollectapi.models.entities.CategoryEntity;
import org.example.rekollectapi.models.enums.CategoryName;
import org.example.rekollectapi.repositories.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CategoryInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void run(String... args) {
        for (CategoryName categoryName : CategoryName.values()) {
            if (categoryRepository.findByName(categoryName.getName()).isEmpty()) {
                CategoryEntity category = new CategoryEntity();
                category.setName(categoryName.getName());
                categoryRepository.save(category);
            }
        }
    }
}
