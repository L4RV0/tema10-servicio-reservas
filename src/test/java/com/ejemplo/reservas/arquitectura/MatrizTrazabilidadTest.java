package com.ejemplo.reservas.arquitectura;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Comprueba que cada fila de docs/matriz-trazabilidad.md apunte a cosas que existen:
 * el componente en el modelo, la función en su clase y la prueba en src/test.
 * Las filas cuya prueba empieza con "Pendiente" se aceptan como deuda visible.
 */
class MatrizTrazabilidadTest {

    static final Path MATRIZ = Path.of("docs/matriz-trazabilidad.md");
    static final Path CODIGO = Path.of("src/main/java");
    static final Path PRUEBAS = Path.of("src/test/java");

    record Fila(String requisito, String modelo, String codigo, String prueba) {
    }

    static List<Fila> filas() throws IOException {
        List<Fila> filas = new ArrayList<>();
        for (String linea : Files.readAllLines(MATRIZ)) {
            if (!linea.startsWith("| RF") && !linea.startsWith("| RNF")) {
                continue;
            }
            String[] c = linea.split("\\|");
            filas.add(new Fila(c[1].trim(), c[2].trim(), c[3].trim(), c[4].trim()));
        }
        return filas;
    }

    static String fuenteDe(Path raiz, String clase) throws IOException {
        try (Stream<Path> archivos = Files.walk(raiz)) {
            Path archivo = archivos.filter(p -> p.getFileName().toString().equals(clase + ".java"))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("No existe la clase " + clase));
            return Files.readString(archivo);
        }
    }

    @Test
    void laMatrizTieneFilas() throws IOException {
        assertTrue(filas().size() >= 4, "La matriz debería tener al menos los requisitos RF-01 a RF-04");
    }

    @Test
    void cadaFilaApuntaAUnComponenteUnaFuncionYUnaPruebaReales() throws IOException {
        Set<String> componentes = ModeloSincronizadoTest.componentesDelModelo();
        List<String> errores = new ArrayList<>();

        for (Fila f : filas()) {
            if (f.modelo().startsWith("Contenedor")) {
                continue;   // requisito no funcional: vive en el contenedor, no en una función
            }
            if (!componentes.contains(f.modelo())) {
                errores.add(f.requisito() + ": el componente " + f.modelo() + " no está en workspace.dsl");
                continue;
            }
            String funcion = f.codigo().replace("()", "");
            if (!fuenteDe(CODIGO, f.modelo()).contains(" " + funcion + "(")) {
                errores.add(f.requisito() + ": " + f.modelo() + " no tiene la función " + funcion + "()");
            }
            if (f.prueba().startsWith("Pendiente")) {
                continue;
            }
            String[] prueba = f.prueba().split("\\.");
            String fuentePrueba = fuenteDe(PRUEBAS, prueba[0]);
            if (prueba.length > 1 && !fuentePrueba.contains("void " + prueba[1] + "(")) {
                errores.add(f.requisito() + ": " + prueba[0] + " no tiene la prueba " + prueba[1]);
            }
        }
        assertTrue(errores.isEmpty(), "Matriz desactualizada:\n" + String.join("\n", errores));
    }
}
