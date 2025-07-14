package com.digitalfactory.alumnoservice.util.validation.impl;

import com.digitalfactory.alumnoservice.exception.AlumnoYaExisteException;
import com.digitalfactory.alumnoservice.model.Alumno;
import com.digitalfactory.alumnoservice.repository.AlumnoRepository;
import com.digitalfactory.alumnoservice.util.validation.ValidadorAlumno;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ValidarIdDuplicado implements ValidadorAlumno {

    private final AlumnoRepository alumnoRepository;

    @Override
    public Mono<Void> validar(Alumno alumno) {
        return alumnoRepository.existePorId(alumno.getId())
                .flatMap(existe -> existe
                        ? Mono.error(new AlumnoYaExisteException())
                        : Mono.empty());
    }
}
