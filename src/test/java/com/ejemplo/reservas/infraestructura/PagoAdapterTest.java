package com.ejemplo.reservas.infraestructura;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Prueba del componente PagoAdapter (pasarela simulada). */
class PagoAdapterTest {

    // Traza: RF-03
    @Test
    void debeRegistrarElCobroYRechazarMontosInvalidos() {
        PagoAdapter pago = new PagoAdapter();

        pago.cobrar(new BigDecimal("150.00"));

        assertEquals(List.of(new BigDecimal("150.00")), pago.cobros());
        assertThrows(IllegalArgumentException.class, () -> pago.cobrar(BigDecimal.ZERO));
    }

    // Traza: RF-05
    @Test
    void debeRegistrarElReembolso() {
        PagoAdapter pago = new PagoAdapter();

        pago.reembolsar(new BigDecimal("150.00"));

        assertEquals(List.of(new BigDecimal("150.00")), pago.reembolsos());
    }
}
