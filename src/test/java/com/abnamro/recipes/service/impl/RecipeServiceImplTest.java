package com.abnamro.recipes.service.impl;

import com.abnamro.recipes.data.model.Recipe;
import com.abnamro.recipes.exception.RecipeNotFoundException;
import com.abnamro.recipes.mapper.RecipeEntityMapper;
import com.abnamro.recipes.repository.RecipeRepository;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeEntityMapper recipeEntityMapper;

    private RecipeServiceImpl recipeService;

    @BeforeEach
    private void init(){
        recipeService = new RecipeServiceImpl(recipeRepository, recipeEntityMapper);
    }

    @Test
    void testCreateRecipe(){
        //GIVEN
        var id = 1L;
        var name = "Recipe name";
        var numberOfServings = 2;
        var category = Recipe.CategoryEnum.APPETIZER;
        var instructions = "Recipe instructions";
        var ingredients = List.of("Ingredient A", "Ingredient B");

        var recipeRequest = new Recipe();
        recipeRequest.setName(name);
        recipeRequest.setNumberOfServings(numberOfServings);
        recipeRequest.setCategory(category);
        recipeRequest.setInstructions(instructions);
        recipeRequest.setIngredients(ingredients);

        var recipeResponse = new Recipe();
        recipeResponse.setId(id);
        recipeResponse.setName(name);
        recipeResponse.setNumberOfServings(numberOfServings);
        recipeResponse.setCategory(category);
        recipeResponse.setInstructions(instructions);
        recipeResponse.setIngredients(ingredients);

        var recipeEntityRequest = new com.abnamro.recipes.db.model.Recipe();
        recipeEntityRequest.setName(name);
        recipeEntityRequest.setNumberOfServings(numberOfServings);
        recipeEntityRequest.setCategory(category.getValue());
        recipeEntityRequest.setInstructions(instructions);
        recipeEntityRequest.setIngredients(ingredients);

        var recipeEntityResponse = new com.abnamro.recipes.db.model.Recipe();
        recipeEntityRequest.setId(id);
        recipeEntityRequest.setName(name);
        recipeEntityRequest.setNumberOfServings(numberOfServings);
        recipeEntityRequest.setCategory(category.getValue());
        recipeEntityRequest.setInstructions(instructions);
        recipeEntityRequest.setIngredients(ingredients);

        when(recipeEntityMapper.toEntity(Mockito.any(Recipe.class)))
                .thenReturn(recipeEntityRequest);

        when(recipeRepository.save(Mockito.any(com.abnamro.recipes.db.model.Recipe.class)))
                .thenReturn(recipeEntityResponse);

        when(recipeEntityMapper.toApiModel(Mockito.any(com.abnamro.recipes.db.model.Recipe.class)))
                .thenReturn(recipeResponse);

        //WHEN
        val response = recipeService.createRecipe(recipeRequest);

        //THEN
        assertThat(response).isEqualTo(recipeResponse);
    }

    @Test
    void testGetRecipe(){
        //GIVEN
        var id = 1L;
        var name = "Recipe name";
        var numberOfServings = 2;
        var category = Recipe.CategoryEnum.APPETIZER;
        var instructions = "Recipe instructions";
        var ingredients = List.of("Ingredient A", "Ingredient B");

        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName(name);
        recipe.setNumberOfServings(numberOfServings);
        recipe.setCategory(category);
        recipe.setInstructions(instructions);
        recipe.setIngredients(ingredients);

        var recipeEntity = new com.abnamro.recipes.db.model.Recipe();
        recipeEntity.setName(name);
        recipeEntity.setNumberOfServings(numberOfServings);
        recipeEntity.setCategory(category.getValue());
        recipeEntity.setInstructions(instructions);
        recipeEntity.setIngredients(ingredients);

        when(recipeRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(recipeEntity));

        when(recipeEntityMapper.toApiModel(Mockito.any(com.abnamro.recipes.db.model.Recipe.class)))
                .thenReturn(recipe);

        //WHEN
        val response = recipeService.getRecipe(1L);

        //THEN
        assertThat(response).isEqualTo(recipe);
    }

    @Test
    void testGetRecipeNotFound(){
        //GIVEN
        when(recipeRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        //WHEN
        val exception = assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipe(1L));

        //THEN
        assertThat(exception.getMessage()).isEqualTo("Recipe not found. Id: 1");
    }

    @Test
    void testUpdateRecipe(){
        //GIVEN
        var id = 1L;
        var name = "Recipe name";
        var numberOfServings = 2;
        var category = Recipe.CategoryEnum.APPETIZER;
        var instructions = "Recipe instructions";
        var ingredients = List.of("Ingredient A", "Ingredient B");

        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName(name);
        recipe.setNumberOfServings(numberOfServings);
        recipe.setCategory(category);
        recipe.setInstructions(instructions);
        recipe.setIngredients(ingredients);

        var recipeEntity = new com.abnamro.recipes.db.model.Recipe();
        recipeEntity.setName(name);
        recipeEntity.setNumberOfServings(numberOfServings);
        recipeEntity.setCategory(category.getValue());
        recipeEntity.setInstructions(instructions);
        recipeEntity.setIngredients(ingredients);

        when(recipeEntityMapper.toEntity(Mockito.any(Recipe.class)))
                .thenReturn(recipeEntity);

        when(recipeRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(recipeEntity));

        when(recipeRepository.save(Mockito.any(com.abnamro.recipes.db.model.Recipe.class)))
                .thenReturn(recipeEntity);

        when(recipeEntityMapper.toApiModel(Mockito.any(com.abnamro.recipes.db.model.Recipe.class)))
                .thenReturn(recipe);
        //WHEN
        val response = recipeService.updateRecipe(recipe);

        //THEN
        assertThat(response).isEqualTo(recipe);
    }

    @Test
    void testUpdateRecipeNotFound(){
        //GIVEN
        var id = 1L;
        var name = "Recipe name";
        var numberOfServings = 2;
        var category = Recipe.CategoryEnum.APPETIZER;
        var instructions = "Recipe instructions";
        var ingredients = List.of("Ingredient A", "Ingredient B");

        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName(name);
        recipe.setNumberOfServings(numberOfServings);
        recipe.setCategory(category);
        recipe.setInstructions(instructions);
        recipe.setIngredients(ingredients);

        var recipeEntity = new com.abnamro.recipes.db.model.Recipe();
        recipeEntity.setName(name);
        recipeEntity.setNumberOfServings(numberOfServings);
        recipeEntity.setCategory(category.getValue());
        recipeEntity.setInstructions(instructions);
        recipeEntity.setIngredients(ingredients);

        when(recipeEntityMapper.toEntity(Mockito.any(Recipe.class)))
                .thenReturn(recipeEntity);

        when(recipeRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        //WHEN
        val exception = assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(recipe));

        //THEN
        assertThat(exception.getMessage()).isEqualTo("Recipe not found. Id: 1");
    }

    @Test
    void testDeleteRecipe(){
        //GIVEN
        Mockito.doNothing().when(recipeRepository).deleteById(Mockito.anyLong());

        //WHEN
        //THEN
        assertDoesNotThrow(() -> recipeService.deleteRecipe(1L));
    }

    @Test
    void testGetRecipesNotFound(){
        //GIVEN
        when(recipeRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(List.of());

        //WHEN
        val exception = assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipeList(null,null,null,null,null));

        //THEN
        assertThat(exception.getMessage()).isEqualTo("Recipes not found for the current set of filters");
    }

    @Test
    void testGetRecipesNoFilters(){
        //GIVEN
        var id = 1L;
        var name = "Recipe name";
        var numberOfServings = 2;
        var category = Recipe.CategoryEnum.APPETIZER;
        var instructions = "Recipe instructions";
        var ingredients = List.of("Ingredient A", "Ingredient B");

        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName(name);
        recipe.setNumberOfServings(numberOfServings);
        recipe.setCategory(category);
        recipe.setInstructions(instructions);
        recipe.setIngredients(ingredients);

        var recipeEntity = new com.abnamro.recipes.db.model.Recipe();
        recipeEntity.setName(name);
        recipeEntity.setNumberOfServings(numberOfServings);
        recipeEntity.setCategory(category.getValue());
        recipeEntity.setInstructions(instructions);
        recipeEntity.setIngredients(ingredients);

        when(recipeRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(List.of(recipeEntity));

        when(recipeEntityMapper.toApiModel(Mockito.any(com.abnamro.recipes.db.model.Recipe.class)))
                .thenReturn(recipe);

        //WHEN
        val response = recipeService.getRecipeList(null,null,null,null,null);

        //THEN
        assertThat(response).isEqualTo(List.of(recipe));
    }

    @Test
    void testGetRecipesAllFilters(){
        //GIVEN
        var id = 1L;
        var name = "Recipe name";
        var numberOfServings = 2;
        var category = Recipe.CategoryEnum.APPETIZER;
        var instructions = "Recipe instructions";
        var ingredients = List.of("Ingredient A", "Ingredient B");

        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName(name);
        recipe.setNumberOfServings(numberOfServings);
        recipe.setCategory(category);
        recipe.setInstructions(instructions);
        recipe.setIngredients(ingredients);

        var recipeEntity = new com.abnamro.recipes.db.model.Recipe();
        recipeEntity.setName(name);
        recipeEntity.setNumberOfServings(numberOfServings);
        recipeEntity.setCategory(category.getValue());
        recipeEntity.setInstructions(instructions);
        recipeEntity.setIngredients(ingredients);

        when(recipeRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(List.of(recipeEntity));

        when(recipeEntityMapper.toApiModel(Mockito.any(com.abnamro.recipes.db.model.Recipe.class)))
                .thenReturn(recipe);

        //WHEN
        val response = recipeService.getRecipeList("oven",2,"vegetarian","tamarind","chicken");

        //THEN
        assertThat(response).isEqualTo(List.of(recipe));
    }

}
