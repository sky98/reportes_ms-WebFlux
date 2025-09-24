package co.com.pragma.usecase.generarreportesolicitudesaprobadasdiario;

import co.com.pragma.model.reportediario.ReporteDiario;
import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRestConsumerGateway;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRestConsumerGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GenerarReporteSolicitudesAprobadasDiarioUseCase {

    private final SolicitudRestConsumerGateway solicitudRestConsumerGateway;
    private final UsuarioRestConsumerGateway usuarioRestConsumerGateway;

    public Mono<List<ReporteDiario>> ejecutar(String fechaInicio, String fechaFin){
        return validarFechas(fechaInicio, fechaFin)
                .flatMap(voidResult -> {
                    Mono<List<Solicitud>> solicitudesMono =
                            solicitudRestConsumerGateway.obtenerSolicitudesAprobadasPorFecha(fechaInicio, fechaFin)
                                    .cache();
                    Mono<List<Usuario>> usuariosMono = solicitudesMono
                            .flatMap(solicitudes ->
                                    Flux.fromIterable(solicitudes)
                                            .map(Solicitud::getDocumentoId)
                                            .distinct()
                                            .collectList()
                            )
                            .flatMapMany(Flux::fromIterable)
                            .flatMap(usuarioRestConsumerGateway::obtenerUsuarioPorDocumentoId)
                            .collectList();
                    return Mono.zip(solicitudesMono, usuariosMono);
                })
                .map(tuple -> {
                    List<Solicitud> solicitudes = tuple.getT1();
                    List<Usuario> usuarios = tuple.getT2();
                    Map<Long, Usuario> usuarioMap = usuarios.stream()
                            .collect(Collectors.toMap(Usuario::getDocumentoId, Function.identity()));

                    return solicitudes.stream()
                            .map(solicitud -> {
                                Usuario usuario = usuarioMap.get(solicitud.getDocumentoId());
                                return new ReporteDiario(usuario, solicitud);
                            })
                            .collect(Collectors.toList());
                });
    }

    private Mono<Void> validarFechas(String fechaInicio, String fechaFin) {
        Function<String, Mono<LocalDate>> parseAndValidate = fechaStr ->
                Mono.justOrEmpty(fechaStr)
                        .filter(str -> !str.trim().isEmpty())
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("La fecha no puede ser nula o vacía.")))
                        .map(str -> {
                            try {
                                return LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            } catch (DateTimeParseException e) {
                                throw new IllegalArgumentException("El formato de fecha no es válido. Use el formato 'yyyy-MM-dd'.");
                            }
                        });

        return Mono.zip(
                        parseAndValidate.apply(fechaInicio),
                        parseAndValidate.apply(fechaFin)
                )
                .filter(tuple -> !tuple.getT1().isAfter(tuple.getT2()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.")))
                .then(Mono.empty())
                .onErrorResume(throwable -> Mono.error(new IllegalArgumentException(throwable.getMessage()))).then();
    }

}
