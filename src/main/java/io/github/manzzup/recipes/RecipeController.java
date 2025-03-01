package io.github.manzzup.recipes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

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
    public List<Recipe> recipeList(String difficulty) {
		List<Recipe> sortedRecipes = new ArrayList<>(recipes);
        sortedRecipes.sort(null);
		
        if (difficulty == null) {
            return recipes;
        }

		if(difficulty.isEmpty()){
			throw new IllegalArgumentException("Difficulty cannot be empty");
		}

        return recipes.stream()
                .filter(recipe -> recipe.getDifficulty().equals(difficulty))
                .toList();
    }
}
