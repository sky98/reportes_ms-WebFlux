package co.com.pragma.api.handlers.usecase;

import co.com.pragma.api.ReporteMapper;
import co.com.pragma.usecase.obtenerreporte.ObtenerReporteUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ObtenerReporteHandler {

    private final ObtenerReporteUseCase useCase;
    private final ReporteMapper reporteMapper;

    @PreAuthorize("hasRole('1')")
    public Mono<ServerResponse> ejecutar(ServerRequest serverRequest){
        return useCase.ejecutar()
                .map(reporteMapper::toResponse)
                .flatMap(response -> {
                    log.info("Se proceso con exito la solicitud de reporte.");
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(response);
                });
    }

}
