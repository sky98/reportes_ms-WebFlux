package co.com.pragma.model.reporte.gateways;

import co.com.pragma.model.reporte.Reporte;
import reactor.core.publisher.Mono;

public interface ReporteRepository {
    Mono<Reporte> obtenerReporte();
    Mono<Reporte> guardar(Reporte reporte);
}
