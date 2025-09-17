package co.com.pragma.api.routers;

import co.com.pragma.api.Handler;
import co.com.pragma.api.dto.response.ReporteResponse;
import co.com.pragma.api.handlers.filterexception.AccessDeniedHandler;
import co.com.pragma.api.handlers.filterexception.AuthenticationEntryPoint;
import co.com.pragma.api.handlers.usecase.ObtenerReporteHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @RouterOperation(
            path = PATH,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            method = RequestMethod.GET,
            beanClass = ObtenerReporteHandler.class,
            beanMethod = "ejecutar",
            operation = @Operation(
                    operationId = "obtenerReporte",
                    tags = {"Reportes"},
                    summary = "Obtiene el reporte de datos de la aplicación",
                    description = "Requiere el rol '1' (administrador) para acceder a este recurso.",
                    security = @SecurityRequirement(name = "Bearer Authentication"),
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Reporte obtenido exitosamente",
                                    content = @Content(schema = @Schema(implementation = ReporteResponse.class))
                            ),
                            @ApiResponse(
                                    responseCode = "401",
                                    description = "No autorizado, el usuario no ha enviado un token de autenticación o el token es inválido.",
                                    content = @Content(schema = @Schema(implementation = AuthenticationEntryPoint.class))
                            ),
                            @ApiResponse(
                                    responseCode = "403",
                                    description = "Acceso denegado, el usuario no tiene los permisos necesarios (rol 'administrador').",
                                    content = @Content(schema = @Schema(implementation = AccessDeniedHandler.class))
                            )
                    }
            )
    )
    public RouterFunction<ServerResponse> obtenerReporte(){
        return RouterFunctions.route(GET(PATH).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::obtenerReporte);
    }

}
