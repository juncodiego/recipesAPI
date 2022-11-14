package com.abnamro.recipes.service.impl;

import com.abnamro.recipes.data.model.Recipe;
import com.abnamro.recipes.exception.RecipeNotFoundException;
import com.abnamro.recipes.mapper.RecipeEntityMapper;
import com.abnamro.recipes.repository.RecipeRepository;
import com.abnamro.recipes.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeEntityMapper recipeEntityMapper;

    @Override
    public Recipe createRecipe(final Recipe recipe) {
        val recipeEntity = recipeEntityMapper.toEntity(recipe);
        return recipeEntityMapper.toApiModel(recipeRepository.save(recipeEntity));
    }

    @Override
    public Recipe getRecipe(final Long id) {
        return recipeRepository.findById(id).map(recipeEntityMapper::toApiModel)
                .orElseThrow( () -> new RecipeNotFoundException(id));
    }

    @Override
    public Recipe updateRecipe(final Recipe newRecipe) {
        val newRecipeEntity = recipeEntityMapper.toEntity(newRecipe);
        return recipeRepository.findById(newRecipe.getId())
                .map(recipe -> {
                    recipe.setName(newRecipeEntity.getName());
                    recipe.setNumberOfServings(newRecipeEntity.getNumberOfServings());
                    recipe.setCategory(newRecipeEntity.getCategory());
                    recipe.setInstructions(newRecipeEntity.getInstructions());
                    recipe.setIngredients(newRecipeEntity.getIngredients());
                    return recipeEntityMapper.toApiModel(recipeRepository.save(recipe));
                })
                .orElseThrow( () -> new RecipeNotFoundException(newRecipe.getId()));
    }

    @Override
    public void deleteRecipe(final Long recipeId) {
        recipeRepository.deleteById(recipeId);
    }

    @Override
    public List<Recipe> getRecipeList(final String instructionContains, final Integer numberOfServings, final String category, final String includeIngredients, final String excludeIngredients) {
        final Specification<com.abnamro.recipes.db.model.Recipe> spec = (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(instructionContains)) {
                final String lowerInstructions = instructionContains.toLowerCase();
                predicates.add(cb.like(cb.lower(root.get(com.abnamro.recipes.db.model.Recipe.Fields.instructions)), "%" + lowerInstructions + "%"));
            }
            if(Optional.ofNullable(numberOfServings).isPresent()){
                predicates.add(cb.equal(root.get(com.abnamro.recipes.db.model.Recipe.Fields.numberOfServings), numberOfServings));
            }
            if (StringUtils.hasText(category)) {
                final String lowerCategory = category.toLowerCase();
                predicates.add(cb.equal(cb.lower(root.get(com.abnamro.recipes.db.model.Recipe.Fields.category)), lowerCategory));
            }
            if (StringUtils.hasText(includeIngredients)) {
                final var includeIngredientsList = Arrays.asList(includeIngredients.split(","));
                Expression<?> jsonb = cb.function(
                        "jsonb_build_array",
                        Object.class,
                        cb.literal(includeIngredientsList)
                );
                var containsExpression = cb.function("jsonb_contains",
                        Boolean.class,
                        root.get(com.abnamro.recipes.db.model.Recipe.Fields.ingredients),
                        jsonb
                        );
                predicates.add(cb.isTrue(containsExpression));
            }
            if (StringUtils.hasText(excludeIngredients)) {
                final var excludeIngredientsList = Arrays.asList(excludeIngredients.split(","));
                Expression<?> jsonb = cb.function(
                        "jsonb_build_array",
                        Object.class,
                        cb.literal(excludeIngredientsList)
                );
                var containsExpression = cb.function("jsonb_contains",
                        Boolean.class,
                        root.get(com.abnamro.recipes.db.model.Recipe.Fields.ingredients),
                        jsonb
                );
                predicates.add(cb.isFalse(containsExpression));
            }

            return cb.and(predicates.toArray(new Predicate[]{}));
        };

        final var response = recipeRepository.findAll(spec);
        if(response.isEmpty()){
            throw new RecipeNotFoundException("Recipes not found for the current set of filters");
        }

        return response.stream().map(recipeEntityMapper::toApiModel).toList();
    }
}
