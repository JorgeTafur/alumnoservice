package com.digitalfactory.alumnoservice.service;

import com.digitalfactory.alumnoservice.model.Alumno;
import com.digitalfactory.alumnoservice.model.AlumnoRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlumnoService {
    Mono<Void> crearAlumnoDesdeRequest(AlumnoRequest alumnoRequest);
    Mono<Void> crearAlumno(Alumno alumno);
    Mono<Void> actualizarAlumnoDesdeRequest(AlumnoRequest alumno);
    Flux<Alumno> obtenerAlumnosActivos();
}
