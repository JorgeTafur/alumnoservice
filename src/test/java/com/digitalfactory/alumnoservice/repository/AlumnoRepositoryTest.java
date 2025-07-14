package com.digitalfactory.alumnoservice.repository;

import com.digitalfactory.alumnoservice.model.Alumno;
import com.digitalfactory.alumnoservice.model.EstadoEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class AlumnoRepositoryTest {

    private AlumnoRepository repository;

    private Alumno alumno1;
    private Alumno alumno2;

    @BeforeEach
    void setUp() {
        repository = new AlumnoRepository();

        alumno1 = new Alumno(1, "Juan", "Pérez", EstadoEnum.ACTIVO, 20);
        alumno2 = new Alumno(2, "Ana", "López", EstadoEnum.INACTIVO, 22);
    }

    @Test
    void guardar_deberiaGuardarAlumno() {
        StepVerifier.create(repository.guardar(alumno1))
                .verifyComplete();

        StepVerifier.create(repository.existePorId(alumno1.getId()))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void existePorId_deberiaRetornarFalseSiNoExiste() {
        StepVerifier.create(repository.existePorId(99))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void obtenerTodos_deberiaRetornarTodosLosAlumnos() {
        repository.guardar(alumno1).block();
        repository.guardar(alumno2).block();

        StepVerifier.create(repository.obtenerTodos())
                .expectNext(alumno1)
                .expectNext(alumno2)
                .verifyComplete();
    }
}
