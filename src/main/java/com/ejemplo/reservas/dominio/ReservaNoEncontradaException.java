package com.ejemplo.reservas.dominio;

public class ReservaNoEncontradaException extends RuntimeException {
    public ReservaNoEncontradaException(Long id) {
        super("No existe la reserva #" + id);
    }
}
