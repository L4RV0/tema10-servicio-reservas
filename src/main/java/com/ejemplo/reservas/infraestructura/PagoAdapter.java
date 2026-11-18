package com.ejemplo.reservas.infraestructura;

import com.ejemplo.reservas.aplicacion.IPasarelaPago;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Componente PagoAdapter: aísla a la pasarela de pago externa.
 * En el prototipo la pasarela es simulada: registra el cobro y lo escribe en el log.
 */
public class PagoAdapter implements IPasarelaPago {

    private static final Logger LOG = Logger.getLogger(PagoAdapter.class.getName());
    private final List<BigDecimal> cobros = new ArrayList<>();

    // Traza: RF-03
    @Override
    public void cobrar(BigDecimal monto) {
        if (monto == null || monto.signum() <= 0) {
            throw new IllegalArgumentException("El monto a cobrar debe ser positivo");
        }
        cobros.add(monto);
        LOG.info("[Pasarela simulada] Cobro de " + monto);
    }

    public List<BigDecimal> cobros() {
        return List.copyOf(cobros);
    }
}
