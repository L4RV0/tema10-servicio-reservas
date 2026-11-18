package com.ejemplo.reservas.dominio;

import java.math.BigDecimal;

/** Lo que el cliente envía al pedir una reserva (HU-03). */
public record SolicitudReserva(String emailCliente, RangoFechas fechas, BigDecimal precioPorNoche) {
}
