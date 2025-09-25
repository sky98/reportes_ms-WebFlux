package co.com.pragma.consumer;

import co.com.pragma.model.errores.ErrorExterno;
import co.com.pragma.model.reportediario.ReporteDiario;
import co.com.pragma.model.solicitud.gateways.SolicitudRestConsumerGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolicitudRestConsumer implements SolicitudRestConsumerGateway {

    private final WebClient client;
    private final String PATH_OBTENER_SOLICITUDES_APROBADAS_POR_FECHA = "/api/v1/solicitudes/aprobadas/fecha";

    @Override
    @CircuitBreaker(name = "obtenerSolicitudesAprobadasPorFecha" , fallbackMethod = "obtenerSolicitudesAprobadasPorFechaFallBack")
    public Mono<List<ReporteDiario>> obtenerSolicitudesAprobadasPorFecha(String fechaInicio, String fechaFin){
        log.info("Obteniendo solicitudes aprobadas en las fechas {}, {}", fechaInicio, fechaFin);
        return client
                .get()
                .uri(PATH_OBTENER_SOLICITUDES_APROBADAS_POR_FECHA)
                .retrieve()
                .bodyToMono(ReporteSolicitudesAprobadasResponse.class)
                .map(ReporteSolicitudesAprobadasResponse::getReporteDiarioList);
    }

    private Mono<ReporteDiario> obtenerSolicitudesAprobadasPorFechaFallBack(Throwable throwable){
        log.error("Se activa el fallback para la peticion de obtener solicitudes aprobadas por fecha. Causa: {}", throwable.getMessage());
        return Mono.defer(()-> Mono.error(new ErrorExterno("Se ha generado un error al obtener las soicitudes aprobadas por fecha.", Set.of(throwable.getMessage()))));
    }


}
