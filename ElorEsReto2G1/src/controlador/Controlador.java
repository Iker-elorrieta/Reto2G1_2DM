package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import modelo.Centro;
import modelo.Horarios;
import modelo.Users;
import modelo.Reuniones;
import vista.Alumnos;
import vista.CrearReuniones;
import vista.Horario;
import vista.InicioCliente;
import vista.Login;
import vista.Menu;
import vista.OtrosHorarios;
import vista.Perfil;
import vista.VerReuniones;

public class Controlador {

	private InicioCliente vistaInicio;
	private Login vistaLogin;
	private Menu vistaMenu;
	private int idUsuario;

	// Configuración de rango horario mostrado (inclusive)
	private static final int MIN_HOUR = 8;
	private static final int MAX_HOUR = 20;
	private static final int ROWS = MAX_HOUR - MIN_HOUR + 1;
	// Shift para horas representadas en Horarios (bloques 1..6). Se mapeará añadiendo +7.
	private static final int HORARIO_SHIFT = 7;

	public void iniciar() {
		vistaInicio = new InicioCliente();
		vistaInicio.getPanelFondo().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evento) {
				mostrarVistaLogin();
				vistaInicio.setVisible(false);
			}
		});
		vistaInicio.setVisible(true);
	}

	private void mostrarVistaLogin() {
		if (vistaLogin != null)
			vistaLogin.dispose();

		vistaLogin = new Login();
		vistaLogin.getBtnLogin().addActionListener(evento -> intentarInicioSesion());
		vistaLogin.setVisible(true);
	}

	private void intentarInicioSesion() {
		String usuario = vistaLogin.getUsuario();
		String contrasena = vistaLogin.getContrasena();

		if (usuario.isEmpty() || contrasena.isEmpty()) {
			vistaLogin.mostrarMensajeAdvertencia("Introduce usuario y contraseña", "Campos requeridos");
			return;
		}

		try {
			int codigo = controlador.ControladorServidor.getInstance().verificarLogin(usuario, contrasena);

			if (codigo == 1) { // Profesor
				idUsuario = controlador.ControladorServidor.getInstance().getUsuarioId();
				vistaLogin.dispose();
				mostrarVistaMenu();
			} else if (codigo == 2) {
				vistaLogin.mostrarMensajeError("Usuario sin permisos de profesor", "Acceso denegado");
			} else {
				vistaLogin.mostrarMensajeError("Usuario o contraseña incorrectos", "Acceso denegado");
			}

		} catch (Exception e) {
			vistaLogin.mostrarMensajeError("Error conectando con el servidor", "Error");
		}
	}

	private void mostrarVistaMenu() {
		if (vistaMenu != null)
			vistaMenu.dispose();

		vistaMenu = new Menu();
		vistaMenu.getBtnPerfil().addActionListener(evento -> mostrarPerfilPropio());
		vistaMenu.getBtnAlumnos().addActionListener(evento -> mostrarAlumnos());
		vistaMenu.getBtnConsultarHorario().addActionListener(evento -> mostrarHorarioPropio());
		vistaMenu.getBtnOtrosHorarios().addActionListener(evento -> mostrarOtrosHorarios());
		vistaMenu.getBtnCrearReunion().addActionListener(evento -> crearReuniones());
		vistaMenu.getBtnVerReuniones().addActionListener(evento -> mostrarReuniones());
		vistaMenu.getBtnDesc().addActionListener(evento -> manejarCierreSesion());

		vistaMenu.setVisible(true);
	}

	private void crearReuniones() {
		List<Users> alumnos = Users.obtenerAlumnos(idUsuario);
		if (alumnos == null || alumnos.isEmpty()) {
			vistaMenu.mostrarMensajeError("No se encontraron alumnos para crear reuniones.");
			return;
		}

		Users profesor = Users.obtenerPerfil(idUsuario);
		if (profesor == null) {
			vistaMenu.mostrarMensajeError("No se pudo obtener el perfil del profesor.");
			return;
		}

		CrearReuniones vistaCrear = new CrearReuniones();
		// Poblamos los combos desde el controlador
		vistaCrear.getComboEstudiantes().removeAllItems();
		for (Users u : alumnos) {
			vistaCrear.getComboEstudiantes().addItem(u);
		}
		java.util.List<Centro> centros = obtenerCentros();
		if (centros != null) {
			vistaCrear.getComboUbicacion().removeAllItems();
			for (Centro c : centros) {
				vistaCrear.getComboUbicacion().addItem(c);
			}
			
		}

		vistaCrear.getBtnVolver().addActionListener(e -> {
			vistaCrear.dispose();
			vistaMenu.setVisible(true);
		});

		vistaCrear.getBtnCrear().addActionListener(e -> {
			Reuniones reunion = construirReunionDesdeVista(vistaCrear, profesor);
			if (reunion == null)
				return;

			boolean creada = Reuniones.crearReunion(reunion);

			if (creada) {
				JOptionPane.showMessageDialog(vistaCrear, "Reunión creada correctamente.");
				vistaCrear.dispose();
				vistaMenu.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(vistaCrear, "Error al crear la reunión.");
			}
		});

		vistaMenu.setVisible(false);
		vistaCrear.setVisible(true);
	}

	private void mostrarPerfilPropio() {
		Users usuario = Users.obtenerPerfil(idUsuario);
		if (usuario == null) {
			vistaMenu.mostrarMensajeError("No se pudo obtener el perfil del usuario.");
			return;
		}
		mostrarPerfil(usuario, vistaMenu);
	}

	private void mostrarPerfil(Users usuario, JFrame ventanaAnterior) {
		Perfil vistaPerfil = new Perfil(usuario);
		vistaPerfil.getBtnVolver().addActionListener(evento -> {
			vistaPerfil.dispose();
			ventanaAnterior.setVisible(true);
		});
		ventanaAnterior.setVisible(false);
		vistaPerfil.setVisible(true);
	}

	private DefaultListModel<String> crearModeloListaAlumnos(List<Users> alumnos) {
		DefaultListModel<String> modeloLista = new DefaultListModel<>();
		for (Users alumno : alumnos) {
			modeloLista.addElement(alumno.getId() + " - " + alumno.getNombre() + " " + alumno.getApellidos());
		}
		return modeloLista;
	}

	// Construye y devuelve un DefaultTableModel para mostrar el horario (no
	// editable)
	private DefaultTableModel buildHorarioModel(List<Horarios> listaHorario) {
		String[] dias = { "Hora", "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES" };
		String[][] tablaHorario = new String[ROWS][dias.length];

		// Inicializar y añadir columna de hora
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < dias.length; j++) {
				if (j == 0) {
					int hour = MIN_HOUR + i;
					tablaHorario[i][j] = String.format("%02d:00", hour);
				} else {
					tablaHorario[i][j] = "<html><body style='width: 100px; text-align: center;'>Libre</body></html>";
				}
			}
		}

		if (listaHorario != null)
			for (Horarios bloqueHorario : listaHorario) {
				int horaBloque = bloqueHorario.getHora(); // 1..6
				int horaReal = horaBloque + HORARIO_SHIFT; // mapeo a horas reales: 1->8, 6->13
				int fila = horaReal - MIN_HOUR;
				int columna = diaAColumna(bloqueHorario.getDia());

				if (fila < 0 || fila >= ROWS || columna == -1)
					continue;

				StringBuilder texto = new StringBuilder();

				if (bloqueHorario.getNombreModulo() != null) {
					String nombreModulo = bloqueHorario.getNombreModulo().trim();

					if (nombreModulo.length() > 20) {
						texto.append("<b>").append(generarSiglasExcluyendoY(nombreModulo)).append("</b>");
					} else {
						texto.append("<b>").append(nombreModulo).append("</b>");
					}
				}

				if (bloqueHorario.getAula() != null) {
					if (texto.length() > 0)
						texto.append("<br>");
					texto.append("Aula: ").append(bloqueHorario.getAula());
				}

				if (bloqueHorario.getObservaciones() != null) {
					if (texto.length() > 0)
						texto.append("<br>");
					texto.append(bloqueHorario.getObservaciones());
				}

				String textoCelda = texto.length() > 0 ? texto.toString() : "Libre";

				// Columna real = columna + 1 (porque la primera columna es Hora)
				tablaHorario[fila][columna + 1] = "<html><body style='width: 100px; text-align: center;'>" + textoCelda
						+ "</body></html>";
			}


		DefaultTableModel modeloTabla = new DefaultTableModel(tablaHorario, dias) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Esto bloquea la edición de todas las celdas
			}
		};

		return modeloTabla;
	}

	// Construye un modelo mezclando horario y reuniones (pinta asignaturas +
	// reuniones y conflictos)
	private DefaultTableModel buildHorarioModelWithReuniones(List<Horarios> horario, List<Reuniones> reuniones) {
String[] dias = { "Hora", "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES" };
		String[][] tabla = new String[ROWS][dias.length];

		// Inicializar con contenido vacío y columna hora
		for (int i = 0; i < ROWS; i++)
			for (int j = 0; j < dias.length; j++) {
				if (j == 0) tabla[i][j] = String.format("%02d:00", MIN_HOUR + i);
				else tabla[i][j] = "";
			}

		// 1. Pintar asignaturas (envueltas en HTML)
		if (horario != null)
			for (Horarios h : horario) {
				int horaBloque = h.getHora();
				int horaReal = horaBloque + HORARIO_SHIFT;
				int fila = horaReal - MIN_HOUR;
				int col = diaAColumna(h.getDia());
				if (fila >= 0 && fila < ROWS && col >= 0) {
					String nombreModulo = h.getNombreModulo();
					String contenido;
					if (nombreModulo == null) {
						contenido = "";
					} else if (nombreModulo.length() > 20) {
						contenido = "<b>" + generarSiglasExcluyendoY(nombreModulo) + "</b>";
					} else {
						contenido = "<b>" + nombreModulo + "</b>";
					}
					// colocamos en columna +1 porque la primera columna es Hora
					tabla[fila][col + 1] = "<html><body style='width: 100px; text-align: center;'>" + contenido
							+ "</body></html>";
				}
			}

		// 2. Pintar reuniones (también con HTML, y combinando con asignaturas si hace
		// falta)
		if (reuniones != null)
			for (Reuniones r : reuniones) {
				java.time.LocalDateTime dt = r.getFecha().toLocalDateTime();

				int fila = dt.getHour() - MIN_HOUR; // mapear a filas según rango mostrado
				int col = diaAColumna(convertirDia(dt.getDayOfWeek().name()));

				if (fila < 0 || fila >= ROWS || col < 0)
					continue;

				String estado = r.getEstado();
				String textoReunion = "<br><b>Reunión</b>";

			if (Reuniones.EST_PENDIENTE.equals(estado))
				textoReunion += " <span style='color:orange'>(Pendiente)</span>";

			if (Reuniones.EST_ACEPTADA.equals(estado))
				textoReunion += " <span style='color:green'>(Aceptada)</span>";

			if (Reuniones.EST_DENEGADA.equals(estado))
				textoReunion += " <span style='color:red'>(Denegada)</span>";

				// Si ya hay contenido → añadir reunión (preservando clase si existe)
				int tablaCol = col + 1;
				if (tabla[fila][tablaCol] != null && !tabla[fila][tablaCol].isEmpty()) {
					String existente = tabla[fila][tablaCol];
					if (existente.startsWith("<html>")) {
						existente = existente.replaceFirst("(?i)^<html><body[^>]*>", "");
						existente = existente.replaceFirst("(?i)</body></html>$", "");
					}
					String combinado = existente + "<br>" + textoReunion;
					tabla[fila][tablaCol] = "<html><body style='width: 100px; text-align: center;'>" + combinado
						+ "</body></html>";
				} else {
					String contenido = textoReunion;
					tabla[fila][tablaCol] = "<html><body style='width: 100px; text-align: center;'>" + contenido
							+ "</body></html>";
				}
			}

		// Rellenar celdas vacías con 'Libre' envuelto en HTML
		for (int i = 0; i < ROWS; i++) {
			for (int j = 1; j < dias.length; j++) { // empezamos en 1 para no sobrescribir la columna Hora
				if (tabla[i][j] == null || tabla[i][j].isEmpty()) {
					tabla[i][j] = "<html><body style='width: 100px; text-align: center;'>Libre</body></html>";
				}
			}
		}

		DefaultTableModel modelo = new DefaultTableModel(tabla, dias) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		return modelo;
	}

	private String convertirDia(String dayOfWeek) {
		return switch (dayOfWeek) {
		case "MONDAY" -> "LUNES";
		case "TUESDAY" -> "MARTES";
		case "WEDNESDAY" -> "MIERCOLES";
		case "THURSDAY" -> "JUEVES";
		case "FRIDAY" -> "VIERNES";
		default -> "";
		};
	}

	private String generarSiglasExcluyendoY(String nombre) {
		if (nombre == null || nombre.trim().isEmpty())
			return "";
		String[] palabras = nombre.trim().split("\\s+");
		StringBuilder sb = new StringBuilder();
		for (String p : palabras) {
			String limpio = p.replaceAll("[^\\p{L}0-9]", "");
			if (limpio.equalsIgnoreCase("y") || limpio.isEmpty())
				continue;
			sb.append(Character.toUpperCase(limpio.charAt(0)));
		}
		return sb.toString();
	}

	private DefaultTableModel buildPendientesModel(List<Reuniones> reuniones) {
		String[] columnas = { "ID", "Alumno", "Título", "Fecha", "Estado" };
		java.util.List<Reuniones> pendientes = (reuniones == null) ? java.util.Collections.emptyList()
				: reuniones.stream().filter(r -> Reuniones.EST_PENDIENTE.equals(r.getEstado())).toList();

		Object[][] datos = new Object[pendientes.size()][5];

		for (int i = 0; i < pendientes.size(); i++) {
			Reuniones r = pendientes.get(i);
			datos[i][0] = r.getIdReunion();
			datos[i][1] = r.getIdAlumno();
			datos[i][2] = r.getTitulo();
			datos[i][3] = r.getFecha();
			datos[i][4] = "Pendiente";
		}

		DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		return modelo;
	}

	// Construye una entidad Reuniones a partir de los valores actuales de la vista
	private Reuniones construirReunionDesdeVista(CrearReuniones vistaCrear, Users profesor) {
		if (vistaCrear.getCampoTituloText().isBlank() || vistaCrear.getCampoTemaText().isBlank()
				|| vistaCrear.getCampoAulaText().isBlank()) {
			JOptionPane.showMessageDialog(vistaCrear, "Por favor, completa todos los campos obligatorios.", "Atención",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}

		Reuniones r = new Reuniones();
		r.setTitulo(vistaCrear.getCampoTituloText());
		r.setAsunto(vistaCrear.getCampoTemaText());
		r.setAula(vistaCrear.getCampoAulaText());

		Centro centro = (Centro) vistaCrear.getComboUbicacion().getSelectedItem();
		if (centro == null) {
			JOptionPane.showMessageDialog(vistaCrear, "Selecciona un centro.", "Atención", JOptionPane.WARNING_MESSAGE);
			return null;
		}
		r.setIdCentro(centro.getCCEN());

		r.setEstado(Reuniones.EST_PENDIENTE);
		r.setIdProfesor(profesor.getId());

		Users alumno = (Users) vistaCrear.getComboEstudiantes().getSelectedItem();
		if (alumno == null) {
			JOptionPane.showMessageDialog(vistaCrear, "Selecciona un estudiante.", "Atención",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}
		r.setIdAlumno(alumno.getId());

		try {
			java.util.Date fechaSeleccionada = vistaCrear.getSpinnerFecha();
			LocalDateTime date = fechaSeleccionada.toInstant().atZone(java.time.ZoneId.systemDefault())
					.toLocalDateTime();
			String horaStr = (String) vistaCrear.getComboHora().getSelectedItem();
			int hora = 0;
			try {
				if (horaStr != null && horaStr.contains(":")) {
					hora = Integer.parseInt(horaStr.split(":")[0]);
				} else {
					hora = Integer.parseInt(horaStr);
				}
			} catch (Exception ex) {
				throw new RuntimeException("Hora inválida: " + horaStr, ex);
			}
			date = date.withHour(hora).withMinute(0).withSecond(0);
			r.setFecha(Timestamp.valueOf(date));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(vistaCrear, "Error al procesar la fecha u hora.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return r;
	}

	private int diaAColumna(String dia) {
		return switch (dia.toUpperCase()) {
		case "LUNES" -> 0;
		case "MARTES" -> 1;
		case "MIERCOLES" -> 2;
		case "JUEVES" -> 3;
		case "VIERNES" -> 4;
		default -> -1;
		};
	}

	private void mostrarAlumnos() {
		List<Users> listadoAlumnos = Users.obtenerAlumnos(idUsuario);
		if (listadoAlumnos == null || listadoAlumnos.isEmpty()) {
			vistaMenu.mostrarMensajeError("No se encontraron alumnos.");
			return;
		}
		DefaultListModel<String> modeloLista = crearModeloListaAlumnos(listadoAlumnos);
		Alumnos vistaAlumnos = new Alumnos(listadoAlumnos, modeloLista);
		vistaAlumnos.getBtnVolver().addActionListener(evento -> {
			vistaAlumnos.dispose();
			vistaMenu.setVisible(true);
		});
		vistaAlumnos.getBtnDetalles().addActionListener(evento -> {
			Users alumnoSeleccionado = vistaAlumnos.getSelectedAlumno();
			if (alumnoSeleccionado != null) {
				mostrarPerfil(alumnoSeleccionado, vistaAlumnos);
			}
		});
		vistaMenu.setVisible(false);
		vistaAlumnos.setVisible(true);
	}

	private void mostrarHorarioPropio() {
		List<Horarios> horarioDocente = Horarios.obtenerHorario(idUsuario);
		mostrarHorario(horarioDocente, "Horario del profesor", vistaMenu);
	}

	private void mostrarHorario(List<Horarios> horario, String titulo, JFrame ventanaAnterior) {
		if (horario == null || horario.isEmpty()) {
			JOptionPane.showMessageDialog(ventanaAnterior, "No hay horario disponible.");
			return;
		}

		Horario vistaHorario = new Horario();
		DefaultTableModel modelo = buildHorarioModel(horario);
		vistaHorario.setTableModel(modelo, titulo);
		vistaHorario.getBtnVolver().addActionListener(evento -> {
			vistaHorario.dispose();
			ventanaAnterior.setVisible(true);
		});

		ventanaAnterior.setVisible(false);
		vistaHorario.setVisible(true);
	}

	private void mostrarOtrosHorarios() {
		List<Users> profesores = Users.obtenerProfesores();

		if (profesores == null || profesores.isEmpty()) {
			vistaMenu.mostrarMensajeError("No se encontraron profesores.");
			return;
		}

		OtrosHorarios vistaOtrosHorarios = new OtrosHorarios();
		vistaOtrosHorarios.setProfesores(profesores);

		// Poblamos el combo desde el controlador
		vistaOtrosHorarios.getComboProfesores().removeAllItems();
		for (Users docente : profesores) {
			vistaOtrosHorarios.getComboProfesores().addItem(docente.getNombre() + " " + docente.getApellidos());
		}

		vistaOtrosHorarios.getBtnVerHorario().addActionListener(evento -> {
			Users profesorSeleccionado = vistaOtrosHorarios.getSelectedProfesor();
			if (profesorSeleccionado != null) {
				List<Horarios> horario = Horarios.obtenerHorario(profesorSeleccionado.getId());
				mostrarHorarioDesde(vistaOtrosHorarios, horario, profesorSeleccionado);
			}
		});

		vistaOtrosHorarios.getBtnVolver().addActionListener(evento -> {
			vistaOtrosHorarios.dispose();
			vistaMenu.setVisible(true);
		});

		vistaMenu.setVisible(false);
		vistaOtrosHorarios.setVisible(true);
	}

	private void mostrarHorarioDesde(JFrame ventanaOrigen, List<Horarios> horario, Users profesor) {
		if (horario == null || horario.isEmpty()) {
			JOptionPane.showMessageDialog(ventanaOrigen, "El profesor no tiene horario disponible.");
			return;
		}

		String nombreCompleto = profesor.getNombre() + " " + profesor.getApellidos();

		Horario vistaHorario = new Horario();
		DefaultTableModel modelo = buildHorarioModel(horario);
		vistaHorario.setTableModel(modelo, "Horario de " + nombreCompleto);
		vistaHorario.getBtnVolver().addActionListener(evento -> {
			vistaHorario.dispose();
			ventanaOrigen.setVisible(true);
		});

		ventanaOrigen.setVisible(false);
		vistaHorario.setVisible(true);
	}

	private void mostrarReuniones() {
		VerReuniones vistaReuniones = new VerReuniones();

		List<Horarios> horario = Horarios.obtenerHorario(idUsuario);
		List<Reuniones> reuniones = Reuniones.obtenerReunionesProfesor(idUsuario);

		DefaultTableModel modeloHorario = buildHorarioModelWithReuniones(horario, reuniones);
		DefaultTableModel modeloPendientes = buildPendientesModel(reuniones);

		vistaReuniones.setHorarioModel(modeloHorario);
		vistaReuniones.setPendientesModel(modeloPendientes);

		// Botón volver: cerrar vista y volver al menú
		vistaReuniones.getBtnVolver().addActionListener(evt -> {
			vistaReuniones.dispose();
			vistaMenu.setVisible(true);
		});

		// Ocultar menú mientras la vista de reuniones está abierta
		vistaMenu.setVisible(false);

		// Doble-clic en pendientes -> aceptar/rechazar
		vistaReuniones.getTablaPendientes().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getClickCount() != 2) return;
				int row = vistaReuniones.getTablaPendientes().rowAtPoint(e.getPoint());
				if (row == -1) return;

				Object idObj = vistaReuniones.getTablaPendientes().getValueAt(row, 0);
				if (idObj == null) return;
				int idReu;
				try { idReu = Integer.parseInt(String.valueOf(idObj)); } catch (Exception ex) { return; }

				Reuniones seleccionada = null;
				for (Reuniones r : reuniones) if (r.getIdReunion() != null && r.getIdReunion() == idReu) { seleccionada = r; break; }
				if (seleccionada == null) return;

				String estado = seleccionada.getEstado();
				if (estado == null) estado = "";
if (!Reuniones.EST_PENDIENTE.equals(estado) && !Reuniones.EST_CONFLICTO.equals(estado)) {
					javax.swing.JOptionPane.showMessageDialog(vistaReuniones, "Solo puede modificar reuniones en estado Pendiente o Conflicto.");
					return;
				}

				String[] opciones = {"Aceptar", "Rechazar", "Cancelar"};
				int elegido = javax.swing.JOptionPane.showOptionDialog(vistaReuniones, "Marcar reunión como:", "Actualizar reunión",
						javax.swing.JOptionPane.DEFAULT_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
				if (elegido == 0) seleccionada.setEstado(Reuniones.EST_ACEPTADA);
				else if (elegido == 1) seleccionada.setEstado(Reuniones.EST_DENEGADA);
				else return;

				boolean ok = controlador.ControladorServidor.getInstance().modificarReunion(seleccionada);
				if (ok) {
					javax.swing.JOptionPane.showMessageDialog(vistaReuniones, "Reunión actualizada correctamente.");
					// refrescar datos
					List<Horarios> nuevoHorario = Horarios.obtenerHorario(idUsuario);
					List<Reuniones> nuevasReuniones = Reuniones.obtenerReunionesProfesor(idUsuario);
					vistaReuniones.setHorarioModel(buildHorarioModelWithReuniones(nuevoHorario, nuevasReuniones));
					vistaReuniones.setPendientesModel(buildPendientesModel(nuevasReuniones));
				} else {
					javax.swing.JOptionPane.showMessageDialog(vistaReuniones, "Error al actualizar la reunión.");
				}
			}
		});

		vistaReuniones.setVisible(true);
	}

	private void manejarCierreSesion() {
		idUsuario = 0;

		try {
			controlador.ControladorServidor.getInstance().desconectar();
		} catch (Exception e) {
			// ignore
		}

		if (vistaMenu != null)
			vistaMenu.dispose();
		if (vistaLogin != null)
			vistaLogin.dispose();

		vistaInicio.setVisible(true);
	}

	public List<Centro> obtenerCentros() {
		return Centro.obtenerCentros();
	}
}
