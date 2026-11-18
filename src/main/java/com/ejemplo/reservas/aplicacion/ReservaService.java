package com.ejemplo.reservas.aplicacion;

import com.ejemplo.reservas.dominio.Reserva;
import com.ejemplo.reservas.dominio.SolicitudReserva;

/**
 * Componente ReservaService: orquesta la reserva.
 * Depende solo de interfaces, nunca de clases de infraestructura.
 */
public class ReservaService implements IReservaService {

    private final IDisponibilidad disponibilidad;
    private final IReservaRepository repositorio;
    private final IPasarelaPago pago;
    private final INotificador notificador;

    public ReservaService(IDisponibilidad disponibilidad, IReservaRepository repositorio,
                          IPasarelaPago pago, INotificador notificador) {
        this.disponibilidad = disponibilidad;
        this.repositorio = repositorio;
        this.pago = pago;
        this.notificador = notificador;
    }

    // Traza: HU-03 · RF-02
    @Override
    public Reserva crearReserva(SolicitudReserva s) {
        disponibilidad.verificar(s.fechas());
        Reserva r = repositorio.guardar(Reserva.nueva(s));
        pago.cobrar(r.total());
        notificador.enviarConfirmacion(r);
        return r;
    }
}
