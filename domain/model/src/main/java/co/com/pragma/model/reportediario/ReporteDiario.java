package co.com.pragma.model.reportediario;

import co.com.pragma.model.solicitud.Solicitud;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReporteDiario {
    private Long documentoId;
    private String nombresUsuario;
    private String apellidosUsuario;
    private String correoElectronico;
    private BigDecimal salarioBase;
    private List<Solicitud> solicitud;
}
