# Servicio de Reservas · Tema 10 «De componentes a código»

Prototipo para la exposición del Tema 10 (Unidad 4 · 4.3, Modelado Ágil de Software).

Muestra cómo un **diagrama de componentes C4** se corresponde con **clases reales**. También muestra cómo una historia de usuario se sigue desde el contexto hasta una **función** y su **prueba**. Todo queda versionado junto: código, modelo y matriz.

> El caso es ilustrativo. La pasarela de pago y el correo están **simulados** (escriben en el log). La base de datos está **en memoria**. En el diagrama aparecen como sistemas reales porque así sería en producción: cambiarlos solo tocaría `PagoAdapter`, `NotificadorEmail` y `ReservaRepository`.

## Cómo ejecutarlo

Solo hace falta Java 21 o superior. No hay que instalar Maven: `mvnw` lo descarga la primera vez.

```powershell
.\mvnw.cmd test               # compila y ejecuta las 13 pruebas
.\mvnw.cmd spring-boot:run    # levanta la API en http://localhost:8080
```

Crear una reserva con la API en marcha (en otra terminal de PowerShell):

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/reservas -ContentType "application/json" -Body '{"emailCliente":"ana@correo.com","fechas":{"entrada":"2027-01-10","salida":"2027-01-12"},"precioPorNoche":40}'
```

Si se repite la misma petición, la respuesta es **409 Conflict** porque las fechas ya están ocupadas.

## Del diagrama al código

| Nivel C4 | Dónde está |
|---|---|
| 1 · Contexto y 2 · Contenedores | `docs/arquitectura/workspace.dsl` (vistas `Nivel1_Contexto` y `Nivel2_Contenedores`) |
| 3 · Componentes | Mismo archivo, vista `Nivel3_Componentes` |
| 4 · Código | `src/main/java/com/ejemplo/reservas/aplicacion/ReservaService.java` |

| Componente = clase | Interfaz | Paquete |
|---|---|---|
| ReservasController | `POST /reservas` | `api` |
| ReservaService | `IReservaService` | `aplicacion` |
| DisponibilidadService | `IDisponibilidad` | `aplicacion` |
| ReservaRepository | `IReservaRepository` | `infraestructura` |
| PagoAdapter | `IPasarelaPago` | `infraestructura` |
| NotificadorEmail | `INotificador` | `infraestructura` |

Para ver los diagramas, pega el contenido de `workspace.dsl` en el editor en línea de Structurizr o ábrelo con Structurizr Lite.

## Cómo se detecta un diagrama desactualizado

Tres pruebas en `src/test/java/com/ejemplo/reservas/arquitectura/` hacen fallar `mvnw test` si la documentación y el código se separan:

| Prueba | Qué compara |
|---|---|
| `ModeloSincronizadoTest` | Componentes de `workspace.dsl` con las clases reales, en los dos sentidos |
| `MatrizTrazabilidadTest` | Cada fila de `docs/matriz-trazabilidad.md` con el componente, la función y la prueba |
| `ArquitecturaTest` (ArchUnit) | Las flechas del diagrama: `aplicacion` no puede depender de `infraestructura`, de `api` ni de Spring |

## Estructura

```
docs/
  arquitectura/workspace.dsl     modelo C4 como código
  matriz-trazabilidad.md         requisito → modelo → código → prueba
  adr/                           decisiones de arquitectura (formato Nygard)
src/main/java/com/ejemplo/reservas/
  api/                           ReservasController
  aplicacion/                    ReservaService, DisponibilidadService e interfaces
  infraestructura/               ReservaRepository, PagoAdapter, NotificadorEmail
  dominio/                       Reserva, SolicitudReserva, RangoFechas…
  ConfiguracionComponentes.java  único lugar donde se conectan los componentes
```

## El cambio RF-05 (cancelar con reembolso)

La rama `rf-05-cancelacion` hace el cambio de la diapositiva 11 en **un solo commit**: código, modelo, matriz, prueba y ADR. Para verlo:

```powershell
git diff main rf-05-cancelacion --stat
```
