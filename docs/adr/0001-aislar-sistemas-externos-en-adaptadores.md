# ADR 0001 · Aislar los sistemas externos en componentes adaptadores

- **Estado:** Aceptada
- **Formato:** Michael Nygard, "Documenting Architecture Decisions" (2011)

## Contexto

La API de Reservas usa tres cosas que no controlamos: la pasarela de pago, el servicio de correo y la base de datos. Si `ReservaService` hablara con ellas directamente, no se podría probar sin conexión, y cambiar de pasarela obligaría a tocar la lógica del caso.

## Decisión

- Cada sistema externo queda detrás de un componente propio (`PagoAdapter`, `NotificadorEmail`, `ReservaRepository`), expuesto por una interfaz (`IPasarelaPago`, `INotificador`, `IReservaRepository`).
- Los componentes del paquete `aplicacion` dependen solo de esas interfaces.
- Los componentes se conectan en un único lugar: `ConfiguracionComponentes`.

## Consecuencias

- `ReservaServiceTest` prueba la orquestación con dobles simples, sin red ni base de datos.
- Cambiar de pasarela solo cambia `PagoAdapter`.
- `ArquitecturaTest` (ArchUnit) hace fallar la compilación si `aplicacion` empieza a depender de `infraestructura`, de `api` o de Spring.
- Coste: hay una interfaz más por componente.
