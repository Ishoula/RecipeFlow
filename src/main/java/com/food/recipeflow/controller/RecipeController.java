package com.food.recipeflow.controller;

import com.food.recipeflow.entity.Recipe;
import com.food.recipeflow.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("{id}")
    public Recipe getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    @PostMapping
    public Recipe addRecipe(@Valid @RequestBody Recipe recipe, Principal principal) {
        return recipeService.addRecipe(recipe, principal.getName());
    }

    @PostMapping("/bulk")
    public List<Recipe> addRecipes(@RequestBody List<Recipe> recipes, Principal principal) {
        return recipeService.addRecipes(recipes, principal.getName());
    }

    @PutMapping("{id}")
    public Recipe updateRecipe(@PathVariable Long id, @Valid @RequestBody Recipe recipe) {
        return recipeService.editRecipe(recipe, id);
    }

    @DeleteMapping("{id}")
    public String deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return "Recipe deleted";
    }

    @PostMapping("{id}/like")
    public Recipe likeRecipe(@PathVariable Long id) {
        return recipeService.likeRecipe(id);
    }

    @PostMapping("{id}/dislike")
    public Recipe dislikeRecipe(@PathVariable Long id) {
        return recipeService.dislikeRecipe(id);
    }

    @PostMapping({"{id}/comment", "{id}/comments"})
    public Recipe addComment(@PathVariable Long id) {
        return recipeService.addComment(id);
    }
}
