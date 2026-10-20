package es.dwes.dentista.controller;

import es.dwes.dentista.dao.ClinicaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/pacientes/*")
public class PacienteServlet extends HttpServlet {
    private final ClinicaDAO dao = new ClinicaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        try {
            if (path == null || "/".equals(path)) {
                request.setAttribute("pacientes", dao.listarPacientes());
                request.getRequestDispatcher("/WEB-INF/views/listadoPacientes.jsp").forward(request, response);
            } else if ("/nuevo".equals(path)) {
                request.getRequestDispatcher("/WEB-INF/views/formularioPaciente.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception exception) {
            mostrarError(request, response, exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getPathInfo();
        try {
            if ("/guardar".equals(path)) {
                dao.crearPaciente(request.getParameter("nombre"), request.getParameter("apellidos"),
                        request.getParameter("dni"), request.getParameter("fecha_nacimiento"),
                        request.getParameter("telefono"), request.getParameter("email"),
                        request.getParameter("direccion"), request.getParameter("alergias"));
                response.sendRedirect(request.getContextPath() + "/pacientes?resultado=alta");
            } else if ("/eliminar".equals(path)) {
                dao.borrarPaciente(Integer.parseInt(request.getParameter("id_paciente")));
                response.sendRedirect(request.getContextPath() + "/pacientes?resultado=baja");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception exception) {
            mostrarError(request, response, exception);
        }
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response, Exception exception)
            throws ServletException, IOException {
        getServletContext().log("Error en la gestión de pacientes", exception);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        request.setAttribute("mensajeError", "No se ha podido completar la operación de pacientes.");
        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
}