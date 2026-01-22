package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import modelo.Horarios;
import modelo.Users;
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
    private ControladorServidor controladorServidor;
    private Socket socket;
    private DataInputStream entradaDatos;
    private DataOutputStream salidaDatos;
    private int idUsuario;
    private boolean conectando;

    public void iniciar() {
        vistaInicio = new InicioCliente();
        vistaInicio.getPanelFondo().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evento) {
                conectarYMostrarLogin();
            }
        });
        vistaInicio.setVisible(true);
    }

    private void conectarYMostrarLogin() {
        if (conectando) {
            return;
        }
        conectando = true;
        try {
            socket = new Socket("localhost", 5000);
            controladorServidor = new ControladorServidor(socket);
            entradaDatos = controladorServidor.getEntrada();
            salidaDatos = controladorServidor.getSalida();
            vistaInicio.setVisible(false);
            mostrarVistaLogin();
        } catch (IOException excepcion) {
            vistaInicio.mostrarMensajeError("No se pudo conectar al servidor");
        } finally {
            conectando = false;
        }
    }

    private void mostrarVistaLogin() {
        if (vistaLogin != null) {
            vistaLogin.dispose();
        }
        vistaLogin = new Login();
        vistaLogin.getBtnLogin().addActionListener(evento -> intentarInicioSesion());
        vistaLogin.setVisible(true);
    }

    private void intentarInicioSesion() {
        if (controladorServidor == null || entradaDatos == null || salidaDatos == null) {
            vistaLogin.mostrarMensajeError("No hay conexión con el servidor", "Error");
            return;
        }

        String usuario = vistaLogin.getUsuario();
        String contrasena = vistaLogin.getContrasena();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            vistaLogin.mostrarMensajeAdvertencia("Introduce usuario y contraseña", "Campos requeridos");
            return;
        }

        int codigo = controladorServidor.verificarLogin(usuario, contrasena);

        if (codigo == 1) {
            idUsuario = controladorServidor.getUsuarioId();
            vistaLogin.mostrarMensajeInformativo("Login correcto", "Acceso permitido");
            vistaLogin.dispose();
            vistaLogin = null;
            mostrarVistaMenu();
        } else if (codigo == 2) {
            vistaLogin.mostrarMensajeAdvertencia("Solo los profesores pueden iniciar sesión", "Acceso denegado");
        } else {
            vistaLogin.mostrarMensajeError("Usuario o contraseña incorrectos", "Acceso denegado");
            vistaLogin.clearPassword();
        }
    }

    private void mostrarVistaMenu() {
        if (vistaMenu != null) {
            vistaMenu.dispose();
        }
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
        List<Users> alumnos = Users.obtenerAlumnos(entradaDatos, salidaDatos, idUsuario);
        if (alumnos == null || alumnos.isEmpty()) {
            vistaMenu.mostrarMensajeError("No se encontraron alumnos para crear reuniones.");
            return;
        }

        Users profesor = Users.obtenerPerfil(entradaDatos, salidaDatos, idUsuario);
        if (profesor == null) {
            vistaMenu.mostrarMensajeError("No se pudo obtener el perfil del profesor.");
            return;
        }

        CrearReuiniones vistaCrear = new CrearReuiniones(alumnos);
        vistaCrear.getBtnVolver().addActionListener(e -> {
            vistaCrear.dispose();
            vistaMenu.setVisible(true);
        });

        vistaCrear.getBtnCrear().addActionListener(e -> {
            modelo.Reuniones reunion = vistaCrear.construirReunion(profesor);
            if (reunion == null) return; // Error en fecha

            try {
                salidaDatos.writeUTF("CREAR_REUNION");
                salidaDatos.writeUTF(new com.google.gson.Gson().toJson(reunion));
                salidaDatos.flush();

                boolean confirmacion = entradaDatos.readBoolean();
                if (confirmacion) {
                    JOptionPane.showMessageDialog(vistaCrear, "Reunión creada correctamente.");
                    vistaCrear.dispose();
                    vistaMenu.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(vistaCrear, "Error al crear la reunión.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(vistaCrear, "Error de comunicación con el servidor.");
            }
        });

        vistaMenu.setVisible(false);
        vistaCrear.setVisible(true);
    }


	private void mostrarPerfilPropio() {
        Users usuario = Users.obtenerPerfil(entradaDatos, salidaDatos, idUsuario);
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
        List<Users> listadoAlumnos = Users.obtenerAlumnos(entradaDatos, salidaDatos, idUsuario);
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
        List<Horarios> horarioDocente = Horarios.obtenerHorario(entradaDatos, salidaDatos, idUsuario);
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
        List<Users> listadoProfesores = Users.obtenerProfesores(entradaDatos, salidaDatos);
        if (listadoProfesores == null || listadoProfesores.isEmpty()) {
            vistaMenu.mostrarMensajeError("No se encontraron profesores.");
            return;
        }

        OtrsHorarios vistaOtrosHorarios = new OtrsHorarios(listadoProfesores);
        vistaOtrosHorarios.getBtnVerHorario().addActionListener(evento -> {
            Users profesorSeleccionado = vistaOtrosHorarios.getSelectedProfesor();
            if (profesorSeleccionado != null) {
                List<Horarios> horario = Horarios.obtenerHorario(entradaDatos, salidaDatos, profesorSeleccionado.getId());
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
        StringBuilder nombreCompleto = new StringBuilder();
        if (profesor.getNombre() != null) {
            nombreCompleto.append(profesor.getNombre());
        }
        if (profesor.getApellidos() != null) {
            if (nombreCompleto.length() > 0) {
                nombreCompleto.append(' ');
            }
            nombreCompleto.append(profesor.getApellidos());
        }
        String tituloProfesor = nombreCompleto.length() > 0 ? nombreCompleto.toString() : "profesor";
        Horario vistaHorario = new Horario(horario, "Horario de " + tituloProfesor);
        vistaHorario.getBtnVolver().addActionListener(evento -> {
            vistaHorario.dispose();
            ventanaOrigen.setVisible(true);
        });
        ventanaOrigen.setVisible(false);
        vistaHorario.setVisible(true);
    }

   
    private void mostrarReuniones() {
        VerReuniones vistaReuniones = new VerReuniones();
        vistaReuniones.setVisible(true);
    }

    private void manejarCierreSesion() {
        if (!vistaMenu.confirmarCierreSesion()) {
            return;
        }
        if (controladorServidor != null) {
            controladorServidor.desconectar();
            controladorServidor = null;
        }
        cerrarSocket();
        idUsuario = 0;
        entradaDatos = null;
        salidaDatos = null;

        if (vistaMenu != null) {
            vistaMenu.dispose();
            vistaMenu = null;
        }
        if (vistaLogin != null) {
            vistaLogin.dispose();
            vistaLogin = null;
        }
        if (vistaInicio != null) {
            vistaInicio.setVisible(true);
        }
    }

    private void cerrarSocket() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
            socket = null;
        }
    }
}
