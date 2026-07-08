package com.food.recipeflow.service;

import com.food.recipeflow.entity.Recipe;
import com.food.recipeflow.entity.User;
import com.food.recipeflow.repository.RecipeRepository;
import com.food.recipeflow.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe addRecipe(Recipe recipe, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        recipe.setUser(user);
        return recipeRepository.save(recipe);
    }

    public List<Recipe> addRecipes(List<Recipe> recipes, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        recipes.forEach(recipe -> recipe.setUser(user));
        return recipeRepository.saveAll(recipes);
    }

    public Recipe editRecipe(Recipe recipe, Long id) {
        return recipeRepository.findById(id)
                .map(rec -> {

                    rec.setName(recipe.getName());
                    rec.setDescription(recipe.getDescription());
                    rec.setImageUrl(recipe.getImageUrl());
                    rec.setTime(recipe.getTime());
                    rec.setCategory(recipe.getCategory()); // FIXED

                    rec.setEquipments(recipe.getEquipments());
                    rec.setIngredients(recipe.getIngredients());
                    rec.setInstructions(recipe.getInstructions());
                    rec.setTags(recipe.getTags());

                    if (recipe.getLikes() != null) {
                        rec.setLikes(recipe.getLikes());
                    }
                    if (recipe.getDislikes() != null) {
                        rec.setDislikes(recipe.getDislikes());
                    }
                    if (recipe.getComments() != null) {
                        rec.setComments(recipe.getComments());
                    }
                    if (recipe.getViews() != null) {
                        rec.setViews(recipe.getViews());
                    }

                    return recipeRepository.save(rec);
                })
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public Recipe likeRecipe(Long id) {
        recipeRepository.incrementLikes(id);
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    public Recipe dislikeRecipe(Long id) {
        recipeRepository.incrementDislikes(id);
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    public Recipe addComment(Long id) {
        recipeRepository.incrementComments(id);
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }
}
