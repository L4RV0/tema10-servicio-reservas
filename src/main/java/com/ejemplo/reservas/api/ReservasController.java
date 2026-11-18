package com.ejemplo.reservas.api;

import com.ejemplo.reservas.aplicacion.ICancelacion;
import com.ejemplo.reservas.aplicacion.IReservaService;
import com.ejemplo.reservas.dominio.FechasNoDisponiblesException;
import com.ejemplo.reservas.dominio.Reserva;
import com.ejemplo.reservas.dominio.ReservaNoEncontradaException;
import com.ejemplo.reservas.dominio.SolicitudReserva;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Componente ReservasController: recibe la petición HTTP y delega en los servicios. */
@RestController
@RequestMapping("/reservas")
public class ReservasController {

    private final IReservaService reservas;
    private final ICancelacion cancelacion;

    public ReservasController(IReservaService reservas, ICancelacion cancelacion) {
        this.reservas = reservas;
        this.cancelacion = cancelacion;
    }

    // Traza: HU-03 · RF-02
    @PostMapping
    public ResponseEntity<Reserva> crear(@RequestBody SolicitudReserva solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservas.crearReserva(solicitud));
    }

    // Traza: RF-05
    @PostMapping("/{id}/cancelacion")
    public Reserva cancelar(@PathVariable Long id) {
        return cancelacion.cancelar(id);
    }

    @ExceptionHandler(FechasNoDisponiblesException.class)
    public ResponseEntity<Map<String, String>> fechasOcupadas(FechasNoDisponiblesException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(ReservaNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> noEncontrada(ReservaNoEncontradaException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, String>> datosInvalidos(RuntimeException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}
