package com.digitalfactory.alumnoservice.util.validation.impl;

import com.digitalfactory.alumnoservice.exception.AlumnoDuplicadoPorCamposException;
import com.digitalfactory.alumnoservice.model.Alumno;
import com.digitalfactory.alumnoservice.model.EstadoEnum;
import com.digitalfactory.alumnoservice.repository.AlumnoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class ValidarAlumnoDuplicadoPorCamposTest {

    private AlumnoRepository alumnoRepository;
    private ValidarAlumnoDuplicadoPorCampos validador;

    @BeforeEach
    void setUp() {
        alumnoRepository = mock(AlumnoRepository.class);
        validador = new ValidarAlumnoDuplicadoPorCampos(alumnoRepository);
    }

    @Test
    void validar_DeberiaEmitirErrorSiEncuentraDuplicado() {
        AlumnoRepository repo = mock(AlumnoRepository.class);
        ValidarAlumnoDuplicadoPorCampos validador = new ValidarAlumnoDuplicadoPorCampos(repo);

        Alumno nuevo = new Alumno(1, "Juan", "Pérez", EstadoEnum.ACTIVO, 25);
        Alumno duplicado = new Alumno(2, "Juan", "Pérez", EstadoEnum.INACTIVO, 25); // Distinto ID

        when(repo.obtenerTodos()).thenReturn(Flux.just(duplicado));

        StepVerifier.create(validador.validar(nuevo))
                .expectError(AlumnoDuplicadoPorCamposException.class)
                .verify();
    }

    @Test
    void validar_DeberiaPasarSiNoHayDuplicados() {
        AlumnoRepository repo = mock(AlumnoRepository.class);
        ValidarAlumnoDuplicadoPorCampos validador = new ValidarAlumnoDuplicadoPorCampos(repo);

        Alumno nuevo = new Alumno(1, "Juan", "Pérez", EstadoEnum.ACTIVO, 25);
        Alumno otro = new Alumno(1, "Juan", "Pérez", EstadoEnum.ACTIVO, 25); // Mismo ID (se ignora)

        when(repo.obtenerTodos()).thenReturn(Flux.just(otro));

        StepVerifier.create(validador.validar(nuevo))
                .verifyComplete();
    }

    @Test
    void validar_alumnoNoEsDuplicado_PorNombre_noLanzaExcepcion() {
        // El alumno nuevo
        Alumno alumnoNuevo = new Alumno(1, "Juan", "Perez", EstadoEnum.ACTIVO, 20);
        // El alumno en la base tiene un apellido diferente → no es duplicado
        Alumno alumnoExistente = new Alumno(2, "Raul", "Perez", EstadoEnum.ACTIVO, 20);

        when(alumnoRepository.obtenerTodos()).thenReturn(Flux.just(alumnoExistente));

        StepVerifier.create(validador.validar(alumnoNuevo))
                .verifyComplete(); // No debe lanzar excepción
    }

    @Test
    void validar_alumnoNoEsDuplicado_PorApellido_noLanzaExcepcion() {
        // El alumno nuevo
        Alumno alumnoNuevo = new Alumno(1, "Juan", "Perez", EstadoEnum.ACTIVO, 20);
        // El alumno en la base tiene un apellido diferente → no es duplicado
        Alumno alumnoExistente = new Alumno(2, "Juan", "Gomez", EstadoEnum.ACTIVO, 20);

        when(alumnoRepository.obtenerTodos()).thenReturn(Flux.just(alumnoExistente));

        StepVerifier.create(validador.validar(alumnoNuevo))
                .verifyComplete(); // No debe lanzar excepción
    }

    @Test
    void validar_alumnoNoEsDuplicado_PorEdad_noLanzaExcepcion() {
        // El alumno nuevo
        Alumno alumnoNuevo = new Alumno(1, "Juan", "Perez", EstadoEnum.ACTIVO, 20);
        // El alumno en la base tiene un apellido diferente → no es duplicado
        Alumno alumnoExistente = new Alumno(2, "Juan", "Perez", EstadoEnum.ACTIVO, 25);

        when(alumnoRepository.obtenerTodos()).thenReturn(Flux.just(alumnoExistente));

        StepVerifier.create(validador.validar(alumnoNuevo))
                .verifyComplete(); // No debe lanzar excepción
    }
}
