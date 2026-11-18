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
    private final List<BigDecimal> reembolsos = new ArrayList<>();

    // Traza: RF-03
    @Override
    public void cobrar(BigDecimal monto) {
        validar(monto);
        cobros.add(monto);
        LOG.info("[Pasarela simulada] Cobro de " + monto);
    }

    // Traza: RF-05
    @Override
    public void reembolsar(BigDecimal monto) {
        validar(monto);
        reembolsos.add(monto);
        LOG.info("[Pasarela simulada] Reembolso de " + monto);
    }

    private static void validar(BigDecimal monto) {
        if (monto == null || monto.signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
    }

    public List<BigDecimal> cobros() {
        return List.copyOf(cobros);
    }

    public List<BigDecimal> reembolsos() {
        return List.copyOf(reembolsos);
    }
}
