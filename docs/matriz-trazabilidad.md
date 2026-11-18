# Matriz de trazabilidad: requisito → modelo → código → prueba

Se lee en los dos sentidos:
- **Ida** (requisito → prueba): ¿cada requisito está implementado y probado?
- **Vuelta** (prueba → requisito): ¿por qué existe cada componente y cada prueba?

`MatrizTrazabilidadTest` comprueba en cada `mvn test` que el componente exista en `workspace.dsl`, que la función exista en esa clase y que la prueba exista. Por eso una fila desactualizada rompe la compilación.

| Requisito | Modelo (C4) | Código | Prueba |
|---|---|---|---|
| RF-01 Consultar disponibilidad | DisponibilidadService | verificar() | DisponibilidadServiceTest.debeAceptarFechasLibresYRechazarFechasOcupadas |
| RF-02 Crear reserva (HU-03) | ReservaService | crearReserva() | ReservaServiceTest.debeConfirmarYNotificar |
| RF-03 Cobrar la reserva | PagoAdapter | cobrar() | PagoAdapterTest.debeRegistrarElCobroYRechazarMontosInvalidos |
| RF-04 Confirmar por correo | NotificadorEmail | enviarConfirmacion() | NotificadorEmailTest.debeEnviarLaConfirmacionAlCliente |
| RF-05 Cancelar con reembolso | CancelacionService | cancelar() | CancelacionServiceTest.debeCancelarYReembolsar |
| RNF-01 Responder en menos de 2 s | Contenedor: API de Reservas | Configuración y caché | Pendiente: prueba de carga (no automatizada en el prototipo) |

**HU-03:** Como cliente, quiero reservar una habitación y recibir la confirmación por correo.
