package com.abnamro.recipes.service;

import com.abnamro.recipes.data.model.Recipe;

import java.util.List;

public interface RecipeService {

    Recipe createRecipe(Recipe recipe);

    Recipe getRecipe(Long id);

    Recipe updateRecipe(Recipe recipe);

    void deleteRecipe(Long recipeId);

    List<Recipe> getRecipeList(String instructionContains, Integer numberOfServings, String category, String includeIngredients, String excludeIngredients);
}
