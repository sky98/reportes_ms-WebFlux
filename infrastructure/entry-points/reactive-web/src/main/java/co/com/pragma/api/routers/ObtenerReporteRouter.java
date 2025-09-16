package co.com.pragma.api.routers;

import co.com.pragma.api.Handler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
@RequiredArgsConstructor
public class ObtenerReporteRouter {

    private static final String PATH = "/api/v1/reportes";
    private final Handler handler;

    @Bean
    public RouterFunction<ServerResponse> obtenerReporte(){
        return RouterFunctions.route(GET(PATH).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::obtenerReporte);
    }

}
