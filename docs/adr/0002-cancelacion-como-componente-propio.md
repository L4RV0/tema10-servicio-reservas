# ADR 0002 · La cancelación con reembolso es un componente propio

- **Estado:** Aceptada
- **Requisito:** RF-05 Cancelar una reserva con reembolso

## Contexto

Llega RF-05: el cliente puede cancelar una reserva confirmada y se le devuelve el dinero. Había dos opciones:

- **(a)** Añadir un método `cancelar()` a `ReservaService`.
- **(b)** Crear un componente nuevo.

`ReservaService` ya tiene cuatro dependencias y una responsabilidad clara: orquestar la creación.

## Decisión

- Se crea `CancelacionService`, con la interfaz `ICancelacion`. Depende solo de `IReservaRepository` y de `IPasarelaPago`.
- `IPasarelaPago` gana la operación `reembolsar()`, implementada en `PagoAdapter`.

## Consecuencias

- **Contexto y contenedores no cambian:** el cliente, la pasarela y la API son los mismos.
- **Sí cambian el nivel de componentes** (`workspace.dsl`), **el código**, **la matriz** (fila RF-05) y **las pruebas** (`CancelacionServiceTest`).
- Todo va en un solo commit. Si faltara el modelo, `ModeloSincronizadoTest` fallaría con «Hay clases que no aparecen en workspace.dsl: [CancelacionService]».
