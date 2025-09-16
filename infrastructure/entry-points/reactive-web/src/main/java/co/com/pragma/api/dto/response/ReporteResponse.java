package co.com.pragma.api.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ReporteResponse(
        int cantidad,
        BigDecimal monto
) {
}
