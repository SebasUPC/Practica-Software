# 🧠 De Enunciado a Código: Guía de Traducción para el Examen

Cuando leas las instrucciones de tu examen, cada oración que el profesor escribió se traduce a una línea específica de código en Spring Boot. Esta es la **piedra roseta** para que sepas exactamente qué código escribir según lo que te pidan.

---

## 1. Traducción de Validaciones (Capa DTO)

Las frases de "Validación" en el enunciado van SIEMPRE en tus clases `RequestDTO`. 

| Frase del Examen | Código Java en el DTO (Record) |
| :--- | :--- |
| "El campo X es obligatorio y no debe estar vacío" | `@NotBlank(message = "El campo X es obligatorio")` (Si es String) <br> `@NotNull(message = "...")` (Si es número/objeto) |
| "Debe tener al menos 3 caracteres" | `@Size(min = 3, message = "Debe tener al menos 3 caracteres")` |
| "Debe ser un valor positivo (mayor que 0)" | `@Positive(message = "Debe ser positivo")` <br> Ojo: Usa `@Min(1)` si incluye el cero. |
| "La fecha no puede ser futura" | `@PastOrPresent(message = "No puede ser futura")` |
| "Solo puede tomar los valores INGRESO o GASTO" | `@Pattern(regexp = "^(INGRESO\|GASTO)$", message = "Solo INGRESO o GASTO")` |

> [!IMPORTANT]
> **El Secreto del Controller:** Para que todas estas anotaciones mágicas del DTO funcionen y el cliente reciba un `400 Bad Request`, **SIEMPRE** debes poner la anotación `@Valid` al lado de tu `@RequestBody` en el Controlador.
> `public ResponseEntity<?> registrar(@Valid @RequestBody MiDTO request)`

---

## 2. Traducción de Conversión de Fechas (Del DTO al Service)

**Enunciado:** *"En los DTO, la fecha se manejará como String en formato 'yyyy-MM-dd' o 'HH:mm'. En la capa Service, debe convertirse..."*

* **En el `RequestDTO`:**
  ```java
  @NotBlank(message = "La fecha es obligatoria")
  String fecha // o horaInicio
  ```
* **En el `Service` (Al iniciar tu método):**
  ```java
  // Para Fechas (LocalDate)
  LocalDate fechaConvertida = LocalDate.parse(request.fecha()); 
  
  // Para Horas (LocalTime)
  LocalTime horaInicio = LocalTime.parse(request.horaInicio());
  LocalTime horaFin = LocalTime.parse(request.horaFin());
  ```

---

## 3. Traducción de Reglas de Negocio (Capa Service)

Las "Reglas de Negocio" van SIEMPRE en la clase `Service`. Si la regla se rompe, lanzas un `409 Conflict`. Si falta un dato que buscas, lanzas un `404 Not Found`.

### Caso A: "No permitir registrar X con el mismo código ya existente"
* **En el Repositorio:**
  ```java
  boolean existsByCodigo(String codigo);
  ```
* **En el Servicio (Validación 409):**
  ```java
  if (estacionamientoRepository.existsByCodigo(request.codigo())) {
      throw new ResourceConflictException("El código de estacionamiento ya está registrado.");
  }
  ```

### Caso B: "El X debe existir antes de registrar la reserva"
* **En el Servicio (Búsqueda 404):**
  ```java
  Estacionamiento est = estacionamientoRepository.findById(request.estacionamientoId())
      .orElseThrow(() -> new ResourceNotFoundException("El estacionamiento seleccionado no existe."));
  ```

### Caso C: "Cálculos matemáticos Automáticos" (Ej: Duración o Saldo)
**Enunciado:** *"La duración debe calcularse automáticamente como la diferencia entre horaInicio y horaFin"*
* **En el Servicio:**
  ```java
  LocalTime inicio = LocalTime.parse(request.horaInicio());
  LocalTime fin = LocalTime.parse(request.horaFin());
  
  // Validar regla: "inicio debe ser anterior a fin"
  if (!inicio.isBefore(fin)) {
      throw new BusinessRuleException("La hora de inicio debe ser anterior a la hora de fin."); // 400 o 409 según prefieras
  }
  
  // Calcular duración en horas
  long minutos = java.time.Duration.between(inicio, fin).toMinutes();
  double duracionHoras = minutos / 60.0;
  
  // Lo seteas a mano a la entidad antes de guardar
  reserva.setDuracionHoras(duracionHoras);
  ```

### Caso D: Cruce de Horarios (¡La más difícil!)
**Enunciado:** *"No permitir una nueva reserva si el estacionamiento ya está ocupado en ese rango"*
* **En el Repositorio (Consulta JPQL):**
  ```java
  @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reserva r " +
         "WHERE r.estacionamiento.id = :id AND " +
         "((:horaInicio BETWEEN r.horaInicio AND r.horaFin) OR " +
         "(:horaFin BETWEEN r.horaInicio AND r.horaFin) OR " +
         "(r.horaInicio BETWEEN :horaInicio AND :horaFin))")
  boolean existeCruceHorario(@Param("id") Long id, 
                             @Param("horaInicio") LocalTime horaInicio, 
                             @Param("horaFin") LocalTime horaFin);
  ```
* **En el Servicio:**
  ```java
  if (reservaRepository.existeCruceHorario(est.getId(), inicio, fin)) {
      throw new ResourceConflictException("El estacionamiento ya está reservado en ese rango.");
  }
  ```

---

## 4. Traducción de Reportes y "Si no hay datos..."

**Enunciado:** *"Generar un reporte agregado... Si no hay reservas, retornar un mensaje informativo..."*

1. **DTO del Reporte:** Crea un DTO que NO tenga id, solo los datos pedidos.
  ```java
  public record ReporteIngresoResponseDTO(
      String estacionamiento,
      Integer totalHoras,
      Double tarifaHora,
      Double ingresoTotal
  ) {}
  ```
2. **El Servicio:** 
  ```java
  public List<ReporteIngresoResponseDTO> generarReporte() {
      List<Reserva> reservas = reservaRepository.findAll();
      
      // Regla: "Si no hay reservas..."
      if (reservas.isEmpty()) {
          // Nota: Si el examen pide un mensaje JSON "message: No se encontraron...", 
          // la forma más fácil es lanzar una Excepción personalizada (Ej: EmptyReportException)
          // y que el GlobalExceptionHandler devuelva el 200 OK con ese JSON.
          // O devolver una lista vacía, según qué tan estrictos sean.
      }
      
      // Lógica de sumar horas por estacionamiento...
  }
  ```

> [!TIP]
> Si en el examen te piden que devuelvas un `200 OK` con un JSON `{"message": "No hay datos"}` en vez de la lista, un truco ninja es lanzar una excepción controlada (ej. `throw new NoDataFoundException("No hay datos")`) y en tu `GlobalExceptionHandler` crear un método `@ExceptionHandler(NoDataFoundException.class)` que devuelva un `ResponseEntity.ok()` (200) con el campo `message`. ¡Es la forma más limpia de hacerlo!
