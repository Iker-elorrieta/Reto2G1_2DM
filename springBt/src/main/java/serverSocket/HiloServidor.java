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

    // Servicios creados UNA sola vez
    private final LoginService loginService = new LoginService();
    private final PerfilService perfilService = new PerfilService();
    private final AlumnoService alumnoService = new AlumnoService();
    private final HorarioService horarioService = new HorarioService();

    public HiloServidor(Socket conx) {
        this.conx = conx;

        // Gson configurado para evitar problemas con Hibernate
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

                    switch (operacion.toUpperCase()) {

                        // ================= LOGIN =================
                        case "LOGIN": {
                            String usuario = entrada.readUTF();
                            String password = entrada.readUTF();

                            Users user = loginService.login(usuario, password);

                            int codigo;
                            int idUsuario = -1;

                            if (user == null || user.getTipos() == null) {
                                codigo = 0; // Incorrecto
                            } else if (user.getTipos().getId() == 3) {
                                codigo = 1; // Profesor
                                idUsuario = user.getId();
                            } else {
                                codigo = 2; // Otros roles
                            }

                            salida.writeInt(codigo);
                            salida.writeInt(idUsuario);
                            salida.flush();

                            System.out.println("Login procesado: usuario=" + usuario +
                                    " | codigo=" + codigo +
                                    " | idUsuario=" + idUsuario);
                            break;
                        }

                        // ================= LOGOUT =================
                        case "LOGOUT": {
                            System.out.println("Cliente solicitó desconexión: " + conx.getRemoteSocketAddress());
                            activo = false;
                            break;
                        }

                        // ================= GET_PERFIL =================
                        case "GET_PERFIL": {
                            int idUsuario = entrada.readInt();

                            Users user = perfilService.getPerfil(idUsuario);
                            String jsonResponse = (user != null) ? gson.toJson(user) : "";

                            salida.writeUTF(jsonResponse);
                            salida.flush();

                            System.out.println("Perfil enviado para usuario: " + idUsuario);
                            break;
                        }

                        // ================= GET_ALUMNOS =================
                        case "GET_ALUMNOS": {
                            int idProfesor = entrada.readInt();

                            List<Users> alumnos = alumnoService.getAlumnosDelProfesor(idProfesor);
                            String jsonResponse = gson.toJson(alumnos);

                            salida.writeUTF(jsonResponse);
                            salida.flush();

                            System.out.println("Alumnos enviados para profesor: " + idProfesor);
                            break;
                        }

                        // ================= GET_HORARIO =================
                 
                        case "GET_HORARIO": {
                            int idProfesor = entrada.readInt();

                            List<Horarios> horarios = horarioService.obtenerHorarioProfesor(idProfesor);
                            String jsonResponse = gson.toJson(horarios);

                            // EXACTAMENTE IGUAL QUE GET_ALUMNOS
                            salida.writeUTF(jsonResponse);
                            salida.flush();

                            System.out.println("Horario enviado para profesor: " + idProfesor);
                            break;
                        }
                        
                        case "GET_PROFESORES": {
                            List<Users> profesores = alumnoService.getProfesores(); 
                            String jsonResponse = gson.toJson(profesores);

                            salida.writeUTF(jsonResponse);
                            salida.flush();

                            System.out.println("Profesores enviados");
                            break;
                        }


                        

                        // ================= DESCONOCIDO =================
                        default:
                            System.out.println("Comando desconocido: " + operacion);
                            break;
                    }

                } catch (Exception e) {
                    System.out.println("Error procesando comando: " + e.getMessage());
                    e.printStackTrace();
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
