package com.abnamro.recipes.exception;

public class RecipeNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Recipe not found. Id: ";

    public RecipeNotFoundException(Long id){
        super(MESSAGE + id);
    }

    public RecipeNotFoundException(String message){
        super(message);
    }
}
