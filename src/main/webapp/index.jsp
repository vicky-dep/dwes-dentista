<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Clínica Dental</title>
    <link rel="stylesheet" type="text/css" href="estilos.css" />
  </head>
  <body>
    <h1>Bienvenido a la aplicación de gestión de citas de la clínica dental</h1>
    <p>Seleccione una opción:</p>
    <ul>
      <li><a href="${pageContext.request.contextPath}/pacientes">Ver listado de pacientes</a></li>
      <li><a href="${pageContext.request.contextPath}/citas">Ver próximas citas</a></li>
      <li><a href="${pageContext.request.contextPath}/citas/nueva">Nueva cita</a></li>
      <li><a href="${pageContext.request.contextPath}/pacientes/nuevo">Nuevo paciente</a></li>
    </ul>
  </body>
</html>
