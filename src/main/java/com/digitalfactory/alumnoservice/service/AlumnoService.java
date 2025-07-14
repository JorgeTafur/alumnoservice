package com.digitalfactory.alumnoservice.service;

import com.digitalfactory.alumnoservice.model.Alumno;
import com.digitalfactory.alumnoservice.model.AlumnoRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlumnoService {
    Mono<Alumno> crearAlumnoDesdeRequest(AlumnoRequest alumnoRequest);
    Mono<Alumno> crearAlumno(Alumno alumno);
    Flux<Alumno> obtenerAlumnosActivos();
}
