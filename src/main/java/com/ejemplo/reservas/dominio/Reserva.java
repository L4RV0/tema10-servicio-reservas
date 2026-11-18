package com.ejemplo.reservas.dominio;

import java.math.BigDecimal;

/** Una reserva de la habitación. El repositorio le asigna el id al guardarla. */
public record Reserva(Long id, String emailCliente, RangoFechas fechas, BigDecimal total, EstadoReserva estado) {

    public static Reserva nueva(SolicitudReserva s) {
        BigDecimal total = s.precioPorNoche().multiply(BigDecimal.valueOf(s.fechas().noches()));
        return new Reserva(null, s.emailCliente(), s.fechas(), total, EstadoReserva.CONFIRMADA);
    }

    public Reserva conId(Long nuevoId) {
        return new Reserva(nuevoId, emailCliente, fechas, total, estado);
    }
}
