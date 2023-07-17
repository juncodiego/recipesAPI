package com.abnamro.recipes.handler;

import com.abnamro.recipes.exception.RecipeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler({RecipeNotFoundException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<Void> handleNotFound(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.notFound().build();
    }
}
