package es.dwes.dentista.model;

public class Paciente {
    private final int id;
    private final String nombre;
    private final String apellidos;
    private final String dni;
    private final String fechaNacimiento;
    private final String telefono;
    private final String email;
    private final String direccion;
    private final String alergias;
    private final int numCitas;

    public Paciente(int id, String nombre, String apellidos, String dni, String fechaNacimiento,
                    String telefono, String email, String direccion, String alergias, int numCitas) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.alergias = alergias;
        this.numCitas = numCitas;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getDni() { return dni; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getDireccion() { return direccion; }
    public String getAlergias() { return alergias; }
    public int getNumCitas() { return numCitas; }
}