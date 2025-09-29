package co.com.pragma.model.errores;

import java.util.Set;

public class ErrorDeserializando extends RuntimeException implements ApplicationError{
    private Set<String> campos;

    public ErrorDeserializando(String mensaje, Set<String> campos) {
        super(mensaje);
        this.campos = campos;
    }

    @Override
    public Set<String> getCampos() {
        return campos;
    }
}
