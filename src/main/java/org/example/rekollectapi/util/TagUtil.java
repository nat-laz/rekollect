package org.example.rekollectapi.util;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TagUtil {

    public TagUtil() {
    }

    public static Set<String> sanitizedTagNames(List<String> tagNames) {
        return tagNames.stream()
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .collect(Collectors.toSet());
    }

}
