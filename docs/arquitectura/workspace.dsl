/*
 * Modelo C4 del Servicio de Reservas, escrito como código (Structurizr DSL).
 * Vive en el mismo repositorio que el código y cambia en el mismo commit.
 * ModeloSincronizadoTest comprueba que los componentes de aquí existan como clases, y al revés.
 */
workspace "Servicio de Reservas" "Tema 10 · De componentes a código" {

    model {
        cliente = person "Cliente" "Persona que reserva una habitación"

        pasarela = softwareSystem "Pasarela de pago" "Sistema externo que cobra con tarjeta" {
            tags "Externo"
        }
        correo = softwareSystem "Servicio de correo" "Sistema externo que envía correos" {
            tags "Externo"
        }

        reservas = softwareSystem "Sistema de Reservas" "Nuestro software" {
            web = container "App web" "Interfaz del cliente en el navegador" "React"

            api = container "API de Reservas" "Expone POST /reservas" "Java + Spring Boot" {
                controller = component "ReservasController" "Recibe la petición HTTP" "Spring MVC"
                servicio = component "ReservaService" "Orquesta la reserva" "Java"
                disponibilidad = component "DisponibilidadService" "Verifica que las fechas estén libres" "Java"
                repositorio = component "ReservaRepository" "Guarda y lee reservas" "Java"
                pago = component "PagoAdapter" "Cobra con la pasarela externa" "Java"
                notificador = component "NotificadorEmail" "Envía la confirmación" "Java"
            }

            db = container "Base de datos" "Guarda las reservas" "PostgreSQL" {
                tags "BaseDeDatos"
            }
        }

        cliente -> web "Reserva desde el navegador"
        web -> controller "POST /reservas" "JSON/HTTPS"
        controller -> servicio "Delega en IReservaService"
        servicio -> disponibilidad "Verifica fechas (IDisponibilidad)"
        servicio -> repositorio "Guarda (IReservaRepository)"
        servicio -> pago "Cobra (IPasarelaPago)"
        servicio -> notificador "Confirma (INotificador)"
        disponibilidad -> repositorio "Consulta reservas"
        repositorio -> db "Lee y escribe" "JDBC"
        pago -> pasarela "Cobra" "HTTPS"
        notificador -> correo "Envía el correo" "SMTP"
    }

    views {
        systemContext reservas "Nivel1_Contexto" {
            include *
            autolayout tb
        }

        container reservas "Nivel2_Contenedores" {
            include *
            autolayout lr
        }

        component api "Nivel3_Componentes" {
            include *
            autolayout tb
        }

        styles {
            element "Person" {
                shape Person
                background #1B2B34
                color #F6F1E7
            }
            element "Software System" {
                background #F6F1E7
                color #1B2B34
            }
            element "Container" {
                background #2F6F73
                color #F6F1E7
            }
            element "Component" {
                background #E8684A
                color #1B2B34
            }
            element "Externo" {
                background #EAE2D3
                color #1B2B34
            }
            element "BaseDeDatos" {
                shape Cylinder
            }
        }
    }
}
