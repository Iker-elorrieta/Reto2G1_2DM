package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.google.gson.Gson;

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
import vista.OtrsHorarios;
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
            String json = HttpClientHelper.get("login/" + usuario + "/" + contrasena);

            Gson gson = new Gson();
            Users user = gson.fromJson(json, Users.class);

            if (user != null && user.getTipos().getId() == 3) {
                idUsuario = user.getId();
                vistaLogin.dispose();
                mostrarVistaMenu();
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
        List<Users> alumnos = Users.obtenerAlumnosREST(idUsuario);
        if (alumnos == null || alumnos.isEmpty()) {
            vistaMenu.mostrarMensajeError("No se encontraron alumnos para crear reuniones.");
            return;
        }

        Users profesor = Users.obtenerPerfilREST(idUsuario);
        if (profesor == null) {
            vistaMenu.mostrarMensajeError("No se pudo obtener el perfil del profesor.");
            return;
        }

        CrearReuiniones vistaCrear = new CrearReuiniones(this, alumnos);

        vistaCrear.getBtnVolver().addActionListener(e -> {
            vistaCrear.dispose();
            vistaMenu.setVisible(true);
        });

        vistaCrear.getBtnCrear().addActionListener(e -> {
            Reuniones reunion = vistaCrear.construirReunion(profesor);
            if (reunion == null) return;

            boolean creada = Reuniones.crearReunionREST(reunion);

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
        Users usuario = Users.obtenerPerfilREST(idUsuario);
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

    private void mostrarAlumnos() {
        List<Users> listadoAlumnos = Users.obtenerAlumnosREST(idUsuario);

        if (listadoAlumnos == null || listadoAlumnos.isEmpty()) {
            vistaMenu.mostrarMensajeError("No se encontraron alumnos.");
            return;
        }

        Alumnos vistaAlumnos = new Alumnos(listadoAlumnos);
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
        List<Horarios> horarioDocente = Horarios.obtenerHorarioREST(idUsuario);
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
        List<Users> profesores = Users.obtenerProfesoresREST();

        if (profesores == null || profesores.isEmpty()) {
            vistaMenu.mostrarMensajeError("No se encontraron profesores.");
            return;
        }

        OtrsHorarios vistaOtrosHorarios = new OtrsHorarios(profesores);

        vistaOtrosHorarios.getBtnVerHorario().addActionListener(evento -> {
            Users profesorSeleccionado = vistaOtrosHorarios.getSelectedProfesor();
            if (profesorSeleccionado != null) {
                List<Horarios> horario = Horarios.obtenerHorarioREST(profesorSeleccionado.getId());
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
        VerReuniones vistaReuniones = new VerReuniones(idUsuario);
        vistaReuniones.setVisible(true);
    }

    private void manejarCierreSesion() {
        idUsuario = 0;

        if (vistaMenu != null) vistaMenu.dispose();
        if (vistaLogin != null) vistaLogin.dispose();

        vistaInicio.setVisible(true);
    }

    public List<Centro> obtenerCentros() {
        return Centro.obtenerCentrosREST();
    }
}
