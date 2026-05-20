# 🚨 Cheat Sheet para el Examen Parcial: Backend Spring Boot

Este documento es tu "salvavidas" para leer rápidamente durante el examen cuando tengas dudas sobre qué anotación usar o cómo armar una relación o un servicio.

---

## 🔗 1. Guía Definitiva de Relaciones JPA

Cuando leas el enunciado del examen, hazte siempre la pregunta: **"¿Un(a) [Entidad A] cuántos [Entidad B] puede tener? ¿Y al revés?"**

### A. `@ManyToOne` (Muchos a Uno) - ¡La más usada!
Es la relación que define la llave foránea (Foreign Key) en la base de datos. **Casi siempre empezarás por aquí.**
* **Cómo identificarla:** "Muchos Pacientes tienen Un Seguro", "Muchas Citas pertenecen a Un Médico".
* **Regla de oro:** Se coloca en la entidad "hija" o "fuerte" (la que tiene el campo `_id` en la tabla SQL).
* **Plantilla:**
  ```java
  @ManyToOne(fetch = FetchType.LAZY) // SIEMPRE LAZY en el examen
  @JoinColumn(name = "medico_id", nullable = false) // Nombre de la columna en BD
  private Medico medico;
  ```

### B. `@OneToMany` (Uno a Muchos) - Opcional, úsala con cuidado
Es la inversa de `@ManyToOne`. A menudo **no la necesitas** a menos que el examen te pida expresamente "obtener el Médico y su lista de Citas".
* **Cómo identificarla:** "Un Médico tiene Muchas Citas".
* **Regla de oro:** Va en la entidad "padre". Nunca lleva `@JoinColumn`. SIEMPRE lleva `mappedBy` apuntando al nombre de la variable en la clase hija.
* **Plantilla:**
  ```java
  @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
  private List<Cita> citas;
  ```

### C. `@ManyToMany` (Muchos a Muchos)
* **Cómo identificarla:** "Un Estudiante tiene Muchos Cursos, y un Curso tiene Muchos Estudiantes".
* **Regla de oro:** Requiere una tabla intermedia. En el examen, si la tabla intermedia tiene columnas extra (ej. "Nota del curso"), **¡NO USES @ManyToMany!** Rompe esa tabla en dos `@ManyToOne` (Estudiante -> Inscripcion <- Curso). Si no tiene columnas extra, usa esto:
* **Plantilla:**
  ```java
  @ManyToMany
  @JoinTable(
      name = "estudiante_curso",
      joinColumns = @JoinColumn(name = "estudiante_id"),
      inverseJoinColumns = @JoinColumn(name = "curso_id")
  )
  private List<Curso> cursos;
  ```

### D. `@OneToOne` (Uno a Uno)
* **Cómo identificarla:** "Un Usuario tiene Un Perfil".
* **Regla de oro:** El dueño de la relación (el que lleva la llave foránea) usa `@JoinColumn`. El otro usa `mappedBy`.

---

## ⚙️ 2. Guía Definitiva para los Servicios (Lógica de Negocio)

El servicio (`@Service`) es el cerebro de tu aplicación. Aquí es donde los profesores se fijan si realmente sabes programar o si solo copiaste y pegaste.

### La Estructura Perfecta de un Método de Registro (POST)
Divide siempre tu método en 4 pasos mentales:

```java
@Transactional // 🔴 SIEMPRE para POST, PUT, DELETE (escritura)
public CitaResponse registrar(CitaRequest request) {
    
    // PASO 1: Validar Reglas de Negocio Negativas (Lanzar Excepciones 409 Conflict)
    if (citaRepository.existeCita(request.fecha())) {
        throw new ResourceConflictException("Ya existe una cita en esa fecha");
    }

    // PASO 2: Extraer y Validar Dependencias (Lanzar Excepciones 404 Not Found)
    // Usar dependencias inyectadas (ej. medicoRepository)
    Medico medico = medicoRepository.findById(request.medicoId())
        .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado"));

    // PASO 3: Construir / Mapear la Entidad y Procesar Lógica Positiva
    Cita cita = citaMapper.toEntity(request); // Usando MapStruct
    cita.setMedico(medico);
    cita.setEstado("PENDIENTE"); // Valores por defecto

    // PASO 4: Guardar y Mapear a Respuesta
    return citaMapper.toResponse(citaRepository.save(cita));
}
```

### `@Transactional` vs `@Transactional(readOnly = true)`
* Usa `@Transactional(readOnly = true)` **SOLO para los métodos `GET`** (búsquedas). Hace que Hibernate sea súper rápido porque no gasta memoria revisando si modificaste objetos.
* Usa `@Transactional` (normal) **para `POST`, `PUT`, `DELETE`**. Si falla una validación a la mitad, revierte (Rollback) toda la base de datos automáticamente.

---

## 🎯 3. Glosario Rápido de Anotaciones de Controlador y Validación

Si te olvidas de qué hace cada cosa en la capa web, revisa esta tabla:

| Anotación | ¿Dónde va? | ¿Qué hace? |
| :--- | :--- | :--- |
| `@RestController` | Clase (Controlador) | Combina `@Controller` y `@ResponseBody`. Hace que tu clase devuelva JSON. |
| `@RequestMapping("/api/v1/...")`| Clase (Controlador) | Define la ruta base para todos los métodos de esa clase. |
| `@GetMapping` / `@PostMapping` | Método (Controlador) | Define el verbo HTTP (Lectura / Creación). |
| `@PathVariable` | Parámetro de Método | Extrae valores de la URL. Ej: `/api/v1/citas/{id}`. |
| `@RequestBody` | Parámetro de Método | Transforma el JSON que envía Postman a un objeto Java (DTO). |
| `@Valid` | Al lado de `@RequestBody` | ¡CRÍTICO! Le dice a Spring que ejecute las reglas `@NotBlank`, `@NotNull`, etc., de tu DTO. Si lo olvidas, el Bean Validation no funciona. |
| `@NotBlank` | Atributos String (DTO) | Asegura que el texto no sea nulo ni esté vacío (""). |
| `@NotNull` | Atributos Objeto/Número (DTO)| Asegura que no envíen valores nulos (`null`). |
| `@Future` / `@Past` | Fechas (DTO) | Asegura que la fecha esté en el futuro o pasado. |
| `@Positive` / `@Min(0)` | Números (DTO) | Asegura números positivos o mayores a cero. |
| `@RestControllerAdvice` | Clase (ExceptionHandler) | Intercepta excepciones de todos los controladores para devolver JSONs de error bonitos. |

---

## 💡 Tip Final para el Examen
Si en el examen te bloqueas con **MapStruct** y no compila el proyecto:
¡No pierdas tiempo intentando arreglar el `pom.xml` durante 1 hora! Borra la interface de MapStruct, crea un `public static Response toResponse(Entity)` manual rápido y sigue adelante. Un código feo que funciona vale más puntos que un código perfecto que no compila.
