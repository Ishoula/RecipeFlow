package com.food.recipeflow.service;

import com.food.recipeflow.entity.Recipe;
import com.food.recipeflow.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public List<Recipe> addRecipes(List<Recipe> recipes) {
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

                    rec.setLikes(recipe.getLikes());
                    rec.setDislikes(recipe.getDislikes());
                    rec.setComments(recipe.getComments());
                    rec.setViews(recipe.getViews());

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
        return recipeRepository.findById(id)
                .map(recipe -> {
                    recipe.setLikes(recipe.getLikes() + 1);
                    return recipeRepository.save(recipe);
                })
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    public Recipe dislikeRecipe(Long id) {
        return recipeRepository.findById(id)
                .map(recipe -> {
                    recipe.setDislikes(recipe.getDislikes() + 1);
                    return recipeRepository.save(recipe);
                })
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    public Recipe addComment(Long id) {
        return recipeRepository.findById(id)
                .map(recipe -> {
                    recipe.setComments(recipe.getComments() + 1);
                    return recipeRepository.save(recipe);
                })
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }
}