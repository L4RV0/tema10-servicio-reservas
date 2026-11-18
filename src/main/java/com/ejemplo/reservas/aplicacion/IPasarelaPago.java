package com.ejemplo.reservas.aplicacion;

import java.math.BigDecimal;

/** Interfaz del componente PagoAdapter. */
public interface IPasarelaPago {
    void cobrar(BigDecimal monto);

    void reembolsar(BigDecimal monto);
}
