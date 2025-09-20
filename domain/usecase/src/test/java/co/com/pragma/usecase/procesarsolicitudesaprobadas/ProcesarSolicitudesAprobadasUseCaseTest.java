package co.com.pragma.usecase.procesarsolicitudesaprobadas;

import co.com.pragma.model.mensaje.gateway.MensajeUtilsGateway;
import co.com.pragma.model.mensaje.model.SolicitudAprobadaMensaje;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProcesarSolicitudesAprobadasUseCaseTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private MensajeUtilsGateway mensajeUtilsGateway;

    @InjectMocks
    private ProcesarSolicitudesAprobadasUseCase useCase;

    private SolicitudAprobadaMensaje solicitudAprobadaMensaje;
    private Reporte reporteExistente;
    private Reporte reporteActualizado;
    private final String messageBody = "{\"id\":\"123\", \"monto\":100.50}";

    @BeforeEach
    void setUp() {
        solicitudAprobadaMensaje = SolicitudAprobadaMensaje.builder()
                .solicitudId(1L)
                .monto(new BigDecimal("100.50"))
                .build();

        reporteExistente = Reporte.builder()
                .id("12345")
                .cantidad(10)
                .monto(new BigDecimal("500.00"))
                .build();

        reporteActualizado = Reporte.builder()
                .id("12345")
                .cantidad(11)
                .monto(new BigDecimal("600.50"))
                .build();
    }

    @Test
    @DisplayName("Debe procesar una solicitud aprobada y actualizar el reporte correctamente")
    void shouldProcessApprovedRequestAndUpdateReportSuccessfully() {
        when(mensajeUtilsGateway.deserializarMensaje(eq(messageBody), eq(SolicitudAprobadaMensaje.class)))
                .thenReturn(Mono.just(solicitudAprobadaMensaje));
        when(reporteRepository.obtenerReportes())
                .thenReturn(Mono.just(reporteExistente));
        when(reporteRepository.guardar(any(Reporte.class)))
                .thenReturn(Mono.just(reporteActualizado));

        Mono<Reporte> result = useCase.ejecutar(messageBody);

        StepVerifier.create(result)
                .expectNextMatches(reporte -> reporte.getCantidad() == 11 &&
                        reporte.getMonto().compareTo(new BigDecimal("600.50")) == 0)
                .verifyComplete();
        verify(reporteRepository).guardar(any(Reporte.class));
    }

    @Test
    @DisplayName("Debe devolver un error si la deserialización del mensaje falla")
    void shouldReturnErrorWhenMessageDeserializationFails() {
        when(mensajeUtilsGateway.deserializarMensaje(eq(messageBody), eq(SolicitudAprobadaMensaje.class)))
                .thenReturn(Mono.error(new RuntimeException("Error de deserialización")));

        Mono<Reporte> result = useCase.ejecutar(messageBody);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Error de deserialización"))
                .verify();
    }

    @Test
    @DisplayName("Debe devolver un error si la obtención del reporte falla")
    void shouldReturnErrorWhenFetchingReportFails() {
        when(mensajeUtilsGateway.deserializarMensaje(any(), any()))
                .thenReturn(Mono.just(solicitudAprobadaMensaje));
        when(reporteRepository.obtenerReportes())
                .thenReturn(Mono.error(new RuntimeException("Error de base de datos")));

        Mono<Reporte> result = useCase.ejecutar(messageBody);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Error de base de datos"))
                .verify();
    }

    @Test
    @DisplayName("Debe devolver un error si el guardado del reporte falla")
    void shouldReturnErrorWhenSavingReportFails() {
        when(mensajeUtilsGateway.deserializarMensaje(any(), any()))
                .thenReturn(Mono.just(solicitudAprobadaMensaje));
        when(reporteRepository.obtenerReportes())
                .thenReturn(Mono.just(reporteExistente));
        when(reporteRepository.guardar(any(Reporte.class)))
                .thenReturn(Mono.error(new RuntimeException("Error al guardar en DynamoDB")));

        Mono<Reporte> result = useCase.ejecutar(messageBody);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Error al guardar en DynamoDB"))
                .verify();
    }

}
