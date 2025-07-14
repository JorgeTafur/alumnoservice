package com.digitalfactory.alumnoservice.exception.handler;

import com.digitalfactory.alumnoservice.exception.AlumnoDuplicadoPorCamposException;
import com.digitalfactory.alumnoservice.exception.AlumnoYaExisteException;
import com.digitalfactory.alumnoservice.model.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class GlobalExceptionHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleAlumnoExiste_deberiaRetornar400() {
        AlumnoYaExisteException ex = new AlumnoYaExisteException();

        ResponseEntity<ErrorResponse> response = handler.handleAlumnoExiste(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMensaje()).isEqualTo(ex.getMessage());
    }

    @Test
    void handleAlumnoDuplicadoPorCampos_deberiaRetornar409() {
        AlumnoDuplicadoPorCamposException ex = new AlumnoDuplicadoPorCamposException();

        ResponseEntity<ErrorResponse> response = handler.handleAlumnoDuplicadoPorCampos(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMensaje()).isEqualTo(ex.getMessage());
    }

    @Test
    void handleValidationErrors_deberiaRetornar400ConMensajeFormateado() {
        // Mock de WebExchangeBindException
        FieldError fieldError = new FieldError("alumnoRequest", "id", "no debe ser nulo");

        WebExchangeBindException ex = mock(WebExchangeBindException.class);
        when(ex.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponse> response = handler.handleValidationErrors(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMensaje())
                .isEqualTo("Error de validación: El campo 'id' no debe ser nulo");
    }

    @Test
    void crearAlumno_edadInvalida_retornaErrorDeFormatoAmigable() {
        // JSON inválido: edad como texto
        String jsonInvalido = "{\n" +
                "  \"id\": 1,\n" +
                "  \"nombre\": \"Juan\",\n" +
                "  \"apellido\": \"Perez\",\n" +
                "  \"estado\": \"activo\",\n" +
                "  \"edad\": \"texto\"\n" +
                "}";

        webTestClient.post()
                .uri("/alumnos")
                .header("Content-Type", "application/json")
                .bodyValue(jsonInvalido)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.mensaje")
                .value(msg -> {
                    assert msg instanceof String;
                    String mensaje = (String) msg;
                    assert mensaje.contains("Error de formato");
                    assert mensaje.toLowerCase().contains("edad");
                });
    }
}
