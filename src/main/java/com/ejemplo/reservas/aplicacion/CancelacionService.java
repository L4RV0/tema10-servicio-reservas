package com.ejemplo.reservas.aplicacion;

import com.ejemplo.reservas.dominio.Reserva;
import com.ejemplo.reservas.dominio.ReservaNoEncontradaException;

/** Componente CancelacionService: cancela una reserva y devuelve el dinero. */
public class CancelacionService implements ICancelacion {

    private final IReservaRepository repositorio;
    private final IPasarelaPago pago;

    public CancelacionService(IReservaRepository repositorio, IPasarelaPago pago) {
        this.repositorio = repositorio;
        this.pago = pago;
    }

    // Traza: RF-05
    @Override
    public Reserva cancelar(Long idReserva) {
        Reserva r = repositorio.buscarPorId(idReserva)
                .orElseThrow(() -> new ReservaNoEncontradaException(idReserva));
        Reserva cancelada = repositorio.guardar(r.cancelada());
        pago.reembolsar(cancelada.total());
        return cancelada;
    }
}
