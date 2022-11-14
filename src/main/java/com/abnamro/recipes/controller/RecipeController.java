package com.abnamro.recipes.controller;

import com.abnamro.recipes.data.model.Recipe;
import com.abnamro.recipes.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecipeController implements RecipeApi {

    private final RecipeService recipeService;

    @Override
    public ResponseEntity<Recipe> getRecipeById(final Long recipeId) {
        var recipe = recipeService.getRecipe(recipeId);
        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Recipe> createRecipe(final Recipe recipe) {
        val recipeCreated = recipeService.createRecipe(recipe);
        return new ResponseEntity<>(recipeCreated, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteRecipe(final Long recipeId) {
        recipeService.deleteRecipe(recipeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Recipe> updateRecipe(final Recipe recipe) {
        val recipeUpdate = recipeService.updateRecipe(recipe);
        return new ResponseEntity<>(recipeUpdate, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Recipe>> getRecipeList(final String instructionContains, final Integer numberOfServings, final String category, final String includeIngredients, final String excludeIngredients) {
        val recipeUpdate = recipeService.getRecipeList(instructionContains, numberOfServings, category, includeIngredients, excludeIngredients);
        return new ResponseEntity<>(recipeUpdate, HttpStatus.OK);
    }
}
