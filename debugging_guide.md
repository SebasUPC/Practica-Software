# 🐞 Guía Rápida de Debugging para el Examen

Cuando las cosas no funcionen durante el examen y Postman te devuelva un error 500 feo, que no cunda el pánico. Aquí tienes las 4 estrategias principales para encontrar el error en tiempo récord.

---

## 1. El Modo "Lupa": Breakpoints en IntelliJ / Eclipse

La mejor forma de encontrar por qué una lógica de negocio falla o por qué un DTO llega nulo.

1. **Pon el punto rojo:** Ve a tu clase `Service` (ej. `CitaService.java`) y haz clic al lado del número de línea en el margen izquierdo. Aparecerá un círculo rojo (Breakpoint).
2. **Inicia en modo "Bicho":** En vez de darle al botón verde de "Play" (Run), dale clic al botón con forma de insecto verde (Debug).
3. **Lanza la petición en Postman.** Tu programa se "congelará" mágicamente justo en la línea roja.
4. **Inspecciona las variables:** Pasa el cursor por encima de tus variables (ej. `request`, `paciente`) para ver si están llegando nulas, o si la fecha se parseó mal.
5. **Avanza paso a paso:** Usa las teclas (normalmente F8 en IntelliJ) para avanzar línea por línea y ver exactamente dónde explota.

---

## 2. El Método Clásico: Los "Print" y Logs

A veces solo quieres saber rápidamente "por dónde está pasando el código" sin frenarlo.

*   **El clásico de emergencia:** `System.out.println("LLEGÓ AQUÍ: " + request.nombre());`
*   **La forma Pro (`@Slf4j`):** 
    1. Ponle la anotación `@Slf4j` (de Lombok) arriba del nombre de tu clase (ej. en tu Service o Controller).
    2. Escribe en tu código: `log.info("Buscando paciente con ID: {}", request.pacienteId());`
    3. Revísalo en la consola negra de tu IDE o en los logs de Docker (`docker compose logs -f api`).

---

## 3. ¿Por qué mi JPQL o SQL está fallando?

Si una consulta a la base de datos (como el cruce de horarios de las reservas) no devuelve lo que debería, necesitas ver el SQL real que Hibernate está generando.

Abre tu archivo `src/main/resources/application.properties` (o tu `compose.yml` en la sección de environment) y asegúrate de tener activado:

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Con esto, cada vez que hagas una consulta desde Postman, verás en la consola de tu IDE la sentencia SQL exacta (con los `SELECT` y `WHERE`) que Spring Boot intentó ejecutar. Si copias ese SQL y lo pegas en `pgAdmin`, puedes probar la consulta manualmente.

---

## 4. El Rastreo de Excepciones (El "Stacktrace")

Cuando tu pantalla se llene de letras rojas, **NUNCA mires todo el texto gigante**. 

1. **Haz scroll hasta el final o busca el primer "Caused by:".** 
2. Lee esa línea exacta. Suele decir cosas muy claras como:
   * `Caused by: org.postgresql.util.PSQLException: ERROR: null value in column "nombre" violates not-null constraint` (Olvidaste enviar el nombre).
   * `Caused by: java.time.format.DateTimeParseException: Text '10-05-2025' could not be parsed at index 2` (El formato de fecha que pusiste en el DTO o en Postman está mal, recuerda que debe ser YYYY-MM-DD).

Si sigues estos 4 pasos en orden de prioridad, no habrá error que te quite más de 5 minutos en el parcial. ¡Mucho éxito!
