package com.abnamro.recipes.controller;

import com.abnamro.recipes.data.model.Recipe;
import com.abnamro.recipes.service.RecipeService;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @Mock
    private RecipeService recipeServiceMock;

    private RecipeController recipeController;

    @BeforeEach
    private void init(){
        recipeController = new RecipeController(recipeServiceMock);
    }

    @Test
    void testGetRecipeById(){
        //GIVEN
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Recipe name");
        recipe.setNumberOfServings(2);
        recipe.setCategory(Recipe.CategoryEnum.APPETIZER);
        recipe.setInstructions("Recipe instructions");
        recipe.setIngredients(List.of("Ingredient A", "Ingredient B"));

        when(recipeServiceMock.getRecipe(Mockito.anyLong()))
                .thenReturn(recipe);

        //WHEN
        val response = recipeController.getRecipeById(1L);

        //THEN
        assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatusCodeValue());
        assertThat(response.getBody()).isEqualTo(recipe);
    }

    @Test
    void testCreateRecipe(){
        //GIVEN
        var name = "Recipe name";
        var numberOfServings = 2;
        var category = Recipe.CategoryEnum.APPETIZER;
        var instructions = "Recipe instructions";
        var ingredients = List.of("Ingredient A", "Ingredient B");

        Recipe recipeRequest = new Recipe();
        recipeRequest.setName(name);
        recipeRequest.setNumberOfServings(numberOfServings);
        recipeRequest.setCategory(category);
        recipeRequest.setInstructions(instructions);
        recipeRequest.setIngredients(ingredients);

        Recipe recipeResponse = new Recipe();
        recipeResponse.setId(1L);
        recipeResponse.setName(name);
        recipeResponse.setNumberOfServings(numberOfServings);
        recipeResponse.setCategory(category);
        recipeResponse.setInstructions(instructions);
        recipeResponse.setIngredients(ingredients);

        when(recipeServiceMock.createRecipe(any(Recipe.class)))
                .thenReturn(recipeResponse);

        //WHEN
        val response = recipeController.createRecipe(recipeRequest);

        //THEN
        assertThat(HttpStatus.CREATED.value()).isEqualTo(response.getStatusCodeValue());
        assertThat(response.getBody()).isEqualTo(recipeResponse);
    }

    @Test
    void deleteRecipe(){
        //GIVEN
        Mockito.doNothing().when(recipeServiceMock).deleteRecipe(Mockito.anyLong());

        //WHEN
        val response = recipeController.deleteRecipe(1L);

        //THEN
        assertThat(HttpStatus.NO_CONTENT.value()).isEqualTo(response.getStatusCodeValue());
    }

    @Test
    void updateRecipe(){
        //GIVEN
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Recipe name");
        recipe.setNumberOfServings(2);
        recipe.setCategory(Recipe.CategoryEnum.APPETIZER);
        recipe.setInstructions("Recipe instructions");
        recipe.setIngredients(List.of("Ingredient A", "Ingredient B"));

        when(recipeServiceMock.updateRecipe(any(Recipe.class)))
                .thenReturn(recipe);

        //WHEN
        val response = recipeController.updateRecipe(recipe);

        //THEN
        assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatusCodeValue());
        assertThat(response.getBody()).isEqualTo(recipe);
    }

    @Test
    void testGetRecipeList(){
        //GIVEN
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Recipe name");
        recipe.setNumberOfServings(2);
        recipe.setCategory(Recipe.CategoryEnum.APPETIZER);
        recipe.setInstructions("Recipe instructions");
        recipe.setIngredients(List.of("Ingredient A", "Ingredient B"));

        when(recipeServiceMock.getRecipeList((String) any(), (Integer) any(), (String) any(), (String) any(), (String) any()))
                .thenReturn(List.of(recipe));

        //WHEN
        val response = recipeController.getRecipeList(null,null,null,null,null);

        //THEN
        assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatusCodeValue());
        assertThat(response.getBody()).isEqualTo(List.of(recipe));
    }
}
