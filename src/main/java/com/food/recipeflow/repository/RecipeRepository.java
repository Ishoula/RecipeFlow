package com.food.recipeflow.repository;

import com.food.recipeflow.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByNameContainingIgnoreCase(String name);
    List<Recipe> findByCategoryIgnoreCase(String category);
    List<Recipe> findByNameContainingIgnoreCaseAndCategoryIgnoreCase(String name, String category);
    List<Recipe> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Recipe r SET r.likes = r.likes + 1 WHERE r.id = :id")
    void incrementLikes(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Recipe r SET r.dislikes = r.dislikes + 1 WHERE r.id = :id")
    void incrementDislikes(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Recipe r SET r.comments = r.comments + 1 WHERE r.id = :id")
    void incrementComments(@Param("id") Long id);
}
