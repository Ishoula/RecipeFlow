package com.food.recipeflow.controller;

import com.food.recipeflow.entity.Recipe;
import com.food.recipeflow.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getAllRecipes(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        if (page != null && size != null) {
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);
            return ResponseEntity.ok(recipeService.getAllRecipes(pageable));
        } else {
            return ResponseEntity.ok(recipeService.getAllRecipes());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        Recipe recipe = recipeService.getRecipeById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        return ResponseEntity.ok(recipe);
    }

    @PostMapping
    public ResponseEntity<Recipe> addRecipe(@Valid @RequestBody Recipe recipe, Principal principal) {
        Recipe createdRecipe = recipeService.addRecipe(recipe, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Recipe>> addRecipes(@RequestBody List<Recipe> recipes, Principal principal) {
        List<Recipe> createdRecipes = recipeService.addRecipes(recipes, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipes);
    }

    @PutMapping("{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @Valid @RequestBody Recipe recipe) {
        Recipe editedRecipe = recipeService.editRecipe(recipe, id);
        return ResponseEntity.ok(editedRecipe);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.ok("Recipe deleted");
    }

    @PostMapping("{id}/like")
    public ResponseEntity<Recipe> likeRecipe(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.likeRecipe(id));
    }

    @PostMapping("{id}/dislike")
    public ResponseEntity<Recipe> dislikeRecipe(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.dislikeRecipe(id));
    }

    @PostMapping({ "{id}/comment", "{id}/comments" })
    public ResponseEntity<Recipe> addComment(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.addComment(id));
    }
}
