package com.digitalfactory.alumnoservice.util.validation.impl;

import com.digitalfactory.alumnoservice.exception.AlumnoYaExisteException;
import com.digitalfactory.alumnoservice.model.Alumno;
import com.digitalfactory.alumnoservice.model.EstadoEnum;
import com.digitalfactory.alumnoservice.repository.AlumnoRepository;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class ValidarIdDuplicadoTest {

    @Test
    void validar_DeberiaEmitirErrorSiIdYaExiste() {
        AlumnoRepository repo = mock(AlumnoRepository.class);
        ValidarIdDuplicado validador = new ValidarIdDuplicado(repo);
        Alumno alumno = new Alumno(1, "Juan", "Pérez", EstadoEnum.ACTIVO, 25);

        when(repo.existePorId(1)).thenReturn(Mono.just(true));

        StepVerifier.create(validador.validar(alumno))
                .expectError(AlumnoYaExisteException.class)
                .verify();
    }

    @Test
    void validar_DeberiaPasarSiIdNoExiste() {
        AlumnoRepository repo = mock(AlumnoRepository.class);
        ValidarIdDuplicado validador = new ValidarIdDuplicado(repo);
        Alumno alumno = new Alumno(1, "Juan", "Pérez", EstadoEnum.ACTIVO, 25);

        when(repo.existePorId(1)).thenReturn(Mono.just(false));

        StepVerifier.create(validador.validar(alumno))
                .verifyComplete();
    }
}
