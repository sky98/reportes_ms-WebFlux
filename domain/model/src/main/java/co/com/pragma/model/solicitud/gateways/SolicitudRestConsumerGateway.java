package co.com.pragma.model.solicitud.gateways;

import co.com.pragma.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SolicitudRestConsumerGateway {
    Mono<List<Solicitud>> obtenerSolicitudesAprobadasPorFecha(String fechaInicio, String fechaFin);
}
