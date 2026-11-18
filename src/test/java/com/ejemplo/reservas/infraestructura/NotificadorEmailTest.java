package com.ejemplo.reservas.infraestructura;

import com.ejemplo.reservas.dominio.RangoFechas;
import com.ejemplo.reservas.dominio.Reserva;
import com.ejemplo.reservas.dominio.SolicitudReserva;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Prueba del componente NotificadorEmail (correo simulado). */
class NotificadorEmailTest {

    // Traza: RF-04
    @Test
    void debeEnviarLaConfirmacionAlCliente() {
        NotificadorEmail notificador = new NotificadorEmail();
        Reserva reserva = Reserva.nueva(new SolicitudReserva("ana@correo.com",
                new RangoFechas(LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 23)),
                new BigDecimal("50.00"))).conId(7L);

        notificador.enviarConfirmacion(reserva);

        assertEquals(1, notificador.enviados().size());
        String mensaje = notificador.enviados().get(0);
        assertTrue(mensaje.startsWith("Para: ana@correo.com"));
        assertTrue(mensaje.contains("Reserva #7 confirmada"));
    }
}
