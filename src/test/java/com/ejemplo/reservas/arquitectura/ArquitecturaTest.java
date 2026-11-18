package com.ejemplo.reservas.arquitectura;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Reglas de dependencias del diagrama de componentes, comprobadas sobre el código compilado.
 * Si alguien rompe una flecha del diagrama en el código, esta prueba falla.
 */
@AnalyzeClasses(packages = "com.ejemplo.reservas", importOptions = ImportOption.DoNotIncludeTests.class)
class ArquitecturaTest {

    @ArchTest
    static final ArchRule aplicacionNoDependeDeInfraestructuraNiDeLaApi = noClasses()
            .that().resideInAPackage("..aplicacion..")
            .should().dependOnClassesThat().resideInAnyPackage("..infraestructura..", "..api..")
            .because("los componentes de aplicación dependen de interfaces, no de clases concretas");

    @ArchTest
    static final ArchRule aplicacionNoDependeDeSpring = noClasses()
            .that().resideInAPackage("..aplicacion..")
            .should().dependOnClassesThat().resideInAPackage("org.springframework..")
            .because("la lógica del caso no debe atarse al framework");

    @ArchTest
    static final ArchRule laApiSoloUsaInterfacesDeAplicacion = noClasses()
            .that().resideInAPackage("..api..")
            .should().dependOnClassesThat().resideInAPackage("..infraestructura..")
            .because("ReservasController solo conoce IReservaService");
}
