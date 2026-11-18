package com.ejemplo.reservas.arquitectura;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Responde a la pregunta del debate: ¿cómo detectar que el diagrama quedó desactualizado?
 * Compara los componentes declarados en docs/arquitectura/workspace.dsl con las clases reales,
 * en los dos sentidos (ida y vuelta).
 */
class ModeloSincronizadoTest {

    static final Path MODELO = Path.of("docs/arquitectura/workspace.dsl");
    static final Path CODIGO = Path.of("src/main/java/com/ejemplo/reservas");
    static final List<String> PAQUETES_DE_COMPONENTES = List.of("api", "aplicacion", "infraestructura");

    static Set<String> componentesDelModelo() throws IOException {
        Matcher m = Pattern.compile("component\\s+\"([^\"]+)\"").matcher(Files.readString(MODELO));
        Set<String> nombres = new TreeSet<>();
        while (m.find()) {
            nombres.add(m.group(1));
        }
        return nombres;
    }

    /** Clases concretas (no interfaces) de los paquetes que contienen componentes. */
    static Set<String> clasesDelCodigo() throws IOException {
        Set<String> clases = new TreeSet<>();
        for (String paquete : PAQUETES_DE_COMPONENTES) {
            try (Stream<Path> archivos = Files.list(CODIGO.resolve(paquete))) {
                for (Path archivo : archivos.collect(Collectors.toList())) {
                    String fuente = Files.readString(archivo);
                    String nombre = archivo.getFileName().toString().replace(".java", "");
                    if (fuente.contains("public class " + nombre)) {
                        clases.add(nombre);
                    }
                }
            }
        }
        return clases;
    }

    @Test
    void cadaComponenteDelDiagramaExisteEnElCodigo() throws IOException {
        Set<String> faltan = new TreeSet<>(componentesDelModelo());
        faltan.removeAll(clasesDelCodigo());
        assertTrue(faltan.isEmpty(),
                "El diagrama nombra componentes que no existen en el código: " + faltan);
    }

    @Test
    void cadaClaseDeComponenteApareceEnElDiagrama() throws IOException {
        Set<String> sinDibujar = new TreeSet<>(clasesDelCodigo());
        sinDibujar.removeAll(componentesDelModelo());
        assertTrue(sinDibujar.isEmpty(),
                "Hay clases que no aparecen en workspace.dsl (diagrama desactualizado): " + sinDibujar);
    }
}
