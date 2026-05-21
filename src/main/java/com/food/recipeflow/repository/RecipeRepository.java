package com.food.recipeflow.repository;

import com.food.recipeflow.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByNameContainingIgnoreCase(String name);
    List<Recipe> findByCategoryIgnoreCase(String category);
    List<Recipe> findByNameContainingIgnoreCaseAndCategoryIgnoreCase(String name, String category);

}
