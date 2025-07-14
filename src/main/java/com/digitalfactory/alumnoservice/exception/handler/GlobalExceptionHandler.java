package com.digitalfactory.alumnoservice.exception.handler;

import com.digitalfactory.alumnoservice.exception.AlumnoDuplicadoPorCamposException;
import com.digitalfactory.alumnoservice.exception.AlumnoYaExisteException;
import com.digitalfactory.alumnoservice.model.ErrorResponse;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlumnoYaExisteException.class)
    public ResponseEntity<ErrorResponse> handleAlumnoExiste(AlumnoYaExisteException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse().mensaje(e.getMessage()));
    }

    @ExceptionHandler(AlumnoDuplicadoPorCamposException.class)
    public ResponseEntity<ErrorResponse> handleAlumnoDuplicadoPorCampos(AlumnoDuplicadoPorCamposException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse().mensaje(e.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(WebExchangeBindException ex) {
        String mensaje = ex.getFieldErrors().stream()
                .map(err -> String.format("El campo '%s' %s", err.getField(), err.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(new ErrorResponse().mensaje("Error de validación: " + mensaje));
    }

    @ExceptionHandler(DecodingException.class)
    public ResponseEntity<ErrorResponse> handleDecodingException(DecodingException e) {
        String mensaje = "Error de formato: asegúrate de que los campos tengan el tipo de dato correcto";
        return ResponseEntity.badRequest().body(new ErrorResponse().mensaje(mensaje));
    }
}
