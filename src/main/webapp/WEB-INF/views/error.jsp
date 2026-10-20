<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos.css">
  </head>
  <body>
    <h1 class="error">Se ha producido un error</h1>
    <p class="error">${mensajeError}</p>
    <p><a href="${pageContext.request.contextPath}/">Volver al inicio</a></p>
  </body>
</html>