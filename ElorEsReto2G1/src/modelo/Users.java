package modelo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import controlador.HttpClientHelper;

public class Users implements Serializable {
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


    @Override
	public String toString() {
		return  nombre + " " +   apellidos ;
	}

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
        Timestamp createdAt, Timestamp updatedAt

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


        // ============================================================
        // ======================= MÉTODOS REST ========================
        // ============================================================


    public static Users obtenerPerfilREST(int idUsuario) {
        try {
            String json = HttpClientHelper.get("perfil/" + idUsuario);

            if (json != null && !json.isEmpty()) {
                return new Gson().fromJson(json, Users.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<Users> obtenerAlumnosREST(int idProfesor) {
        try {
            String json = HttpClientHelper.get("profesor/" + idProfesor + "/alumnos");

            Users[] array = new Gson().fromJson(json, Users[].class);
            return Arrays.asList(array);

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
        public static List<Users> obtenerProfesoresREST() {
            try {
                String json = HttpClientHelper.get("profesores");

                Users[] array = new Gson().fromJson(json, Users[].class);
                return Arrays.asList(array);

            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

     
    }


