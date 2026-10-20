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
    <title>Baja de paciente</title>
    <link rel="stylesheet" type="text/css" href="estilos.css" />
  </head>
  <body>
    <%
      Class.forName("com.mysql.cj.jdbc.Driver");
      String url = "jdbc:mysql://localhost:3306/dentista?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";

      try (Connection conexion = DriverManager.getConnection(url, "user", "user");
           PreparedStatement ps = conexion.prepareStatement(
                     "DELETE FROM paciente WHERE id_paciente = ?")) {

        ps.setInt(1, Integer.parseInt(request.getParameter("id_paciente")));
        ps.executeUpdate();
    %>
    <h2>Paciente eliminado.</h2>
    <p class="nota">Las citas asociadas a ese paciente se han eliminado también
       (ON DELETE CASCADE).</p>
    <%
    } catch (Exception e) {
    %>
    <h2 class="error">No se ha podido eliminar el paciente.</h2>
    <p class="detalle-error"><%= e.getMessage() %></p>
    <%
    }
    %>
    <ul>
      <li><a href="listadoPacientes.jsp">Ver listado de pacientes</a></li>
      <li><a href="index.jsp">Volver al inicio</a></li>
    </ul>
  </body>
</html>
