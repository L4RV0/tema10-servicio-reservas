package com.ejemplo.reservas.infraestructura;

import com.ejemplo.reservas.aplicacion.IReservaRepository;
import com.ejemplo.reservas.dominio.Reserva;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Componente ReservaRepository: guarda y lee reservas.
 * En el prototipo guarda en memoria; en producción hablaría con PostgreSQL
 * sin que cambie nada fuera de esta clase.
 */
public class ReservaRepository implements IReservaRepository {

    private final Map<Long, Reserva> reservas = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();

    @Override
    public Reserva guardar(Reserva reserva) {
        Reserva guardada = reserva.id() == null ? reserva.conId(secuencia.incrementAndGet()) : reserva;
        reservas.put(guardada.id(), guardada);
        return guardada;
    }

    @Override
    public List<Reserva> buscarTodas() {
        return new ArrayList<>(reservas.values());
    }
}
