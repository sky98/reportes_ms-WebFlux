package co.com.pragma.model.mensaje.gateway;

import reactor.core.publisher.Mono;

public interface MensajeUtilsGateway {
    <T> Mono<String> serializar(T object);
    <T> Mono<T> deserializarMensaje(String messageBody, Class<T> targetClass);
}
