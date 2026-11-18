package com.ejemplo.reservas.aplicacion;

import com.ejemplo.reservas.dominio.Reserva;

import java.util.List;
import java.util.Optional;

/** Interfaz del componente ReservaRepository. */
public interface IReservaRepository {
    Reserva guardar(Reserva reserva);

    List<Reserva> buscarTodas();

    Optional<Reserva> buscarPorId(Long id);
}
