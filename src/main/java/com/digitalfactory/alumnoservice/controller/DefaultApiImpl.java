package com.digitalfactory.alumnoservice.controller;

import com.digitalfactory.alumnoservice.api.DefaultApi;
import com.digitalfactory.alumnoservice.model.AlumnoRequest;
import com.digitalfactory.alumnoservice.model.Alumno;
import com.digitalfactory.alumnoservice.model.OkResponse;
import com.digitalfactory.alumnoservice.service.AlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DefaultApiImpl implements DefaultApi {

    private final AlumnoService alumnoService;

    @Override
    public Mono<ResponseEntity<OkResponse>> crearAlumno(Mono<AlumnoRequest> alumnoRequestMono, ServerWebExchange exchange) {
        return alumnoRequestMono
                .flatMap(alumnoService::crearAlumnoDesdeRequest)
                .thenReturn(ResponseEntity.ok(new OkResponse().mensaje("ok")));
    }

    @Override
    public Mono<ResponseEntity<Flux<Alumno>>> listarAlumnosActivos(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(alumnoService.obtenerAlumnosActivos()));
    }
}
