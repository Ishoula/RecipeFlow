package com.food.recipeflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RecipeflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeflowApplication.class, args);
	}

}
