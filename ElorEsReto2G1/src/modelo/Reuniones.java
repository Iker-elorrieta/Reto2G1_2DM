package modelo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import controlador.ControladorServidor;

public class Reuniones implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idReunion;

    // Enviar solo IDs al backend
    private Integer idAlumno;
    private Integer idProfesor;

    private String estado;
    private String estadoEus;
    private String idCentro;
    private String titulo;
    private String asunto;
    private String aula;
    private Timestamp fecha;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Estados permitidos
    public static final String EST_PENDIENTE = "pendiente";
    public static final String EST_ACEPTADA = "aceptada";
    public static final String EST_DENEGADA = "denegada";
    public static final String EST_CONFLICTO = "conflicto";

    public static boolean isValidEstado(String e) {
        if (e == null) return false;
        switch (e) {
            case EST_PENDIENTE:
            case EST_ACEPTADA:
            case EST_DENEGADA:
            case EST_CONFLICTO:
                return true;
            default:
                return false;
        }
    }

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

    public Integer getIdReunion() { return idReunion; }
    public void setIdReunion(Integer idReunion) { this.idReunion = idReunion; }

    public Integer getIdAlumno() { return idAlumno; }
    public void setIdAlumno(Integer idAlumno) { this.idAlumno = idAlumno; }

    public Integer getIdProfesor() { return idProfesor; }
    public void setIdProfesor(Integer idProfesor) { this.idProfesor = idProfesor; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) {
        if (estado == null) { this.estado = null; return; }
        String normalized = estado.trim().toLowerCase();
        if (!isValidEstado(normalized)) {
            throw new IllegalArgumentException("Estado inválido: " + estado + ". Valores permitidos: pendiente, aceptada, denegada, conflicto");
        }
        this.estado = normalized;
    }

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

    public static boolean crearReunion(Reuniones reunion) {
        try {
            return ControladorServidor.getInstance().crearReunion(reunion);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    
    public static List<Reuniones> obtenerReunionesProfesor(int idProfesor) {
        try {
            String json = ControladorServidor.getInstance().obtenerReunionesProfesorJson(idProfesor);

            if (json == null || json.isEmpty()) return new ArrayList<>();

            com.google.gson.Gson gson = new com.google.gson.GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();
            Reuniones[] array = gson.fromJson(json, Reuniones[].class);
            if (array == null) return new ArrayList<>();
            return Arrays.asList(array);

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}
