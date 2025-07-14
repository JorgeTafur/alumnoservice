package com.digitalfactory.alumnoservice.exception;

public class AlumnoYaExisteException extends RuntimeException {
    public AlumnoYaExisteException() {
        super("El ID ya existe, no se pudo grabar el alumno");
    }
}
