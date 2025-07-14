package com.digitalfactory.alumnoservice.controller;

import com.digitalfactory.alumnoservice.model.Alumno;
import com.digitalfactory.alumnoservice.model.AlumnoRequest;
import com.digitalfactory.alumnoservice.model.OkResponse;
import com.digitalfactory.alumnoservice.service.AlumnoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultApiImplTest {

    @Mock
    private AlumnoService alumnoService;

    @InjectMocks
    private DefaultApiImpl defaultApi;

    @Test
    void crearAlumnoDesdeRequest_deberiaInvocarServicioYResponderNoContent() {
        AlumnoRequest request = new AlumnoRequest();
        // Puedes setear campos si tu lógica lo requiere
        // Ejemplo:
        // request.setId(1);
        // request.setNombre("Juan");
        // ...

        when(alumnoService.crearAlumnoDesdeRequest(any(AlumnoRequest.class))).thenReturn(Mono.empty());

        Mono<ResponseEntity<OkResponse>> responseMono = defaultApi.crearAlumno(Mono.just(request), null);

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertTrue(response.getStatusCode().is2xxSuccessful());
                    assertEquals(200, response.getStatusCodeValue());
                })
                .verifyComplete();

        verify(alumnoService, times(1)).crearAlumnoDesdeRequest(any(AlumnoRequest.class));
    }

    @Test
    void listarAlumnosActivos_deberiaRetornarAlumnos() {
        Alumno alumno = new Alumno();
        // Setea campos si es necesario para las pruebas

        when(alumnoService.obtenerAlumnosActivos()).thenReturn(Flux.just(alumno));

        Mono<ResponseEntity<Flux<Alumno>>> responseMono = defaultApi.listarAlumnosActivos(null);

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertTrue(response.getStatusCode().is2xxSuccessful());
                    assertNotNull(response.getBody());
                    // Comprobamos que el flujo contenga al menos un alumno (nuestro mock)
                    assertDoesNotThrow(() -> {
                        assertTrue(response.getBody().collectList().block().contains(alumno));
                    });
                })
                .verifyComplete();

        verify(alumnoService, times(1)).obtenerAlumnosActivos();
    }
}
