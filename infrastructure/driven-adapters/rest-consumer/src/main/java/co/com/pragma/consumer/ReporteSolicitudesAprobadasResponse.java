package co.com.pragma.consumer;

import co.com.pragma.model.reportediario.ReporteDiario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReporteSolicitudesAprobadasResponse {
    List<ReporteDiario> reporteDiarioList;
}
