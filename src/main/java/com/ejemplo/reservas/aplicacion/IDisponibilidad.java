package com.ejemplo.reservas.aplicacion;

import com.ejemplo.reservas.dominio.RangoFechas;

/** Interfaz del componente DisponibilidadService. */
public interface IDisponibilidad {
    /** Lanza FechasNoDisponiblesException si las fechas ya están ocupadas. */
    void verificar(RangoFechas fechas);
}
