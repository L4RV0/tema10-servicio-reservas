package com.ejemplo.reservas.dominio;

public class FechasNoDisponiblesException extends RuntimeException {
    public FechasNoDisponiblesException(RangoFechas fechas) {
        super("La habitación ya está reservada entre " + fechas.entrada() + " y " + fechas.salida());
    }
}
