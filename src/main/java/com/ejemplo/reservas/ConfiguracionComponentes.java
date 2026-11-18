package com.ejemplo.reservas;

import com.ejemplo.reservas.aplicacion.DisponibilidadService;
import com.ejemplo.reservas.aplicacion.IDisponibilidad;
import com.ejemplo.reservas.aplicacion.INotificador;
import com.ejemplo.reservas.aplicacion.IPasarelaPago;
import com.ejemplo.reservas.aplicacion.IReservaRepository;
import com.ejemplo.reservas.aplicacion.IReservaService;
import com.ejemplo.reservas.aplicacion.ReservaService;
import com.ejemplo.reservas.infraestructura.NotificadorEmail;
import com.ejemplo.reservas.infraestructura.PagoAdapter;
import com.ejemplo.reservas.infraestructura.ReservaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Único lugar donde se conectan los componentes.
 * Así las clases de "aplicacion" no dependen de Spring ni de la infraestructura.
 */
@Configuration
public class ConfiguracionComponentes {

    @Bean
    IReservaRepository reservaRepository() {
        return new ReservaRepository();
    }

    @Bean
    IPasarelaPago pagoAdapter() {
        return new PagoAdapter();
    }

    @Bean
    INotificador notificadorEmail() {
        return new NotificadorEmail();
    }

    @Bean
    IDisponibilidad disponibilidadService(IReservaRepository repositorio) {
        return new DisponibilidadService(repositorio);
    }

    @Bean
    IReservaService reservaService(IDisponibilidad disponibilidad, IReservaRepository repositorio,
                                   IPasarelaPago pago, INotificador notificador) {
        return new ReservaService(disponibilidad, repositorio, pago, notificador);
    }
}
