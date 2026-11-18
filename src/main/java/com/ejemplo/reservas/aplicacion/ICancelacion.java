package com.ejemplo.reservas.aplicacion;

import com.ejemplo.reservas.dominio.Reserva;

/** Interfaz del componente CancelacionService. */
public interface ICancelacion {
    Reserva cancelar(Long idReserva);
}
