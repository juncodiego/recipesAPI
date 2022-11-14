package com.abnamro.recipes.mapper;

import com.abnamro.recipes.data.model.Recipe;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class RecipeEntityMapper {

    public com.abnamro.recipes.db.model.Recipe toEntity(com.abnamro.recipes.data.model.Recipe recipe) {
        return new com.abnamro.recipes.db.model.Recipe(recipe.getId(), recipe.getName(), recipe.getNumberOfServings(), recipe.getCategory().getValue(), recipe.getIngredients(), recipe.getInstructions());
    }

    public com.abnamro.recipes.data.model.Recipe toApiModel(com.abnamro.recipes.db.model.Recipe recipe) {
        val apiRecipe = new com.abnamro.recipes.data.model.Recipe();
        apiRecipe.setId(recipe.getId());
        apiRecipe.setName(recipe.getName());
        apiRecipe.setIngredients(recipe.getIngredients());
        apiRecipe.setInstructions(recipe.getInstructions());
        apiRecipe.setCategory(Recipe.CategoryEnum.fromValue(recipe.getCategory()));
        apiRecipe.setNumberOfServings(recipe.getNumberOfServings());
        return apiRecipe;
    }
}
