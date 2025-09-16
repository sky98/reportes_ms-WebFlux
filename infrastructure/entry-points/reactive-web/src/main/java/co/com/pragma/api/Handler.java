package co.com.pragma.api;

import co.com.pragma.api.handlers.ObtenerReporteHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ObtenerReporteHandler obtenerReporteHandler;

    public Mono<ServerResponse> obtenerReporte(ServerRequest serverRequest) {
        return obtenerReporteHandler.ejecutar(serverRequest);
    }

//    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
//        // useCase2.logic();
//        return ServerResponse.ok().bodyValue("");
//    }
//
//    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
//        // useCase.logic();
//        return ServerResponse.ok().bodyValue("");
//    }
}
