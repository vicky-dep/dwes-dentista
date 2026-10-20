# Proyecto Clínica Dental

Aplicación web para gestionar pacientes y citas de una clínica dental. Está desarrollada
con **Servlets Jakarta, JSP/JSTL y JDBC**, organizada según el patrón **MVC** y conectada a
MySQL. Se construye como archivo WAR con Maven.

## Arquitectura MVC

- **Modelo:** `Paciente`, `Cita` y `Odontologo` representan los datos mostrados por la aplicación.
- **Acceso a datos:** `ClinicaDAO` centraliza las consultas e inserciones JDBC. Usa
  `PreparedStatement` para las operaciones que reciben datos del usuario y cierra recursos
  mediante *try-with-resources*.
- **Controladores:** `PacienteServlet` y `CitaServlet` procesan las peticiones, preparan los
  datos para las vistas y ejecutan las operaciones solicitadas.
- **Vistas:** JSP bajo `WEB-INF/views`, renderizadas con EL y JSTL. No contienen scriptlets
  ni pueden solicitarse directamente desde el navegador.

## Estructura

```
src/main/
├── java/es/dwes/dentista/
│   ├── controller/
│   │   ├── CitaServlet.java
│   │   └── PacienteServlet.java
│   ├── dao/
│   │   └── ClinicaDAO.java
│   └── model/
│       ├── Cita.java
│       ├── Odontologo.java
│       └── Paciente.java
├── resources/
│   └── dentista.sql
└── webapp/
    ├── index.jsp
    ├── estilos.css
    └── WEB-INF/views/
        ├── error.jsp
        ├── formularioCita.jsp
        ├── formularioPaciente.jsp
        ├── listadoCitas.jsp
        └── listadoPacientes.jsp
```

Solo `index.jsp` y `estilos.css` se encuentran en la raíz pública de la aplicación. El resto
de las vistas se sirve mediante *forward* desde los servlets y queda protegido en `WEB-INF`.

## Rutas

| Método | Ruta | Función |
|---|---|---|
| `GET` | `/` | Menú principal |
| `GET` | `/pacientes` | Listado de pacientes |
| `GET` | `/pacientes/nuevo` | Formulario de alta de paciente |
| `POST` | `/pacientes/guardar` | Inserta un paciente |
| `POST` | `/pacientes/eliminar` | Elimina un paciente y sus citas asociadas |
| `GET` | `/citas` | Próximas citas |
| `GET` | `/citas?todas=1` | Histórico completo de citas |
| `GET` | `/citas/nueva` | Formulario de alta de cita |
| `POST` | `/citas/guardar` | Inserta una cita |
| `POST` | `/citas/anular` | Anula una cita |

Las operaciones de escritura usan el patrón POST/Redirect/GET. Las acciones de borrado solo
se exponen mediante POST.

## Base de datos

El script `src/main/resources/dentista.sql` crea la base de datos `dentista` y sus tablas:

| Tabla | Clave primaria | Datos principales |
|---|---|---|
| `paciente` | `id_paciente` | Nombre, apellidos, DNI, fecha de nacimiento, teléfono, email, dirección y alergias |
| `odontologos` | `id_odontologo` | Nombre y especialidad |
| `cita` | `id_cita` | Fecha, hora, paciente, odontólogo y motivo |

`cita.id_paciente` referencia a `paciente.id_paciente` con `ON DELETE CASCADE`, por lo que al
eliminar un paciente también se eliminan sus citas. `cita.id_odontologo` referencia a
`odontologos.id_odontologo`.

### Configuración local

Requisitos: Java 21, Maven 3.8 o posterior, MySQL Server y Apache Tomcat 11.

Importa el esquema y los datos de ejemplo:

```bash
mysql -u user -puser < src/main/resources/dentista.sql
```

La aplicación espera MySQL en `localhost:3306`, base de datos `dentista` y credenciales
`user` / `user`. La URL y las credenciales JDBC están centralizadas en `ClinicaDAO`.

## Tomcat y dependencias

El proyecto usa el namespace `jakarta.*` y está preparado para **Apache Tomcat 11**:

| Especificación | Versión |
|---|---|
| Jakarta Servlet | 6.1 |
| Jakarta Server Pages (JSP) | 4.0, proporcionada por Tomcat |
| Jakarta Expression Language (EL) | proporcionada por Tomcat |
| Jakarta Tags (JSTL) | 3.0 |

Maven incluye Jakarta Servlet API con alcance `provided` (la aporta Tomcat), JSTL para las
vistas y MySQL Connector/J dentro del WAR.

## Compilar y desplegar

Desde la raíz del proyecto:

```bash
mvn clean package
```

El archivo `target/scriplet_dentista.war` puede copiarse a la carpeta `webapps` de Tomcat 11.
Con el nombre actual del WAR, el contexto será `/scriplet_dentista`:

```text
http://localhost:8080/scriplet_dentista/
```

En Eclipse/VS Code también se puede agregar el proyecto al servidor Tomcat configurado en el
entorno. Si se cambia el servidor, la base de datos o las credenciales, hay que actualizar la
configuración JDBC de `ClinicaDAO`.
