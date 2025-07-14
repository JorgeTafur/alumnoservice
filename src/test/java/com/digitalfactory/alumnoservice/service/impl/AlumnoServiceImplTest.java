package com.digitalfactory.alumnoservice.service.impl;

import com.digitalfactory.alumnoservice.exception.AlumnoDuplicadoPorCamposException;
import com.digitalfactory.alumnoservice.exception.AlumnoYaExisteException;
import com.digitalfactory.alumnoservice.model.Alumno;
import com.digitalfactory.alumnoservice.model.AlumnoRequest;
import com.digitalfactory.alumnoservice.model.EstadoEnum;
import com.digitalfactory.alumnoservice.repository.AlumnoRepository;
import com.digitalfactory.alumnoservice.service.impl.AlumnoServiceImpl;
import com.digitalfactory.alumnoservice.util.factory.AlumnoFactory;
import com.digitalfactory.alumnoservice.util.validation.ValidadorAlumno;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class AlumnoServiceImplTest {

    private AlumnoRepository alumnoRepository;
    private ValidadorAlumno validadorMock;
    private AlumnoServiceImpl alumnoService;

    @BeforeEach
    void setUp() {
        alumnoRepository = mock(AlumnoRepository.class);
        validadorMock = mock(ValidadorAlumno.class);
        alumnoService = new AlumnoServiceImpl(alumnoRepository, List.of(validadorMock));
    }

    @Test
    void crearAlumno_DeberiaGuardarAlumnoSiPasaValidaciones() {
        Alumno alumno = new Alumno(1, "Juan", "Pérez", EstadoEnum.ACTIVO, 25);

        when(validadorMock.validar(alumno)).thenReturn(Mono.empty());
        when(alumnoRepository.guardar(alumno)).thenReturn(Mono.empty());

        StepVerifier.create(alumnoService.crearAlumno(alumno))
                .verifyComplete();

        verify(validadorMock).validar(alumno);
        verify(alumnoRepository).guardar(alumno);
    }

    @Test
    void crearAlumno_DeberiaFallarSiValidadorLanzaError() {
        Alumno alumno = new Alumno(2, "Ana", "Ramírez", EstadoEnum.ACTIVO, 20);

        when(validadorMock.validar(alumno)).thenReturn(Mono.error(new AlumnoYaExisteException()));

        StepVerifier.create(alumnoService.crearAlumno(alumno))
                .expectError(AlumnoYaExisteException.class)
                .verify();

        verify(validadorMock).validar(alumno);
        verify(alumnoRepository, never()).guardar(any());
    }

    @Test
    void obtenerAlumnosActivos_DeberiaRetornarSoloActivos() {
        Alumno activo = new Alumno(3, "Luis", "Suárez", EstadoEnum.ACTIVO, 30);
        Alumno inactivo = new Alumno(4, "Pedro", "Gómez", EstadoEnum.INACTIVO, 40);

        when(alumnoRepository.obtenerTodos()).thenReturn(Flux.just(activo, inactivo));

        StepVerifier.create(alumnoService.obtenerAlumnosActivos())
                .expectNext(activo)
                .verifyComplete();
    }

    @Test
    void crearAlumnoDesdeRequest_DeberiaCrearAlumnoCorrectamente() {
        AlumnoRequest request = new AlumnoRequest();
        request.setId(5);
        request.setNombre("María");
        request.setApellido("López");
        request.setEstado(EstadoEnum.ACTIVO);
        request.setEdad(22);

        Alumno alumnoEsperado = AlumnoFactory.crearDesdeRequest(request);

        when(validadorMock.validar(alumnoEsperado)).thenReturn(Mono.empty());
        when(alumnoRepository.guardar(alumnoEsperado)).thenReturn(Mono.empty());

        StepVerifier.create(alumnoService.crearAlumnoDesdeRequest(request))
                .verifyComplete();

        verify(validadorMock).validar(alumnoEsperado);
        verify(alumnoRepository).guardar(alumnoEsperado);
    }

    @Test
    void crearAlumnoDesdeRequest_errorEnCrearAlumno_logsErrorYPropaga() {
        // Arrange
        AlumnoRequest request = new AlumnoRequest();
        request.setId(1);
        request.setNombre("Juan");
        request.setApellido("Perez");
        request.setEdad(20);
        request.setEstado(EstadoEnum.ACTIVO);

        // Validador que siempre lanza error
        ValidadorAlumno validadorMock = mock(ValidadorAlumno.class);
        when(validadorMock.validar(any())).thenReturn(Mono.error(new RuntimeException("Error forzado")));

        AlumnoServiceImpl alumnoService = new AlumnoServiceImpl(alumnoRepository, List.of(validadorMock));

        // Act & Assert
        StepVerifier.create(alumnoService.crearAlumnoDesdeRequest(request))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Error forzado"))
                .verify();
    }

}
