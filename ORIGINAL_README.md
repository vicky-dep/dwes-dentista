# Proyecto Clínica Dental

Gestión de citas de pacientes de una clínica dental, realizado con **JSP + scriptlets + JDBC**
sobre MySQL. Es el equivalente del proyecto `dwes_jsp_jdbc` (gestión de socios de un club de baloncesto).

## Estructura

```
dwes_scriplet_dentista/
├── pom.xml
└── src/main/
    ├── resources/
    │   └── dentista.sql                    <- script de creación de la base de datos
    └── webapp/
        ├── index.jsp                      <- menú principal
        ├── listadoPacientes.jsp           <- listado de pacientes
        ├── listadoCitas.jsp               <- próximas citas
        ├── formularioPaciente.jsp         <- formulario de alta de paciente
        ├── grabaPaciente.jsp              <- inserta el paciente
        ├── formularioCita.jsp             <- formulario de alta de cita
        ├── grabaCita.jsp                  <- inserta la cita
        ├── borraPaciente.jsp              <- da de baja un paciente
        ├── borraCita.jsp                  <- anula una cita
        └── estilos.css
```

> El proyecto **no contiene ningún archivo `.java`**: toda la lógica (conexión a la base
> de datos y consultas) va en scriptlets dentro de las propias páginas JSP, al estilo del
> proyecto `dwes_jsp_jdbc`.

## Base de datos

Base de datos `dentista` con tres tablas:

| Tabla         | Campo clave     | Contenido                                                          |
|---------------|-----------------|--------------------------------------------------------------------|
| `paciente`    | `id_paciente`   | nombre, apellidos, dni, fecha_nacimiento, telefono, email, direccion, alergias |
| `odontologos` | `id_odontologo` | nombre, especialidad                                                  |
| `cita`        | `id_cita`       | fecha, hora, `id_paciente` (FK), `id_odontologo` (FK), motivo        |

Relaciones:

- `cita.id_paciente` → `paciente.id_paciente`, con `ON DELETE CASCADE`: al borrar un
  paciente se borran también sus citas.
- `cita.id_odontologo` → `odontologos.id_odontologo`: la cita guarda la clave del
  odontólogo, no su nombre, para que los profesionales se mantengan en un único sitio.

## Prerrequisitos

1. Importar la base de datos `src/main/resources/dentista.sql`:

   ```bash
   mysql -u user -puser < src/main/resources/dentista.sql
   ```

   Las citas de ejemplo se crean con `DATE_ADD(CURDATE(), INTERVAL n DAY)`, por lo que
   siempre habrá citas próximas y pasadas sea cual sea el día de importación.

2. Disponer de MySQL Server en `localhost:3306` con el usuario `user` / contraseña `user`
   (si son distintos, cambiarlos en el `String url` de cada página JSP).

3. Java 21 y Maven 3.8+.

## Plataforma: Tomcat 9 (Java EE 8)

La aplicación está preparada para **Tomcat 9**, que implementa Java EE 8 y por tanto usa
el *namespace* `javax.*` (no `jakarta.*`):

| Especificación | Versión | Artefacto Maven                 |
|----------------|---------|---------------------------------|
| Servlet        | 4.0     | `javax.servlet:javax.servlet-api:4.0.1` |
| JSP            | 2.3     | incluido en el contenedor        |
| Expression Lang| 3.0     | incluido en el contenedor        |

El contenedor que corresponde es **Tomcat 9.0.x** (no 10.x, que ya es Jakarta EE 9 con
`jakarta.*`).

```bash
mvn clean package
```

Genera `target/scriplet_dentista.war`, que se despliega en Tomcat 9.0.x.
El contexto de la aplicación es `/scriplet_dentista`.

## Notas de implementación

- La página de inicio es `index.jsp` (no `index.html`). Sus cuatro enlaces son:
  - `listadoPacientes.jsp` → listado de pacientes
  - `listadoCitas.jsp` → próximas citas (añadir `?todas=1` para ver el histórico)
  - `formularioCita.jsp` → alta de cita (selector de fecha, hora y paciente)
  - `formularioPaciente.jsp` → alta de paciente
- Toda la lógica (conexiones y consultas) está en scriptlets dentro de las páginas JSP.
  **No hay ningún archivo `.java`** en el proyecto.
- Cada JSP que necesita la base de datos abre su propia conexión con
  `DriverManager.getConnection(url, "user", "user")` y la cierra con *try-with-resources*.
  La cadena de conexión está repetida en cada página; si hay que cambiar el servidor o
  las credenciales, hay que tocarlas en todas.
- Los odontólogos se leen directamente de la tabla `odontologos` con un `SELECT` dentro
  de `formularioCita.jsp`, que es lo que permite construir el selector de la cita.
- Las inserciones y los borrados usan `PreparedStatement` para evitar inyecciones SQL.
- El borrado de pacientes y de citas se hace con `POST`.

# Tarea a realizar
1. Convertir a proyecto servlet, de la manera más "elegante", según hemos visto en clase.
2. Modificar el archivo README.md para que la información se corresponda al proyecto portado a servlet.

En la tarea de Classroom, entregar:
- el proyecto en formato zip (sin carpeta target ni build).
- enlace al repositorio público.
