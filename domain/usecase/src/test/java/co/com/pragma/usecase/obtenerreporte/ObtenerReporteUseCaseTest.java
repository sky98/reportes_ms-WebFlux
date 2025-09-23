package co.com.pragma.usecase.obtenerreporte;

import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.gateways.ReporteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ObtenerReporteUseCaseTest {

    @Mock
    private ReporteRepository reporteRepository;

    @InjectMocks
    private ObtenerReporteUseCase obtenerReporteUseCase;

    private Reporte reporte;

    @BeforeEach
    void setUp() {
        reporte = Reporte.builder()
                .id("12345")
                .cantidad(1)
                .monto(BigDecimal.TEN)
                .build();
    }

    @Test
    @DisplayName("Debe devolver un reporte cuando la operacion es exitosa")
    void shouldReturnReporteWhenOperationIsSuccessful() {
        when(reporteRepository.obtenerReportes()).thenReturn(Mono.just(List.of(reporte)));

        Mono<Reporte> result = obtenerReporteUseCase.ejecutar();

        StepVerifier.create(result)
                .expectNext(reporte)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe devolver un error cuando la operacion falla")
    void shouldReturnErrorWhenOperationFails() {
        when(reporteRepository.obtenerReportes()).thenReturn(Mono.error(new RuntimeException("Error en la base de datos")));

        Mono<Reporte> result = obtenerReporteUseCase.ejecutar();

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Error en la base de datos"))
                .verify();
    }

}
