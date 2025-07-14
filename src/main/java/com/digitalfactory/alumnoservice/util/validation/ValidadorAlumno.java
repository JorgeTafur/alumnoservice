package com.digitalfactory.alumnoservice.util.validation;

import com.digitalfactory.alumnoservice.model.Alumno;
import reactor.core.publisher.Mono;

public interface ValidadorAlumno {
    Mono<Void> validar(Alumno alumno);
}
