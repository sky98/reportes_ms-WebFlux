package co.com.pragma.model.reportediario;
import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.usuario.Usuario;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReporteDiario {
    private Usuario usuario;
    private Solicitud solicitud;
}
