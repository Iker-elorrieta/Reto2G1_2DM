package serverSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;

import com.example.springBt.AlumnoService;
import com.example.springBt.HorarioService;
import com.example.springBt.LoginService;
import com.example.springBt.PerfilService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import modelo.Horarios;
import modelo.Users;

public class HiloServidor extends Thread {

    private Socket conx;
    private Gson gson;

    public HiloServidor(Socket conx) {
        this.conx = conx;
        // Configurar Gson para manejar proxies de Hibernate
        this.gson = new GsonBuilder()
            .setExclusionStrategies(new HibernateProxyExclusionStrategy())
            .create();
    }

    @Override
    public void run() {

        boolean activo = true;

        try (
            DataInputStream entrada = new DataInputStream(conx.getInputStream());
            DataOutputStream salida = new DataOutputStream(conx.getOutputStream());
        ) {

            System.out.println("Cliente conectado: " + conx.getRemoteSocketAddress());

            while (activo && !conx.isClosed()) {

                try {
                    String operacion = entrada.readUTF();
                    System.out.println("Comando recibido: " + operacion);

                    // ================= LOGIN =================
                    if (operacion.equalsIgnoreCase("LOGIN")) {

                        String usuario = entrada.readUTF();
                        String password = entrada.readUTF();

                        LoginService loginService = new LoginService();
                        Users user = loginService.login(usuario, password);

                        int codigo;
                        int idUsuario = -1;

                        if (user == null || user.getTipos() == null) {
                            // Usuario o contraseña incorrectos
                            codigo = 0;

                        } else if (user.getTipos().getId() == 3) {
                            // PROFESOR
                            codigo = 1;
                            idUsuario = user.getId();

                        } else {
                            // Cualquier otro tipo
                            codigo = 2;
                        }

                        salida.writeInt(codigo);
                        salida.writeInt(idUsuario);
                        salida.flush();

                        System.out.println(
                            "Login procesado: usuario=" + usuario +
                            " | codigo=" + codigo +
                            " | idUsuario=" + idUsuario
                        );
                    }

                    // ================= LOGOUT =================
                    else if (operacion.equalsIgnoreCase("LOGOUT")) {
                        System.out.println("Cliente solicitó desconexión: " + conx.getRemoteSocketAddress());
                        activo = false;
                    }

                    // ================= GET_PERFIL =================
                    else if (operacion.equalsIgnoreCase("GET_PERFIL")) {
                        int idUsuario = entrada.readInt();

                        PerfilService perfilService = new PerfilService();
                        Users user = perfilService.getPerfil(idUsuario);

                        String jsonResponse = (user != null) ? gson.toJson(user) : "";

                        salida.writeUTF(jsonResponse);
                        salida.flush();

                        System.out.println("Perfil enviado para usuario: " + idUsuario);
                    }

                    // ================= GET_ALUMNOS =================
                    else if (operacion.equalsIgnoreCase("GET_ALUMNOS")) {
                        int idProfesor = entrada.readInt();

                        AlumnoService alumnoService = new AlumnoService();
                        List<Users> alumnos = alumnoService.getAlumnosDelProfesor(idProfesor);

                        String jsonResponse = gson.toJson(alumnos);

                        salida.writeUTF(jsonResponse);
                        salida.flush();

                        System.out.println("Alumnos enviados para profesor: " + idProfesor);
                    }

                    // ================= GET_HORARIO =================
                    else if (operacion.equalsIgnoreCase("GET_HORARIO")) {
                        int idProfesor = entrada.readInt();

                        HorarioService horarioService = new HorarioService();
                        List<Horarios> horarios = horarioService.obtenerHorarioProfesor(idProfesor);

                        String jsonResponse = gson.toJson(horarios);

                        salida.writeUTF(jsonResponse);
                        salida.flush();

                        System.out.println("Horario enviado para profesor: " + idProfesor);
                    }

                    // ================= DESCONOCIDO =================
                    else {
                        System.out.println("Comando desconocido: " + operacion);
                    }

                } catch (Exception e) {
                    System.out.println("Error procesando comando: " + e.getMessage());
                    e.printStackTrace();
                    System.out.println("Cliente desconectado: " + conx.getRemoteSocketAddress());
                    activo = false;
                }
            }

        } catch (Exception e) {
            System.out.println("Error al manejar cliente: " + e.getMessage());
        } finally {
            try {
                if (conx != null && !conx.isClosed()) {
                    conx.close();
                }
            } catch (Exception ex) {
                System.out.println("Error cerrando socket: " + ex.getMessage());
            }
        }
    }
}
