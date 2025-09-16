package co.com.pragma.usecase.procesarsolicitudesaprobadas;

import co.com.pragma.model.mensaje.gateway.MensajeUtilsGateway;
import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.gateways.ReporteRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProcesarSolicitudesAprobadasUseCase {

    //private final ReporteRepository reporteRepository;
    //private final MensajeUtilsGateway mensajeUtilsGateway;

    public Mono<Reporte> ejecutar(String messageBody){
        return null;
    }


}
