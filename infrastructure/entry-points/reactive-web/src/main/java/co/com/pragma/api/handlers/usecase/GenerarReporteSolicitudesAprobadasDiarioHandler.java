package co.com.pragma.api.handlers.usecase;

import co.com.pragma.usecase.generarreportesolicitudesaprobadasdiario.GenerarReporteSolicitudesAprobadasDiarioUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenerarReporteSolicitudesAprobadasDiarioHandler {

    private final GenerarReporteSolicitudesAprobadasDiarioUseCase useCase;

    public Mono<ServerResponse> ejecutar(ServerRequest serverRequest){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate fechaInicio = serverRequest.queryParam("fechaInicio")
                .map(f -> LocalDate.parse(f, formatter))
                .orElse(LocalDate.now());
        LocalDate fechaFin = serverRequest.queryParam("fechaFin")
                .map(f -> LocalDate.parse(f, formatter))
                .orElse(LocalDate.now());
        LocalDateTime inicioDelDia = fechaInicio.atStartOfDay();
        LocalDateTime finDelDia = fechaFin.atTime(23, 59, 59);
        log.info("Iniciando flujo para generar reporte en las fechas : {} y {}", inicioDelDia, finDelDia);
        return useCase.ejecutar(inicioDelDia.toString(), finDelDia.toString())
                .flatMap(response -> {
                    log.info("Reporte generado : {} , {}", inicioDelDia, finDelDia);
                    return ServerResponse.ok().build();
                });
    }

}
