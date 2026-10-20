<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Listado de citas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos.css">
  </head>
  <body>
    <h1><c:choose><c:when test="${todas}">Todas las citas</c:when><c:otherwise>Próximas citas</c:otherwise></c:choose></h1>
    <c:if test="${param.resultado == 'alta'}"><p>Cita registrada.</p></c:if>
    <c:if test="${param.resultado == 'baja'}"><p>Cita anulada.</p></c:if>
    <table>
      <tr><th>Id cita</th><th>Fecha</th><th>Hora</th><th>Paciente</th><th>Motivo</th><th>Odontólogo</th><th>Acciones</th></tr>
      <c:forEach var="cita" items="${citas}">
        <tr>
          <td><c:out value="${cita.id}"/></td>
          <td><c:out value="${cita.fecha}"/></td>
          <td><c:out value="${cita.hora}"/></td>
          <td><c:out value="${cita.paciente}"/></td>
          <td><c:out value="${cita.motivo}"/></td>
          <td><c:out value="${cita.odontologo}"/></td>
          <td>
            <form method="post" action="${pageContext.request.contextPath}/citas/anular">
              <input type="hidden" name="id_cita" value="${cita.id}">
              <input type="submit" value="anular">
            </form>
          </td>
        </tr>
      </c:forEach>
    </table>
    <p>
      <c:choose>
        <c:when test="${todas}"><a href="${pageContext.request.contextPath}/citas">Ver sólo las próximas citas</a></c:when>
        <c:otherwise><a href="${pageContext.request.contextPath}/citas?todas=1">Ver el histórico completo</a></c:otherwise>
      </c:choose>
      | <a href="${pageContext.request.contextPath}/citas/nueva">Nueva cita</a>
      | <a href="${pageContext.request.contextPath}/">Volver al inicio</a>
    </p>
  </body>
</html>