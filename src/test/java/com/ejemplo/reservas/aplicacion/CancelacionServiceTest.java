package com.ejemplo.reservas.aplicacion;

import com.ejemplo.reservas.dominio.EstadoReserva;
import com.ejemplo.reservas.dominio.RangoFechas;
import com.ejemplo.reservas.dominio.Reserva;
import com.ejemplo.reservas.dominio.ReservaNoEncontradaException;
import com.ejemplo.reservas.dominio.SolicitudReserva;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Prueba del componente CancelacionService. */
class CancelacionServiceTest {

    private final ReservaServiceTest.RepositorioEnMemoria repositorio = new ReservaServiceTest.RepositorioEnMemoria();
    private final ReservaServiceTest.PasarelaFalsa pago = new ReservaServiceTest.PasarelaFalsa();
    private final CancelacionService cancelacion = new CancelacionService(repositorio, pago);

    // Traza: RF-05
    @Test
    void debeCancelarYReembolsar() {
        Reserva r = repositorio.guardar(Reserva.nueva(new SolicitudReserva("ana@correo.com",
                new RangoFechas(LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 23)),
                new BigDecimal("50.00"))));

        Reserva cancelada = cancelacion.cancelar(r.id());

        assertEquals(EstadoReserva.CANCELADA, cancelada.estado());
        assertEquals(EstadoReserva.CANCELADA, repositorio.buscarPorId(r.id()).orElseThrow().estado());
        assertEquals(List.of(new BigDecimal("150.00")), pago.reembolsos);
    }

    @Test
    void noDebeReembolsarDosVecesNiCancelarLoQueNoExiste() {
        Reserva r = repositorio.guardar(Reserva.nueva(new SolicitudReserva("ana@correo.com",
                new RangoFechas(LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 21)),
                new BigDecimal("50.00"))));
        cancelacion.cancelar(r.id());

        assertThrows(IllegalStateException.class, () -> cancelacion.cancelar(r.id()));
        assertThrows(ReservaNoEncontradaException.class, () -> cancelacion.cancelar(999L));
        assertEquals(1, pago.reembolsos.size());
    }
}
