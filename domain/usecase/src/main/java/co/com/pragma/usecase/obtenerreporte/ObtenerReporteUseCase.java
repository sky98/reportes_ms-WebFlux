package co.com.pragma.usecase.obtenerreporte;

import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.gateways.ReporteRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ObtenerReporteUseCase {

    private final ReporteRepository reporteRepository;

    public Mono<Reporte> ejecutar(){
        return reporteRepository.obtenerReportes()
                .flatMap(reportes -> Mono.just(reportes.getFirst()));
    }

}
