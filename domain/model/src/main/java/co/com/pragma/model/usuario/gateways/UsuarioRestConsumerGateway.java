package co.com.pragma.model.usuario.gateways;

import co.com.pragma.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRestConsumerGateway {
    Mono<Usuario> obtenerUsuarioPorDocumentoId(Long documentoId);
}
