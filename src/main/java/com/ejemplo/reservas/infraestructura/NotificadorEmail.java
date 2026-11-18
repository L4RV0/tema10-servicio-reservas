package com.ejemplo.reservas.infraestructura;

import com.ejemplo.reservas.aplicacion.INotificador;
import com.ejemplo.reservas.dominio.Reserva;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Componente NotificadorEmail: aísla al servicio de correo externo.
 * En el prototipo el envío es simulado: arma el mensaje y lo escribe en el log.
 */
public class NotificadorEmail implements INotificador {

    private static final Logger LOG = Logger.getLogger(NotificadorEmail.class.getName());
    private final List<String> enviados = new ArrayList<>();

    // Traza: RF-04
    @Override
    public void enviarConfirmacion(Reserva reserva) {
        String mensaje = construirMensaje(reserva);
        enviados.add(mensaje);
        LOG.info("[Correo simulado] " + mensaje);
    }

    String construirMensaje(Reserva r) {
        return "Para: " + r.emailCliente()
                + " | Reserva #" + r.id() + " confirmada del " + r.fechas().entrada()
                + " al " + r.fechas().salida() + ". Total: " + r.total();
    }

    public List<String> enviados() {
        return List.copyOf(enviados);
    }
}
