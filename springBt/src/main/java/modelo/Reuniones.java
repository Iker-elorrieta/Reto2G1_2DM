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

	// Estados permitidos (exactos, en minúsculas)
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
		if (estado == null) {
			this.estado = null;
			return;
		}
		String normalized = estado.trim().toLowerCase();
		if (!isValidEstado(normalized)) {
			throw new IllegalArgumentException("Estado inválido: " + estado + ". Valores permitidos: pendiente, aceptada, denegada, conflicto");
		}
		this.estado = normalized;
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

			// Verificar conflictos con horario y otras reuniones del profesor
			boolean conflicto = false;
			if (getFecha() != null && profesor != null) {
				java.time.LocalDateTime dt = getFecha().toLocalDateTime();
				int hora = dt.getHour(); // hora real (08..20)
				int bloque = hora - 7; // convertir hora real a bloque 1..6 (ej. 8->1)
				String dia = switch (dt.getDayOfWeek().name()) {
					case "MONDAY" -> "LUNES";
					case "TUESDAY" -> "MARTES";
					case "WEDNESDAY" -> "MIERCOLES";
					case "THURSDAY" -> "JUEVES";
					case "FRIDAY" -> "VIERNES";
					default -> "";
				};

				if (!dia.isEmpty()) {
					// Horarios que coinciden (solo si bloque válido 1..6)
					if (bloque >= 1 && bloque <= 6) {
						List<Horarios> hs = session.createQuery(
							"SELECT h FROM Horarios h WHERE h.users = :prof AND h.dia = :dia AND h.hora = :hora",
							Horarios.class).setParameter("prof", profesor).setParameter("dia", dia)
								.setParameter("hora", (byte) bloque).getResultList();
						if (hs != null && !hs.isEmpty()) conflicto = true;
					}

					// Reuniones que coinciden exactamente en la misma fecha/hora
					List<Reuniones> otras = session.createQuery(
						"SELECT r FROM Reuniones r WHERE r.profesor = :prof AND r.fecha = :fecha",
						Reuniones.class).setParameter("prof", profesor).setParameter("fecha", getFecha())
							.getResultList();
					if (otras != null && !otras.isEmpty()) conflicto = true;
				}
			}

// Establecer estado según conflicto (usar valores exactos en minúsculas)
		if (conflicto) setEstado(EST_CONFLICTO);
		else if (getEstado() == null || getEstado().isBlank()) setEstado(EST_PENDIENTE);

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

        public static boolean eliminarReunionPorId(int idReunion) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Reuniones r = session.get(Reuniones.class, idReunion);
            if (r == null) return false;
            session.remove(r);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar reunión
    public static boolean actualizarReunion(Reuniones r) {
        // Validar estado antes de abrir transacción para que, en caso de inválido,
        // pueda lanzarse IllegalArgumentException y ser manejado por el controlador.
        if (r.getEstado() != null) {
            String estadoNorm = r.getEstado().trim().toLowerCase();
            if (!isValidEstado(estadoNorm)) {
                throw new IllegalArgumentException("Estado inválido: " + r.getEstado());
            }
        }

        org.hibernate.Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Reuniones existente = session.get(Reuniones.class, r.getIdReunion());
            if (existente == null) return false;

            if (r.getEstado() != null) existente.setEstado(r.getEstado());
            if (r.getEstadoEus() != null) existente.setEstadoEus(r.getEstadoEus());
            if (r.getIdCentro() != null) existente.setIdCentro(r.getIdCentro());
            if (r.getTitulo() != null) existente.setTitulo(r.getTitulo());
            if (r.getAsunto() != null) existente.setAsunto(r.getAsunto());
            if (r.getAula() != null) existente.setAula(r.getAula());
            if (r.getFecha() != null) existente.setFecha(r.getFecha());

            if (r.getIdAlumno() != null) {
                Users alumno = session.get(Users.class, r.getIdAlumno());
                existente.setAlumno(alumno);
            }
            if (r.getIdProfesor() != null) {
                Users profesor = session.get(Users.class, r.getIdProfesor());
                existente.setProfesor(profesor);
            }

            session.merge(existente);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
}
