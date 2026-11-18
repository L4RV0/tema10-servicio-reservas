package com.ejemplo.reservas;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Prueba del contenedor completo: petición HTTP, componentes conectados y respuesta. */
@SpringBootTest
@AutoConfigureMockMvc
class ReservasApiTest {

    @Autowired
    MockMvc mvc;

    private static final String SOLICITUD = """
            {"emailCliente":"ana@correo.com",
             "fechas":{"entrada":"2027-01-10","salida":"2027-01-12"},
             "precioPorNoche":40}""";

    // Traza: HU-03 · RF-02
    @Test
    void postReservasDebeCrearYLuegoRechazarLasMismasFechas() throws Exception {
        mvc.perform(post("/reservas").contentType(MediaType.APPLICATION_JSON).content(SOLICITUD))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("CONFIRMADA"))
                .andExpect(jsonPath("$.total").value(80));

        mvc.perform(post("/reservas").contentType(MediaType.APPLICATION_JSON).content(SOLICITUD))
                .andExpect(status().isConflict());
    }
}
