package com.ejemplo.reservas.aplicacion;

import com.ejemplo.reservas.dominio.Reserva;
import com.ejemplo.reservas.dominio.SolicitudReserva;

/** Interfaz del componente ReservaService. */
public interface IReservaService {
    Reserva crearReserva(SolicitudReserva solicitud);
}
