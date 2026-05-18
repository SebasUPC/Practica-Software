package com.hampcode.clinica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de inicio de la aplicación Spring Boot.
 * 
 * La anotación @SpringBootApplication es una de las más importantes en Spring Boot.
 * Es una anotación de conveniencia que equivale a declarar tres anotaciones juntas:
 * 1. @EnableAutoConfiguration: Permite que Spring Boot configure automáticamente dependencias basadas en el pom.xml.
 * 2. @ComponentScan: Le dice a Spring que busque componentes, controladores, servicios y repositorios en este paquete y subpaquetes.
 * 3. @SpringBootConfiguration: Identifica a esta clase como proveedora de configuración de Spring.
 */
@SpringBootApplication
public class ClinicaApplication {

    /**
     * El método main estándar de Java. Sirve como punto de entrada de la aplicación.
     * 
     * SpringApplication.run() inicia todo el contexto de Spring, levanta el servidor embebido Tomcat (en el puerto 8080)
     * e inicializa las conexiones de base de datos especificadas en el archivo de configuración.
     */
    public static void main(String[] args) {
        SpringApplication.run(ClinicaApplication.class, args);
    }
}
