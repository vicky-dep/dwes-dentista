package es.dwes.dentista.model;

public class Cita {
    private final int id;
    private final String fecha;
    private final String hora;
    private final String paciente;
    private final String motivo;
    private final String odontologo;

    public Cita(int id, String fecha, String hora, String paciente, String motivo, String odontologo) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.paciente = paciente;
        this.motivo = motivo;
        this.odontologo = odontologo;
    }

    public int getId() { return id; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getPaciente() { return paciente; }
    public String getMotivo() { return motivo; }
    public String getOdontologo() { return odontologo; }
}