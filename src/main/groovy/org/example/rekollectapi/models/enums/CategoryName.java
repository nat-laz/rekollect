package org.example.rekollectapi.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// Approach 1: Loops through all enum values every time.
//@Getter
//@AllArgsConstructor
//public enum CategoryName {
//    BOOKS("books"),
//    PODCASTS("podcasts"),
//    MOVIES("movies");
//
//    private final String name;
//
//    // Type-Safe Input Validation
//    public static CategoryName fromName(String name) {
//        for (CategoryName categoryName : CategoryName.values()) {
//            if (categoryName.getName().equalsIgnoreCase(name)) {
//                return categoryName;
//            }
//        }
//        throw new IllegalArgumentException("Invalid category name: " + name);
//    }
//}

// Approach 2:  Use a static Map to improve lookup performance.
@Getter
@AllArgsConstructor
public enum CategoryName {
    BOOKS("books"),
    PODCASTS("podcasts"),
    MOVIES("movies");

    private final String name;

    private static final Map<String, CategoryName> NAME_MAP =
            Arrays.stream(values()).collect(Collectors.toMap(CategoryName::getName, Function.identity()));

    public static CategoryName fromName(String name) {
        CategoryName categoryName = NAME_MAP.get(name.toLowerCase());
        if (categoryName == null) {
            throw new IllegalArgumentException("Invalid category name: " + name);
        }
        return categoryName;
    }
}
