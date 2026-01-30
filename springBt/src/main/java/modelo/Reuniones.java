package modelo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.springBt.HibernateUtil;

public class Reuniones implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idReunion;
	@Transient
	private Integer idAlumno;

	@Transient
	private Integer idProfesor;
	@ManyToOne
	@JoinColumn(name = "id_profesor")
	private Users profesor;

	@ManyToOne
	@JoinColumn(name = "id_alumno")
	private Users alumno;

	private String estado;
	private String estadoEus;
	private String idCentro;
	private String titulo;
	private String asunto;
	private String aula;
	private Timestamp fecha;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	public Reuniones() {
	}

	public Reuniones(Reuniones r) {
		this.idReunion = r.getIdReunion();
		this.idAlumno = (r.getAlumno() != null) ? r.getAlumno().getId() : null;
		this.idProfesor = (r.getProfesor() != null) ? r.getProfesor().getId() : null;
		this.estado = r.getEstado();
		this.estadoEus = r.getEstadoEus();
		this.idCentro = r.getIdCentro();
		this.titulo = r.getTitulo();
		this.asunto = r.getAsunto();
		this.aula = r.getAula();
		this.fecha = r.getFecha();
		this.alumno = new Users(r.getAlumno());
		this.profesor = new Users(r.getProfesor());
		this.createdAt = r.getCreatedAt();
		this.updatedAt = r.getUpdatedAt();
	}

	public Integer getIdReunion() {
		return idReunion;
	}

	public void setIdReunion(Integer idReunion) {
		this.idReunion = idReunion;
	}

	public Users getProfesor() {
		return profesor;
	}

	public void setProfesor(Users profesor) {
		this.profesor = profesor;
	}

	public Users getAlumno() {
		return alumno;
	}

	public void setAlumno(Users alumno) {
		this.alumno = alumno;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadoEus() {
		return estadoEus;
	}

	public void setEstadoEus(String estadoEus) {
		this.estadoEus = estadoEus;
	}

	public String getIdCentro() {
		return idCentro;
	}

	public void setIdCentro(String idCentro) {
		this.idCentro = idCentro;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getAula() {
		return aula;
	}

	public void setAula(String aula) {
		this.aula = aula;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
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

	public Integer getIdAlumno() {
		return idAlumno;
	}

	public void setIdAlumno(Integer idAlumno) {
		this.idAlumno = idAlumno;
	}

	public Integer getIdProfesor() {
		return idProfesor;
	}

	public void setIdProfesor(Integer idProfesor) {
		this.idProfesor = idProfesor;
	}

	// Crear reunión
	public boolean crearReunion() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction tx = session.beginTransaction();

			Users alumno = session.get(Users.class, getIdAlumno());
			Users profesor = session.get(Users.class, getIdProfesor());

			setAlumno(alumno);
			setProfesor(profesor);

			if (alumno == null || profesor == null) {
				throw new RuntimeException("Alumno o profesor no existe");
			}

			setAlumno(alumno);
			setProfesor(profesor);

			// 🔹 Timestamps
			Timestamp ahora = new Timestamp(System.currentTimeMillis());
			setCreatedAt(ahora);
			setUpdatedAt(ahora);

			// 🔹 Persistir
			session.persist(this);
			tx.commit();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// Servicio para obtener reuniones de un profesor
	public static List<Reuniones> obtenerReunionesProfesor(int idProfesor) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Users user = session.get(Users.class, idProfesor);

			List<Reuniones> lista = session
					.createQuery("SELECT r FROM Reuniones r WHERE r.profesor = :Profesor", modelo.Reuniones.class)
					.setParameter("Profesor", user).getResultList();

			lista.replaceAll(r -> new Reuniones(r));
			return lista;
		}
	}

}
