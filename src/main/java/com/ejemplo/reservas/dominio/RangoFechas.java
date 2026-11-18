package com.ejemplo.reservas.dominio;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Fechas de entrada y salida de una estadía. */
public record RangoFechas(LocalDate entrada, LocalDate salida) {

    public RangoFechas {
        if (entrada == null || salida == null || !salida.isAfter(entrada)) {
            throw new IllegalArgumentException("La salida debe ser posterior a la entrada");
        }
    }

    public long noches() {
        return ChronoUnit.DAYS.between(entrada, salida);
    }

    public boolean seSolapaCon(RangoFechas otro) {
        return entrada.isBefore(otro.salida) && otro.entrada.isBefore(salida);
    }
}
