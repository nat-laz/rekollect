package org.example.rekollectapi.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryName {
    BOOKS("books"),
    PODCASTS("podcasts"),
    MOVIES("movies");

    private final String name;

    // Type-Safe Input Validation
    public static CategoryName fromName(String name) {
        for (CategoryName categoryName : CategoryName.values()) {
            if (categoryName.getName().equalsIgnoreCase(name)) {
                return categoryName;
            }
        }
        throw new IllegalArgumentException("Invalid category name: " + name);
    }
}
