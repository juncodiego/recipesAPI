package com.abnamro.recipes.service.impl;

import com.abnamro.recipes.data.model.Recipe;
import com.abnamro.recipes.exception.RecipeNotFoundException;
import com.abnamro.recipes.mapper.RecipeEntityMapper;
import com.abnamro.recipes.repository.RecipeRepository;
import com.abnamro.recipes.service.RecipeService;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
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
        final Specification<com.abnamro.recipes.db.model.Recipe> spec = (root, query, cb) -> getRecipeSpecification(root, cb, instructionContains, numberOfServings, category, includeIngredients, excludeIngredients);
        final var response = recipeRepository.findAll(spec);
        if(response.isEmpty()){
            throw new RecipeNotFoundException("Recipes not found for the current set of filters");
        }
        return response.stream().map(recipeEntityMapper::toApiModel).toList();
    }

    private Predicate getRecipeSpecification(final Root<com.abnamro.recipes.db.model.Recipe> root, final CriteriaBuilder cb, final String instructionContains, final Integer numberOfServings, final String category, final String includeIngredients, final String excludeIngredients){
        final List<Predicate> predicates = new ArrayList<>();
        getInstructionContainsPredicate(instructionContains, root, cb).ifPresent(predicates::add);
        getNumberOfServingsPredicate(numberOfServings, root, cb).ifPresent(predicates::add);
        getCategoryPredicate(category, root, cb).ifPresent(predicates::add);
        getIncludeIngredientsPredicate(includeIngredients, root, cb).ifPresent(predicates::add);
        getExcludeIngredientsPredicate(excludeIngredients, root, cb).ifPresent(predicates::add);
        return cb.and(predicates.toArray(new Predicate[]{}));
    }

    private Optional<Predicate> getInstructionContainsPredicate(final String instructionContains, final Root<com.abnamro.recipes.db.model.Recipe> root, final CriteriaBuilder cb){
        if (StringUtils.hasText(instructionContains)) {
            final String lowerInstructions = instructionContains.toLowerCase();
            return Optional.of(cb.like(cb.lower(root.get(com.abnamro.recipes.db.model.Recipe.Fields.instructions)), "%" + lowerInstructions + "%"));
        }
        return Optional.empty();
    }

    private Optional<Predicate> getNumberOfServingsPredicate(final Integer numberOfServings, final Root<com.abnamro.recipes.db.model.Recipe> root, final CriteriaBuilder cb){
        if(Optional.ofNullable(numberOfServings).isPresent()){
            return Optional.of(cb.equal(root.get(com.abnamro.recipes.db.model.Recipe.Fields.numberOfServings), numberOfServings));
        }
        return Optional.empty();
    }
    private Optional<Predicate> getCategoryPredicate(final String category, final Root<com.abnamro.recipes.db.model.Recipe> root, final CriteriaBuilder cb){
        if (StringUtils.hasText(category)) {
            final String lowerCategory = category.toLowerCase();
            return Optional.of(cb.equal(cb.lower(root.get(com.abnamro.recipes.db.model.Recipe.Fields.category)), lowerCategory));
        }
        return Optional.empty();
    }

    private Optional<Predicate> getIncludeIngredientsPredicate(final String includeIngredients, final Root<com.abnamro.recipes.db.model.Recipe> root, final CriteriaBuilder cb){
        if (StringUtils.hasText(includeIngredients)) {
            var containsExpression = getIngredientsContainsExpression(includeIngredients, root, cb);
            return Optional.of(cb.isTrue(containsExpression));
        }
        return Optional.empty();
    }

    private Optional<Predicate> getExcludeIngredientsPredicate(final String excludeIngredients, final Root<com.abnamro.recipes.db.model.Recipe> root, final CriteriaBuilder cb){
        if (StringUtils.hasText(excludeIngredients)) {
            var containsExpression = getIngredientsContainsExpression(excludeIngredients, root, cb);
            return Optional.of(cb.isFalse(containsExpression));
        }
        return Optional.empty();
    }

    private Expression<Boolean> getIngredientsContainsExpression(final String ingredients, final Root<com.abnamro.recipes.db.model.Recipe> root, final CriteriaBuilder cb){
        final var ingredientsList = Arrays.asList(ingredients.split(","));
        Expression<?> jsonb = cb.function(
            "jsonb_build_array",
            Object.class,
            cb.literal(ingredientsList)
        );
        return cb.function("jsonb_contains",
            Boolean.class,
            root.get(com.abnamro.recipes.db.model.Recipe.Fields.ingredients),
            jsonb
        );
    }
}
