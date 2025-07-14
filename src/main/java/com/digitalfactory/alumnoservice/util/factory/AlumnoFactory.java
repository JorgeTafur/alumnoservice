package com.digitalfactory.alumnoservice.util.factory;

import com.digitalfactory.alumnoservice.model.Alumno;
import com.digitalfactory.alumnoservice.model.AlumnoRequest;

public class AlumnoFactory {
    public static Alumno crearDesdeRequest(AlumnoRequest request) {
        return new Alumno(
                request.getId(),
                request.getNombre(),
                request.getApellido(),
                request.getEstado(),
                request.getEdad()
        );
    }
}

