package com.ejemplo.reservas.aplicacion;

import com.ejemplo.reservas.dominio.FechasNoDisponiblesException;
import com.ejemplo.reservas.dominio.RangoFechas;
import com.ejemplo.reservas.dominio.Reserva;
import com.ejemplo.reservas.dominio.SolicitudReserva;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Prueba del componente DisponibilidadService. */
class DisponibilidadServiceTest {

    private static RangoFechas diciembre(int diaEntrada, int diaSalida) {
        return new RangoFechas(LocalDate.of(2026, 12, diaEntrada), LocalDate.of(2026, 12, diaSalida));
    }

    // Traza: RF-01
    @Test
    void debeAceptarFechasLibresYRechazarFechasOcupadas() {
        ReservaServiceTest.RepositorioEnMemoria repositorio = new ReservaServiceTest.RepositorioEnMemoria();
        repositorio.guardar(Reserva.nueva(new SolicitudReserva("ana@correo.com", diciembre(20, 23), BigDecimal.TEN)));
        DisponibilidadService disponibilidad = new DisponibilidadService(repositorio);

        assertDoesNotThrow(() -> disponibilidad.verificar(diciembre(23, 25)));   // entra el día en que la otra sale
        assertThrows(FechasNoDisponiblesException.class, () -> disponibilidad.verificar(diciembre(21, 22)));
    }
}
