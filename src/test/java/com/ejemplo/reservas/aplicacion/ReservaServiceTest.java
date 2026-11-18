package com.ejemplo.reservas.aplicacion;

import com.ejemplo.reservas.dominio.EstadoReserva;
import com.ejemplo.reservas.dominio.FechasNoDisponiblesException;
import com.ejemplo.reservas.dominio.RangoFechas;
import com.ejemplo.reservas.dominio.Reserva;
import com.ejemplo.reservas.dominio.SolicitudReserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Prueba del componente ReservaService.
 * Como depende de interfaces, se prueba con dobles simples: sin base de datos,
 * sin pasarela real y sin correo real.
 */
class ReservaServiceTest {

    private RepositorioEnMemoria repositorio;
    private PasarelaFalsa pago;
    private NotificadorFalso notificador;
    private ReservaService servicio;

    @BeforeEach
    void preparar() {
        repositorio = new RepositorioEnMemoria();
        pago = new PasarelaFalsa();
        notificador = new NotificadorFalso();
        servicio = new ReservaService(new DisponibilidadService(repositorio), repositorio, pago, notificador);
    }

    // Traza: HU-03 · RF-02
    @Test
    void debeConfirmarYNotificar() {
        SolicitudReserva solicitud = new SolicitudReserva("ana@correo.com",
                new RangoFechas(LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 23)),
                new BigDecimal("50.00"));

        Reserva r = servicio.crearReserva(solicitud);

        assertNotNull(r.id());
        assertEquals(EstadoReserva.CONFIRMADA, r.estado());
        assertEquals(new BigDecimal("150.00"), r.total());          // 3 noches x 50
        assertEquals(List.of(new BigDecimal("150.00")), pago.cobros);
        assertEquals(List.of(r), notificador.confirmadas);
    }

    @Test
    void noDebeCobrarNiNotificarSiLasFechasEstanOcupadas() {
        RangoFechas fechas = new RangoFechas(LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 23));
        servicio.crearReserva(new SolicitudReserva("ana@correo.com", fechas, new BigDecimal("50.00")));

        RangoFechas solapadas = new RangoFechas(LocalDate.of(2026, 12, 22), LocalDate.of(2026, 12, 24));
        assertThrows(FechasNoDisponiblesException.class, () ->
                servicio.crearReserva(new SolicitudReserva("luis@correo.com", solapadas, new BigDecimal("50.00"))));

        assertEquals(1, pago.cobros.size());
        assertEquals(1, notificador.confirmadas.size());
        assertEquals(1, repositorio.buscarTodas().size());
    }

    // ---- Dobles de prueba: implementan las mismas interfaces que los componentes reales ----

    static class RepositorioEnMemoria implements IReservaRepository {
        private final List<Reserva> datos = new ArrayList<>();

        @Override
        public Reserva guardar(Reserva reserva) {
            Reserva guardada = reserva.conId((long) datos.size() + 1);
            datos.add(guardada);
            return guardada;
        }

        @Override
        public List<Reserva> buscarTodas() {
            return List.copyOf(datos);
        }
    }

    static class PasarelaFalsa implements IPasarelaPago {
        final List<BigDecimal> cobros = new ArrayList<>();

        @Override
        public void cobrar(BigDecimal monto) {
            cobros.add(monto);
        }
    }

    static class NotificadorFalso implements INotificador {
        final List<Reserva> confirmadas = new ArrayList<>();

        @Override
        public void enviarConfirmacion(Reserva reserva) {
            confirmadas.add(reserva);
        }
    }
}
