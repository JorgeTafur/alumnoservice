package com.digitalfactory.alumnoservice.repository;

import com.digitalfactory.alumnoservice.model.Alumno;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AlumnoRepository {
    private final Map<Integer, Alumno> baseMemoria = new ConcurrentHashMap<>();

    public Mono<Boolean> existePorId(Integer id) {
        return Mono.just(baseMemoria.containsKey(id));
    }

    public Mono<Alumno> guardar(Alumno alumno) {
        baseMemoria.put(alumno.getId(), alumno);
        return Mono.just(alumno);
    }

    public Flux<Alumno> obtenerTodos() {
        return Flux.fromIterable(baseMemoria.values());
    }
}
