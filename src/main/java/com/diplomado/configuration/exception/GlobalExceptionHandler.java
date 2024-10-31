package com.diplomado.configuration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FechaNoValidaException.class)
    public ResponseEntity<Object> handleFechaNoValidaException(FechaNoValidaException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Fecha No Válida");
        response.put("message", ex.getMessage());
        response.put("path", "/api/sesiones");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Otros manejadores de excepciones si es necesario...
}
