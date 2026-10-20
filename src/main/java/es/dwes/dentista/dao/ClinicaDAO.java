package es.dwes.dentista.dao;

import es.dwes.dentista.model.Cita;
import es.dwes.dentista.model.Odontologo;
import es.dwes.dentista.model.Paciente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ClinicaDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/dentista?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    private static final String USER = "user";
    private static final String PASSWORD = "user";

    private Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", USER);
        properties.setProperty("password", PASSWORD);
        return new com.mysql.cj.jdbc.Driver().connect(URL, properties);
    }

    public List<Paciente> listarPacientes() throws SQLException {
        String sql = "SELECT p.id_paciente, p.nombre, p.apellidos, p.dni, p.fecha_nacimiento, "
                + "p.telefono, p.email, p.direccion, p.alergias, "
                + "(SELECT COUNT(*) FROM cita c WHERE c.id_paciente = p.id_paciente) AS num_citas "
                + "FROM paciente p ORDER BY p.apellidos, p.nombre";
        List<Paciente> pacientes = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {
            while (result.next()) {
                pacientes.add(new Paciente(result.getInt("id_paciente"), result.getString("nombre"),
                        result.getString("apellidos"), result.getString("dni"), result.getString("fecha_nacimiento"),
                        result.getString("telefono"), result.getString("email"), result.getString("direccion"),
                        result.getString("alergias"), result.getInt("num_citas")));
            }
        }
        return pacientes;
    }

    public List<Cita> listarCitas(boolean todas) throws SQLException {
        String sql = "SELECT c.id_cita, c.fecha, c.hora, c.motivo, p.nombre, p.apellidos, o.nombre AS odontologo "
                + "FROM cita c JOIN paciente p ON p.id_paciente = c.id_paciente "
                + "JOIN odontologos o ON o.id_odontologo = c.id_odontologo "
                + (todas ? "" : "WHERE c.fecha >= CURDATE() ") + "ORDER BY c.fecha, c.hora";
        List<Cita> citas = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {
            while (result.next()) {
                String hora = result.getString("hora");
                citas.add(new Cita(result.getInt("id_cita"), result.getString("fecha"),
                        hora == null || hora.length() < 5 ? hora : hora.substring(0, 5),
                        result.getString("apellidos") + ", " + result.getString("nombre"),
                        result.getString("motivo"), result.getString("odontologo")));
            }
        }
        return citas;
    }

    public List<Paciente> listarPacientesParaSelector() throws SQLException {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT id_paciente, nombre, apellidos FROM paciente ORDER BY apellidos, nombre";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {
            while (result.next()) {
                pacientes.add(new Paciente(result.getInt("id_paciente"), result.getString("nombre"),
                        result.getString("apellidos"), null, null, null, null, null, null, 0));
            }
        }
        return pacientes;
    }

    public List<Odontologo> listarOdontologos() throws SQLException {
        List<Odontologo> odontologos = new ArrayList<>();
        String sql = "SELECT id_odontologo, nombre, especialidad FROM odontologos ORDER BY nombre";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {
            while (result.next()) {
                odontologos.add(new Odontologo(result.getInt("id_odontologo"), result.getString("nombre"),
                        result.getString("especialidad")));
            }
        }
        return odontologos;
    }

    public void crearPaciente(String nombre, String apellidos, String dni, String nacimiento,
                              String telefono, String email, String direccion, String alergias) throws SQLException {
        String sql = "INSERT INTO paciente (nombre, apellidos, dni, fecha_nacimiento, telefono, email, direccion, alergias) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombre);
            statement.setString(2, apellidos);
            statement.setString(3, dni);
            if (nacimiento == null || nacimiento.isBlank()) {
                statement.setNull(4, Types.DATE);
            } else {
                statement.setDate(4, java.sql.Date.valueOf(LocalDate.parse(nacimiento)));
            }
            statement.setString(5, telefono);
            statement.setString(6, email);
            statement.setString(7, direccion);
            statement.setString(8, alergias);
            statement.executeUpdate();
        }
    }

    public void crearCita(String fecha, String hora, int idPaciente, int idOdontologo, String motivo) throws SQLException {
        String sql = "INSERT INTO cita (fecha, hora, id_paciente, id_odontologo, motivo) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, java.sql.Date.valueOf(LocalDate.parse(fecha)));
            statement.setTime(2, java.sql.Time.valueOf(LocalTime.parse(hora)));
            statement.setInt(3, idPaciente);
            statement.setInt(4, idOdontologo);
            statement.setString(5, motivo);
            statement.executeUpdate();
        }
    }

    public void borrarPaciente(int id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM paciente WHERE id_paciente = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public void borrarCita(int id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM cita WHERE id_cita = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}