# API de Biblioteca

API REST hecha con Spring Boot para administrar una biblioteca: registra **libros**, **personas** y los **préstamos** de libros a cada persona. Además de la API, incluye una interfaz web sencilla (Thymeleaf) para usar las mismas funciones desde el navegador.

## Descripción de la API

| Recurso | Qué hace |
|---|---|
| **Libros** | Catálogo de libros identificados por su ISBN, con título, autor, editorial, año de publicación y número de ejemplares disponibles. |
| **Personas** | Administradores y usuarios (socios) de la biblioteca, con sus datos de contacto. La contraseña se guarda cifrada con BCrypt y nunca se devuelve en las respuestas. |
| **Préstamos** | Registro de qué libro tiene prestado cada socio, con fecha de préstamo y de devolución. Al prestar se descuenta un ejemplar y al devolver se repone. |

### Reglas de negocio

- Solo se puede prestar a personas con rol `USUARIO` y estado `ACTIVO`.
- Un socio no puede tener dos veces el mismo libro sin devolverlo.
- No se puede prestar un libro sin ejemplares disponibles.
- No se puede marcar como `INACTIVO` a un socio con préstamos sin devolver.
- No se puede eliminar un libro ni una persona que tenga préstamos registrados.
- Un préstamo ya devuelto no se puede modificar ni devolver de nuevo.
- Si se elimina un préstamo que seguía activo, el ejemplar vuelve al inventario.
- El correo de cada persona es único y la contraseña debe tener al menos 6 caracteres.

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.1.1 (Spring Web MVC, Spring Data JPA, Bean Validation)
- Hibernate como implementación de JPA
- MySQL 8
- Thymeleaf y Bootstrap 5 para las vistas
- Spring Security Crypto (BCrypt) para cifrar contraseñas
- Maven

## Estructura del proyecto

```
src/main/java/com/api/libreria
├── controllers    # Controllers REST, controllers de vistas y GlobalExceptionHandler
├── models         # Entidades JPA (LibroModel, PersonaModel, PrestamoModel)
├── repositories   # Interfaces de acceso a datos (Spring Data JPA)
└── services       # Lógica de negocio
src/main/resources
├── application.properties
└── templates      # Vistas Thymeleaf (libros, personas, prestamos)
```

## Instalación y ejecución

### Requisitos previos

- JDK 21
- Maven 3.9 o superior (o el Maven Wrapper del proyecto, si lo incluye)
- MySQL 8 instalado y corriendo en tu equipo

### Pasos

1. **Clona el repositorio** y entra a la carpeta del proyecto:

   ```bash
   git clone <url-del-repositorio>
   cd <carpeta-del-proyecto>
   ```

2. **Crea la base de datos** en MySQL. Las tablas se crean solas al arrancar (`ddl-auto=update`), pero la base de datos debe existir:

   ```sql
   CREATE DATABASE libreria CHARACTER SET utf8mb4;
   ```

3. **Cambia las credenciales de la base de datos** en `src/main/resources/application.properties` por las de tu MySQL local (ver la siguiente sección).

4. **Ejecuta la aplicación:**

   ```bash
   mvn spring-boot:run
   ```

   O, si prefieres generar el `.jar`:

   ```bash
   mvn clean package
   java -jar target/libreria-0.0.1-SNAPSHOT.jar
   ```

5. **Abre la aplicación:**
   - Interfaz web: <http://localhost:8080>
   - API REST: <http://localhost:8080/libros>

## Variables de entorno y configuración

La API no necesita variables de entorno obligatorias: la configuración vive en `src/main/resources/application.properties`. **Cada persona que ejecute el proyecto en su equipo debe cambiar ahí el usuario y la contraseña de la base de datos por los de su propio MySQL**, porque los valores del repositorio son solo de ejemplo.

| Propiedad | Para qué sirve | Valor del proyecto |
|---|---|---|
| `spring.datasource.url` | Dirección de la base de datos | `jdbc:mysql://localhost:3306/libreria?useSSL=false&serverTimezone=UTC` |
| `spring.datasource.username` | Usuario de MySQL | **Cámbialo por el tuyo** |
| `spring.datasource.password` | Contraseña de MySQL | **Cámbiala por la tuya** |
| `server.port` | Puerto de la aplicación | `8080` |
| `spring.jpa.hibernate.ddl-auto` | Crea o actualiza las tablas automáticamente | `update` |
| `spring.jpa.show-sql` | Muestra en consola las consultas SQL | `true` |

Si prefieres no modificar el archivo, Spring Boot también acepta estas propiedades como variables de entorno:

```bash
export SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/libreria?useSSL=false&serverTimezone=UTC"
export SPRING_DATASOURCE_USERNAME="tu_usuario"
export SPRING_DATASOURCE_PASSWORD="tu_contraseña"
export SERVER_PORT=8080
```

## Endpoints

### Libros (`/libros`)

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| GET | `/libros` | Lista todos los libros | 200 |
| POST | `/libros` | Registra un libro | 201, 400, 409 (ISBN repetido) |
| GET | `/libros/{isbn}` | Obtiene un libro por ISBN | 200, 404 |
| PUT | `/libros/{isbn}` | Actualiza un libro | 200, 400, 404 |
| DELETE | `/libros/{isbn}` | Elimina un libro | 204, 404, 409 (tiene préstamos) |

### Personas (`/personas`)

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| GET | `/personas` | Lista todas las personas | 200 |
| POST | `/personas` | Registra una persona | 201, 400, 409 (correo repetido) |
| GET | `/personas/{id}` | Obtiene una persona por id | 200, 404 |
| PUT | `/personas/{id}` | Actualiza una persona (no cambia la contraseña) | 200, 400, 404, 409 |
| DELETE | `/personas/{id}` | Elimina una persona | 204, 404, 409 (tiene préstamos) |

### Préstamos (`/prestamos`)

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| GET | `/prestamos` | Lista todos los préstamos | 200 |
| POST | `/prestamos` | Registra un préstamo | 201, 400, 404, 409 |
| GET | `/prestamos/{id}` | Obtiene un préstamo por id | 200, 404 |
| PUT | `/prestamos/{id}` | Cambia el libro o el socio de un préstamo activo | 200, 400, 404, 409 |
| PUT | `/prestamos/{id}/devolver` | Marca el préstamo como devuelto | 200, 404, 409 |
| DELETE | `/prestamos/{id}` | Elimina un préstamo | 204, 404 |

### Vistas web

| Ruta | Descripción |
|---|---|
| `/` y `/vista/libros` | Catálogo de libros |
| `/vista/personas` | Catálogo de personas |
| `/vista/prestamos` | Control de préstamos |

Las acciones de los formularios (guardar, actualizar, eliminar, devolver) usan rutas `POST` bajo `/vista/...`.

### Códigos de respuesta

| Código | Significado |
|---|---|
| 200 | Operación correcta |
| 201 | Recurso creado (incluye el encabezado `Location`) |
| 204 | Eliminado correctamente, sin contenido |
| 400 | Datos inválidos o faltantes |
| 404 | El recurso no existe |
| 409 | Se rompe una regla de negocio o hay un valor repetido |

Los errores de la API REST tienen siempre este formato:

```json
{
  "fecha": "2026-10-13T10:30:00.123456",
  "estado": 404,
  "error": "Not Found",
  "mensaje": "No existe un libro con ISBN: 9786071234567"
}
```

## Ejemplos de peticiones y respuestas

### Registrar un libro

```bash
curl -X POST http://localhost:8080/libros \
  -H "Content-Type: application/json" \
  -d '{
    "isbn": 9786071234567,
    "titulo": "Cien años de soledad",
    "autor": "Gabriel García Márquez",
    "editorial": "Sudamericana",
    "anoPublicacion": "1967",
    "numEjemplares": 3
  }'
```

Respuesta `201 Created` (encabezado `Location: http://localhost:8080/libros/9786071234567`):

```json
{
  "isbn": 9786071234567,
  "titulo": "Cien años de soledad",
  "autor": "Gabriel García Márquez",
  "editorial": "Sudamericana",
  "anoPublicacion": "1967",
  "numEjemplares": 3
}
```

### Registrar una persona

```bash
curl -X POST http://localhost:8080/personas \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Ana",
    "apellidos": "Ramírez López",
    "correo": "ana.ramirez@correo.com",
    "contrasena": "secreta123",
    "rol": "USUARIO",
    "estado": "ACTIVO",
    "fechaNacimiento": "2000-05-14",
    "telefono": "2291234567",
    "direccion": "Calle Hidalgo 123, Veracruz"
  }'
```

Respuesta `201 Created` (la contraseña no se devuelve):

```json
{
  "id": 1,
  "nombre": "Ana",
  "apellidos": "Ramírez López",
  "correo": "ana.ramirez@correo.com",
  "rol": "USUARIO",
  "estado": "ACTIVO",
  "fechaCreacion": "2026-10-13T10:30:00.123456",
  "fechaNacimiento": "2000-05-14",
  "telefono": "2291234567",
  "direccion": "Calle Hidalgo 123, Veracruz"
}
```

### Registrar un préstamo

Basta con indicar el ISBN del libro y el id del socio:

```bash
curl -X POST http://localhost:8080/prestamos \
  -H "Content-Type: application/json" \
  -d '{
    "libro": { "isbn": 9786071234567 },
    "socio": { "id": 1 }
  }'
```

Respuesta `201 Created` (el libro queda con un ejemplar menos):

```json
{
  "id": 1,
  "libro": {
    "isbn": 9786071234567,
    "titulo": "Cien años de soledad",
    "autor": "Gabriel García Márquez",
    "editorial": "Sudamericana",
    "anoPublicacion": "1967",
    "numEjemplares": 2
  },
  "socio": {
    "id": 1,
    "nombre": "Ana",
    "apellidos": "Ramírez López",
    "correo": "ana.ramirez@correo.com",
    "rol": "USUARIO",
    "estado": "ACTIVO",
    "fechaCreacion": "2026-10-13T10:30:00.123456",
    "fechaNacimiento": "2000-05-14",
    "telefono": "2291234567",
    "direccion": "Calle Hidalgo 123, Veracruz"
  },
  "fechaPrestamo": "2026-10-13T11:00:00.456789",
  "fechaDevolucion": null
}
```

### Devolver un libro

```bash
curl -X PUT http://localhost:8080/prestamos/1/devolver
```

Respuesta `200 OK`: el mismo préstamo, ahora con `fechaDevolucion` y el ejemplar de vuelta en el inventario.

```json
{
  "id": 1,
  "libro": { "isbn": 9786071234567, "titulo": "Cien años de soledad", "numEjemplares": 3, "...": "..." },
  "socio": { "id": 1, "nombre": "Ana", "...": "..." },
  "fechaPrestamo": "2026-10-13T11:00:00.456789",
  "fechaDevolucion": "2026-10-20T09:15:00.987654"
}
```

### Errores comunes

**Datos inválidos** (`POST /libros` con título vacío y ejemplares negativos) → `400 Bad Request`:

```json
{
  "fecha": "2026-10-13T11:05:00.123456",
  "estado": 400,
  "error": "Bad Request",
  "mensaje": "El título es obligatorio; El número de ejemplares no puede ser negativo"
}
```

**Regla de negocio** (prestar un libro sin ejemplares) → `409 Conflict`:

```json
{
  "fecha": "2026-10-13T11:10:00.123456",
  "estado": 409,
  "error": "Conflict",
  "mensaje": "No hay ejemplares disponibles de este libro."
}
```

**Recurso inexistente** (`GET /personas/99`) → `404 Not Found` sin cuerpo.

