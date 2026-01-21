package modelo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;



public class Users implements java.io.Serializable {

    /**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private Integer id;
    private Tipos tipos;
    private String email;
    private String username;
    private String password;
    private String nombre;
    private String apellidos;
    private String dni;
    private String direccion;
    private String telefono1;
    private String telefono2;
    private String argazkiaUrl;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private Set<Matriculaciones> matriculaciones = new HashSet<>();
    private Set<Reuniones> reunionesForAlumno = new HashSet<>();
    private Set<Reuniones> reunionesForProfesor = new HashSet<>();
    private Set<Horarios> horarios = new HashSet<>();

    public Users() {}

    public Users(Tipos tipos, String email, String username, String password) {
        this.tipos = tipos;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public Users(
        Tipos tipos, String email, String username, String password,
        String nombre, String apellidos, String dni, String direccion,
        String telefono1, String telefono2, String argazkiaUrl,
        Timestamp createdAt, Timestamp updatedAt,
        Set<Matriculaciones> matriculaciones,
        Set<Reuniones> reunionesForAlumno,
        Set<Horarios> horarios,
        Set<Reuniones> reunionesForProfesor
    ) {
        this.tipos = tipos;
        this.email = email;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.direccion = direccion;
        this.telefono1 = telefono1;
        this.telefono2 = telefono2;
        this.argazkiaUrl = argazkiaUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.matriculaciones = matriculaciones;
        this.reunionesForAlumno = reunionesForAlumno;
        this.horarios = horarios;
        this.reunionesForProfesor = reunionesForProfesor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tipos getTipos() {
        return tipos;
    }

    public void setTipos(Tipos tipos) {
        this.tipos = tipos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getArgazkiaUrl() {
        return argazkiaUrl;
    }

    public void setArgazkiaUrl(String argazkiaUrl) {
        this.argazkiaUrl = argazkiaUrl;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Matriculaciones> getMatriculaciones() {
        return matriculaciones;
    }

    public void setMatriculaciones(Set<Matriculaciones> matriculaciones) {
        this.matriculaciones = matriculaciones;
    }

    public Set<Reuniones> getReunionesForAlumno() {
        return reunionesForAlumno;
    }

    public void setReunionesForAlumno(Set<Reuniones> reunionesForAlumno) {
        this.reunionesForAlumno = reunionesForAlumno;
    }

    public Set<Reuniones> getReunionesForProfesor() {
        return reunionesForProfesor;
    }

    public void setReunionesForProfesor(Set<Reuniones> reunionesForProfesor) {
        this.reunionesForProfesor = reunionesForProfesor;
    }

    public Set<Horarios> getHorarios() {
        return horarios;
    }

    public void setHorarios(Set<Horarios> horarios) {
        this.horarios = horarios;
    }
}
