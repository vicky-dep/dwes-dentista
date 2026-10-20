<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
  if (!"POST".equalsIgnoreCase(request.getMethod())) {
    response.sendError(405);
    return;
  }
%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Anular cita</title>
    <link rel="stylesheet" type="text/css" href="estilos.css" />
  </head>
  <body>
    <%
      Class.forName("com.mysql.cj.jdbc.Driver");
      String url = "jdbc:mysql://localhost:3306/dentista?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";

      try (Connection conexion = DriverManager.getConnection(url, "user", "user");
           PreparedStatement ps = conexion.prepareStatement(
                     "DELETE FROM cita WHERE id_cita = ?")) {

        ps.setInt(1, Integer.parseInt(request.getParameter("id_cita")));
        ps.executeUpdate();
    %>
    <h2>Cita anulada.</h2>
    <%
    } catch (Exception e) {
    %>
    <h2 class="error">No se ha podido anular la cita.</h2>
    <p class="detalle-error"><%= e.getMessage() %></p>
    <%
    }
    %>
    <ul>
      <li><a href="listadoCitas.jsp">Ver próximas citas</a></li>
      <li><a href="index.jsp">Volver al inicio</a></li>
    </ul>
  </body>
</html>
