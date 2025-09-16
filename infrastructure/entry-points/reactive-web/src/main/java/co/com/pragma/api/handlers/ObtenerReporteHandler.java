package co.com.pragma.api.handlers;

import co.com.pragma.usecase.obtenerreporte.ObtenerReporteUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ObtenerReporteHandler {

    private final ObtenerReporteUseCase useCase;

    public Mono<ServerResponse> ejecutar(ServerRequest serverRequest){
        return useCase.ejecutar()
                .flatMap(response -> {
                    log.info("Se proceso con exito la solicitud de reporte.");
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(response);
                });
    }

}
