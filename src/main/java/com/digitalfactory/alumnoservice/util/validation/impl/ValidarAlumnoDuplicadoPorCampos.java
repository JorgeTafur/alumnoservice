package com.digitalfactory.alumnoservice.util.validation.impl;

import com.digitalfactory.alumnoservice.exception.AlumnoDuplicadoPorCamposException;
import com.digitalfactory.alumnoservice.model.Alumno;
import com.digitalfactory.alumnoservice.repository.AlumnoRepository;
import com.digitalfactory.alumnoservice.util.validation.ValidadorAlumno;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ValidarAlumnoDuplicadoPorCampos implements ValidadorAlumno {

    private final AlumnoRepository alumnoRepository;

    @Override
    public Mono<Void> validar(Alumno alumno) {
        return alumnoRepository.obtenerTodos()
                .filter(otro -> esDuplicado(alumno, otro))
                .next()
                .flatMap(duplicado -> Mono.error(new AlumnoDuplicadoPorCamposException()))
                .switchIfEmpty(Mono.just("OK")) // cualquier valor dummy
                .then(); // convierte a Mono<Void>
    }


    private boolean esDuplicado(Alumno nuevo, Alumno existente) {
        return !nuevo.getId().equals(existente.getId()) &&
                nuevo.getNombre().equalsIgnoreCase(existente.getNombre()) &&
                nuevo.getApellido().equalsIgnoreCase(existente.getApellido()) &&
                nuevo.getEdad().equals(existente.getEdad());
    }
}
