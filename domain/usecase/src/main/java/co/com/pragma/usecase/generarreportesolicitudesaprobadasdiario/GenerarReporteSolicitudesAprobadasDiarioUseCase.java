package co.com.pragma.usecase.generarreportesolicitudesaprobadasdiario;

import co.com.pragma.model.reportediario.ReporteDiario;
import co.com.pragma.model.solicitud.gateways.SolicitudRestConsumerGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class GenerarReporteSolicitudesAprobadasDiarioUseCase {

    private final SolicitudRestConsumerGateway solicitudRestConsumerGateway;

    public Mono<List<ReporteDiario>> ejecutar(String fechaInicio, String fechaFin){
        return solicitudRestConsumerGateway.obtenerSolicitudesAprobadasPorFecha(fechaInicio, fechaFin);
    }

}
