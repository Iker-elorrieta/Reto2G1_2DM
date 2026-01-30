package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


import modelo.Centro;
import modelo.Horarios;
import modelo.Users;
import modelo.Reuniones;
import vista.Alumnos;
import vista.CrearReuiniones;
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
        if (vistaLogin != null) vistaLogin.dispose();

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
        if (vistaMenu != null) vistaMenu.dispose();

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

        CrearReuiniones vistaCrear = new CrearReuiniones();
        vistaCrear.setEstudiantes(alumnos);
        vistaCrear.setCentros(obtenerCentros());

        vistaCrear.getBtnVolver().addActionListener(e -> {
            vistaCrear.dispose();
            vistaMenu.setVisible(true);
        });

        vistaCrear.getBtnCrear().addActionListener(e -> {
            Reuniones reunion = vistaCrear.construirReunion(profesor);
            if (reunion == null) return;

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

        Horario vistaHorario = new Horario(horario, titulo);
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

        Horario vistaHorario = new Horario(horario, "Horario de " + nombreCompleto);
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
        vistaReuniones.setData(horario, reuniones);

        vistaReuniones.setVisible(true);
    }

    private void manejarCierreSesion() {
        idUsuario = 0;

        try {
            controlador.ControladorServidor.getInstance().desconectar();
        } catch (Exception e) {
            // ignore
        }

        if (vistaMenu != null) vistaMenu.dispose();
        if (vistaLogin != null) vistaLogin.dispose();

        vistaInicio.setVisible(true);
    }

    public List<Centro> obtenerCentros() {
        return Centro.obtenerCentros();
    }
}
