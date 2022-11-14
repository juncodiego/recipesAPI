package com.abnamro.recipes.mapper;

import com.abnamro.recipes.data.model.Recipe;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RecipeEntityMapperTest {

    private RecipeEntityMapper recipeEntityMapper;

    @BeforeEach
    private void init(){
        recipeEntityMapper = new RecipeEntityMapper();
    }

    @Test
    void testToEntity() throws JsonProcessingException {
        //GIVEN
        ObjectMapper mapper = new ObjectMapper();
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Recipe name");
        recipe.setNumberOfServings(2);
        recipe.setCategory(Recipe.CategoryEnum.APPETIZER);
        recipe.setInstructions("Recipe instructions");
        recipe.setIngredients(List.of("Ingredient A", "Ingredient B"));

        //WHEN
        val response = recipeEntityMapper.toEntity(recipe);

        //THEN
        assertThat(response.getId()).isEqualTo(recipe.getId());
        assertThat(response.getName()).isEqualTo(recipe.getName());
        assertThat(response.getNumberOfServings()).isEqualTo(recipe.getNumberOfServings());
        assertThat(response.getCategory()).isEqualTo(recipe.getCategory().getValue());
        assertThat(response.getIngredients()).isEqualTo(recipe.getIngredients());
        assertThat(response.getInstructions()).isEqualTo(recipe.getInstructions());
    }

    @Test
    void testToApiModel() throws JsonProcessingException {
        //GIVEN
        ObjectMapper mapper = new ObjectMapper();
        var recipe = new com.abnamro.recipes.db.model.Recipe();
        recipe.setId(1L);
        recipe.setName("Recipe name");
        recipe.setNumberOfServings(2);
        recipe.setCategory("appetizer");
        recipe.setInstructions("Recipe instructions");
        recipe.setIngredients(List.of("Ingredient A","Ingredient B"));

        //WHEN
        val response = recipeEntityMapper.toApiModel(recipe);

        //THEN
        assertThat(response.getId()).isEqualTo(recipe.getId());
        assertThat(response.getName()).isEqualTo(recipe.getName());
        assertThat(response.getNumberOfServings()).isEqualTo(recipe.getNumberOfServings());
        assertThat(response.getCategory()).isEqualTo(Recipe.CategoryEnum.fromValue(recipe.getCategory()));
        assertThat(response.getIngredients()).isEqualTo(recipe.getIngredients());
        assertThat(response.getInstructions()).isEqualTo(recipe.getInstructions());
    }
}
