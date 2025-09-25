package co.com.pragma.api.routers;

import co.com.pragma.api.handlers.filterexception.AccessDeniedHandler;
import co.com.pragma.api.handlers.filterexception.AuthenticationEntryPoint;
import co.com.pragma.api.handlers.usecase.GenerarReporteSolicitudesAprobadasDiarioHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
public class GenerarReporteSolicitudesAprobadasPorFechaRouter {

    private static final String PATH = "/api/v1/reportes/aprobadas/fecha";
    private final GenerarReporteSolicitudesAprobadasDiarioHandler handler;

    @Bean
    @RouterOperation(
            path = PATH,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            method = RequestMethod.GET,
            beanClass = GenerarReporteSolicitudesAprobadasDiarioHandler.class,
            beanMethod = "ejecutar",
            operation = @Operation(
                    operationId = "generarReporteDiario",
                    tags = {"Reportes"},
                    summary = "Genera un reporte de solicitudes aprobadas por rango de fecha",
                    description = "Este servicio genera un reporte detallado de todas las solicitudes aprobadas en un rango de fechas. Requiere el rol '1' (administrador) para acceder a este recurso.",
                    security = @SecurityRequirement(name = "Bearer Authentication"),
                    parameters = {
                            @Parameter(in = ParameterIn.QUERY, name = "fechaInicio", description = "Fecha de inicio para el reporte (formato yyyy-MM-dd)", required = true, schema = @Schema(type = "string", example = "2025-09-01")),
                            @Parameter(in = ParameterIn.QUERY, name = "fechaFin", description = "Fecha de fin para el reporte (formato yyyy-MM-dd)", required = true, schema = @Schema(type = "string", example = "2025-09-30"))
                    },
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "El proceso de generación del reporte se ha ejecutado exitosamente."
                            ),
                            @ApiResponse(
                                    responseCode = "400",
                                    description = "Parámetros de fecha inválidos o faltantes.",
                                    content = @Content(schema = @Schema(implementation = Void.class))
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
    public RouterFunction<ServerResponse> generarReporteSolicitudesAprobadasDiario(){
        return RouterFunctions.route(GET(PATH).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::ejecutar);
    }

}
