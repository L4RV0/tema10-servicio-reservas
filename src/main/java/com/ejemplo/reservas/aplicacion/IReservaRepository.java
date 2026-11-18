package com.ejemplo.reservas.aplicacion;

import com.ejemplo.reservas.dominio.Reserva;

import java.util.List;

/** Interfaz del componente ReservaRepository. */
public interface IReservaRepository {
    Reserva guardar(Reserva reserva);

    List<Reserva> buscarTodas();
}
