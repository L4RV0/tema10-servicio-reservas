package com.ejemplo.reservas.aplicacion;

import com.ejemplo.reservas.dominio.Reserva;

/** Interfaz del componente NotificadorEmail. */
public interface INotificador {
    void enviarConfirmacion(Reserva reserva);
}
