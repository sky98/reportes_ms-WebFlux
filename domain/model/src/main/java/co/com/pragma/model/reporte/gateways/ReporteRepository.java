package co.com.pragma.model.reporte.gateways;

import co.com.pragma.model.reporte.Reporte;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ReporteRepository {
    Mono<List<Reporte>> obtenerReportes();
    Mono<Reporte> guardar(Reporte reporte);
}
