package co.com.pragma.api;

import co.com.pragma.api.dto.response.ReporteResponse;
import co.com.pragma.model.reporte.Reporte;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReporteMapper {
    ReporteResponse toResponse(Reporte reporte);

}
