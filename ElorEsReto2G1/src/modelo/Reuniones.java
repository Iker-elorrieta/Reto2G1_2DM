package modelo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controlador.HttpClientHelper;

public class Reuniones implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idReunion;

    // Enviar solo IDs al backend
    private Integer idAlumno;
    private Integer idProfesor;
    private String alumnoNombre;
    private String profesorNombre;

    private String estado;
    private String estadoEus;
    private String idCentro;
    private String titulo;
    private String asunto;
    private String aula;
    private Timestamp fecha;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Reuniones() {}

    public Reuniones(int idAlumno, int idProfesor, String estado, String estadoEus,
                     String idCentro, String titulo, String asunto, String aula,
                     Timestamp fecha) {

        this.idAlumno = idAlumno;
        this.idProfesor = idProfesor;
        this.estado = estado;
        this.estadoEus = estadoEus;
        this.idCentro = idCentro;
        this.titulo = titulo;
        this.asunto = asunto;
        this.aula = aula;
        this.fecha = fecha;
    }

    public String getAlumnoNombre() { return alumnoNombre; }
    public void setAlumnoNombre(String alumnoNombre) { this.alumnoNombre = alumnoNombre; }
    public Integer getIdReunion() { return idReunion; }
    public void setIdReunion(Integer idReunion) { this.idReunion = idReunion; }

    public Integer getIdAlumno() { return idAlumno; }
    public void setIdAlumno(Integer idAlumno) { this.idAlumno = idAlumno; }

    public Integer getIdProfesor() { return idProfesor; }
    public void setIdProfesor(Integer idProfesor) { this.idProfesor = idProfesor; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getEstadoEus() { return estadoEus; }
    public void setEstadoEus(String estadoEus) { this.estadoEus = estadoEus; }

    public String getIdCentro() { return idCentro; }
    public void setIdCentro(String idCentro) { this.idCentro = idCentro; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getAula() { return aula; }
    public void setAula(String aula) { this.aula = aula; }

    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    
    public String getProfesorNombre() { return profesorNombre; }
    public void setProfesorNombre(String profesorNombre) { this.profesorNombre = profesorNombre; }

    public static boolean crearReunionREST(Reuniones reunion) {
        try {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            String json = gson.toJson(reunion);

            String respuesta = HttpClientHelper.postJson("reuniones/crear", json);

            return Boolean.parseBoolean(respuesta);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    
    public static List<Reuniones> obtenerReunionesProfesorREST(int idProfesor) {
        try {
            
        	String json = HttpClientHelper.get("reuniones/profesor/" + idProfesor);


            Gson gson = new Gson();
            Reuniones[] array = gson.fromJson(json, Reuniones[].class);
            return Arrays.asList(array);

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public static void actualizarEstadoREST(int idReunion, String nuevoEstado) {
        try {
            String json = "{\"idReunion\":" + idReunion + ",\"estado\":\"" + nuevoEstado + "\"}";
            HttpClientHelper.postJson("reuniones/actualizar", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
