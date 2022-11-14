package com.abnamro.recipes.repository;


import com.abnamro.recipes.db.model.Recipe;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {
}