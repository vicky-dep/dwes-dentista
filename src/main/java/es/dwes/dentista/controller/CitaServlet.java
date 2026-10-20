package es.dwes.dentista.controller;

import es.dwes.dentista.dao.ClinicaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/citas/*")
public class CitaServlet extends HttpServlet {
    private final ClinicaDAO dao = new ClinicaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        try {
            if (path == null || "/".equals(path)) {
                boolean todas = "1".equals(request.getParameter("todas"));
                request.setAttribute("todas", todas);
                request.setAttribute("citas", dao.listarCitas(todas));
                request.getRequestDispatcher("/WEB-INF/views/listadoCitas.jsp").forward(request, response);
            } else if ("/nueva".equals(path)) {
                request.setAttribute("pacientes", dao.listarPacientesParaSelector());
                request.setAttribute("odontologos", dao.listarOdontologos());
                request.setAttribute("pacienteSeleccionado", request.getParameter("paciente"));
                request.getRequestDispatcher("/WEB-INF/views/formularioCita.jsp").forward(request, response);
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
                dao.crearCita(request.getParameter("fecha"), request.getParameter("hora"),
                        Integer.parseInt(request.getParameter("id_paciente")),
                        Integer.parseInt(request.getParameter("id_odontologo")), request.getParameter("motivo"));
                response.sendRedirect(request.getContextPath() + "/citas?resultado=alta");
            } else if ("/anular".equals(path)) {
                dao.borrarCita(Integer.parseInt(request.getParameter("id_cita")));
                response.sendRedirect(request.getContextPath() + "/citas?resultado=baja");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception exception) {
            mostrarError(request, response, exception);
        }
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response, Exception exception)
            throws ServletException, IOException {
        getServletContext().log("Error en la gestión de citas", exception);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        request.setAttribute("mensajeError", "No se ha podido completar la operación de citas.");
        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
}