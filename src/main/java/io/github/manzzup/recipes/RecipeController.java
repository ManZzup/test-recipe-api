package io.github.manzzup.recipes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class RecipeController {

    private final List<Recipe> recipes;

    @Autowired
    private ResourceLoader resourceLoader;

    public RecipeController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.recipes = loadRecipes();
    }

    private List<Recipe> loadRecipes() {
        try {
            InputStream inputStream = resourceLoader.getResource("classpath:recipes.json").getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<List<Recipe>> typeReference = new TypeReference<>() {};
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load recipes from JSON", e);
        }
    }

    @GetMapping("/recipes")
    public List<Recipe> index() {
        return recipes;
    }
}
