package com.ejemplo.reservas.aplicacion;

import com.ejemplo.reservas.dominio.EstadoReserva;
import com.ejemplo.reservas.dominio.FechasNoDisponiblesException;
import com.ejemplo.reservas.dominio.RangoFechas;

/** Componente DisponibilidadService: verifica que las fechas estén libres. */
public class DisponibilidadService implements IDisponibilidad {

    private final IReservaRepository repositorio;

    public DisponibilidadService(IReservaRepository repositorio) {
        this.repositorio = repositorio;
    }

    // Traza: RF-01
    @Override
    public void verificar(RangoFechas fechas) {
        boolean ocupada = repositorio.buscarTodas().stream()
                .filter(r -> r.estado() == EstadoReserva.CONFIRMADA)
                .anyMatch(r -> r.fechas().seSolapaCon(fechas));
        if (ocupada) {
            throw new FechasNoDisponiblesException(fechas);
        }
    }
}
