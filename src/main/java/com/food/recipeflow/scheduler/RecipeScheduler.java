package com.food.recipeflow.scheduler;

import com.food.recipeflow.entity.Recipe;
import com.food.recipeflow.entity.User;
import com.food.recipeflow.repository.RecipeRepository;
import com.food.recipeflow.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecipeScheduler {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeScheduler(UserRepository userRepository, RecipeRepository recipeRepository){
        this.recipeRepository=recipeRepository;
        this.userRepository=userRepository;

    }

    // Disabled for performance, uncomment and configure if needed
    // @Scheduled(fixedRate = 3600000) // Example: every 1 hour
    public void yourRecipes(){

        List<User> users=userRepository.findAll();
        for(User user: users){

            List<Recipe> recipes=recipeRepository.findByUserId(user.getId());
            for(Recipe recipe: recipes){
                System.out.println("Recipes for: "+user.getUsername()+"..........");
                System.out.println("Recipe Name: "+recipe.getName());
            }
        }
    }
}
