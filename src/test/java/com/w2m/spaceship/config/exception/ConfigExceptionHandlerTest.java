package com.w2m.spaceship.config.exception;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigExceptionHandlerTest {

    private final ConfigExceptionHandler exceptionHandler = new ConfigExceptionHandler();

    @Test
    void testEntityNotFoundException() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");

        ResponseEntity<String> response = exceptionHandler.handleEntityNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Entity not found", response.getBody());
    }

    @Test
    void testDuplicateKeyException() {
        DuplicateKeyException ex = new DuplicateKeyException("Duplicate key");

        ResponseEntity<String> response = exceptionHandler.handleDuplicateKeyException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Duplicate key", response.getBody());
    }

    @Test
    void testGeneralException() {
        Exception ex = new Exception("General exception");

        ResponseEntity<String> response = exceptionHandler.handleGeneralException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("General exception", response.getBody());
    }
}