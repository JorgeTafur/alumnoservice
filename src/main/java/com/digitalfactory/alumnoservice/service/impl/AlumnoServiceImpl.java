package com.digitalfactory.alumnoservice.service.impl;

import com.digitalfactory.alumnoservice.model.Alumno;
import com.digitalfactory.alumnoservice.model.AlumnoRequest;
import com.digitalfactory.alumnoservice.model.EstadoEnum;
import com.digitalfactory.alumnoservice.repository.AlumnoRepository;
import com.digitalfactory.alumnoservice.service.AlumnoService;
import com.digitalfactory.alumnoservice.util.factory.AlumnoFactory;
import com.digitalfactory.alumnoservice.util.validation.ValidadorAlumno;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final List<ValidadorAlumno> validadores;
    private static final Logger log = LoggerFactory.getLogger(AlumnoServiceImpl.class);

    @Override
    public Mono<Void> crearAlumnoDesdeRequest(AlumnoRequest request) {
        log.info("Inicio crearAlumnoDesdeRequest: {}", request);
        Alumno alumno = AlumnoFactory.crearDesdeRequest(request);
        return crearAlumno(alumno)
                .doOnSuccess(v -> log.info("Alumno creado con éxito: {}", alumno))
                .doOnError(e -> log.error("Error al crear alumno: ", e));
    }

    @Override
    public Mono<Void> crearAlumno(Alumno alumno) {
        log.info("Inicio crearAlumno: {}", alumno);
        return ejecutarValidaciones(alumno)
                .doOnSuccess(v -> log.info("Validaciones pasadas para alumno: {}", alumno))
                .then(Mono.defer(() -> {
                    log.info("Guardando alumno después de validaciones...");
                    return alumnoRepository.guardar(alumno);
                }))
                .doOnSuccess(v -> log.info("Alumno guardado en repositorio: {}", alumno))
                .doOnError(e -> log.error("Error al guardar alumno: ", e));
    }

    @Override
    public Flux<Alumno> obtenerAlumnosActivos() {
        return alumnoRepository.obtenerTodos()
                .filter(alumno -> alumno.getEstado() == EstadoEnum.ACTIVO);
    }

    private Mono<Void> ejecutarValidaciones(Alumno alumno) {
        log.info("Ejecutando validaciones para alumno: {}", alumno);

        return Flux.fromIterable(validadores)
                .concatMap(validador -> ejecutarValidador(validador, alumno))
                .then()
                .doOnSuccess(ignored -> log.info("Todas las validaciones pasaron para alumno: {}", alumno));
    }

    private Mono<Void> ejecutarValidador(ValidadorAlumno validador, Alumno alumno) {
        String nombreValidador = validador.getClass().getSimpleName();
        log.debug("Ejecutando validador: {}", nombreValidador);

        return validador.validar(alumno)
                .doOnError(e -> log.error("Validación fallida en {}: {}", nombreValidador, e.getMessage()));
    }
}