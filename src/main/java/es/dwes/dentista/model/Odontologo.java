package es.dwes.dentista.model;

public class Odontologo {
    private final int id;
    private final String nombre;
    private final String especialidad;

    public Odontologo(int id, String nombre, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEspecialidad() { return especialidad; }
}