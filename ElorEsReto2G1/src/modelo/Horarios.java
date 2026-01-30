package modelo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;


public class Horarios implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Users users;
	private Modulos modulos;
	private String dia;
	private byte hora;
	private String aula;
	private String observaciones;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private String nombreModulo;

	public Horarios() {
	}

	public Horarios(Users users, Modulos modulos, String dia, byte hora) {
		this.users = users;
		this.modulos = modulos;
		this.dia = dia;
		this.hora = hora;
	}

	public Horarios(Users users, Modulos modulos, String dia, byte hora, String aula, String observaciones,
			Timestamp createdAt, Timestamp updatedAt) {
		this.users = users;
		this.modulos = modulos;
		this.dia = dia;
		this.hora = hora;
		this.aula = aula;
		this.observaciones = observaciones;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Users getUsers() {
		return this.users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Modulos getModulos() {
		return this.modulos;
	}

	public void setModulos(Modulos modulos) {
		this.modulos = modulos;
	}

	public String getDia() {
		return this.dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public byte getHora() {
		return this.hora;
	}

	public void setHora(byte hora) {
		this.hora = hora;
	}

	public String getAula() {
		return this.aula;
	}

	public void setAula(String aula) {
		this.aula = aula;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public static List<Horarios> obtenerHorario(int idProfesor) {
	    try {
	        String json = controlador.ControladorServidor.getInstance().obtenerHorarioJson(idProfesor);

	        if (json == null || json.isEmpty()) return new ArrayList<>();

	        Gson gson = new Gson();
	        Horarios[] array = gson.fromJson(json, Horarios[].class);
	        return Arrays.asList(array);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}




	public String getNombreModulo() {
		return nombreModulo;
	}

	public void setNombreModulo(String nombreModulo) {
		this.nombreModulo = nombreModulo;
	}
}
