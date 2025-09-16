package co.com.pragma.usecase.procesarsolicitudesaprobadas;

import co.com.pragma.model.mensaje.gateway.MensajeUtilsGateway;
import co.com.pragma.model.mensaje.model.SolicitudAprobadaMensaje;
import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.gateways.ReporteRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class ProcesarSolicitudesAprobadasUseCase {

    private final ReporteRepository reporteRepository;
    private final MensajeUtilsGateway mensajeUtilsGateway;

    public Mono<Reporte> ejecutar(String messageBody){
        return mensajeUtilsGateway.deserializarMensaje(messageBody, SolicitudAprobadaMensaje.class)
                .flatMap(solicitud -> reporteRepository.guardar(
                        Reporte.builder()
                                .id(UUID.randomUUID().toString())
                                .cantidad(1)
                                .monto(solicitud.getMonto())
                                .build()
                ));
    }


}
