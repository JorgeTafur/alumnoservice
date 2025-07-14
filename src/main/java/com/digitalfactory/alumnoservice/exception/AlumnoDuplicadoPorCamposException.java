package com.digitalfactory.alumnoservice.exception;

public class AlumnoDuplicadoPorCamposException extends RuntimeException {
    public AlumnoDuplicadoPorCamposException() {
        super("Ya existe un alumno con el mismo nombre, apellido y edad");
    }
}
