package co.com.pragma.model.solicitud.gateways;

import co.com.pragma.model.reportediario.ReporteDiario;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SolicitudRestConsumerGateway {
    Mono<List<ReporteDiario>> obtenerSolicitudesAprobadasPorFecha(String fechaInicio, String fechaFin);
}
