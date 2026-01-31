package serverSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;


import com.example.springBt.PerfilController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import modelo.Centro;
import modelo.Horarios;
import modelo.Reuniones;
import modelo.Users;

public class HiloServidor extends Thread {

    private Socket conx;
    private Gson gson;

    // Servicios creados UNA sola vez
    private final PerfilController perfilService = new PerfilController();

    public HiloServidor(Socket conx) {
        this.conx = conx;

        this.gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
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

                            // Desciframos las credenciales recibidas
                            try {
                                usuario = CryptoUtils.decrypt(usuario);
                                password = CryptoUtils.decrypt(password);
                            } catch (Exception ex) {
                                // si hay fallo, usamos los valores tal cual
                            }

                            Users user = Users.login(usuario, password);

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

                            List<Users> alumnos = perfilService.getAlumnosDelProfesor(idProfesor);
             
                            String jsonResponse = gson.toJson(alumnos);

                            salida.writeUTF(jsonResponse);
                            salida.flush();

                            System.out.println("Alumnos enviados para profesor: " + idProfesor);
                            break;
                        }

	                        // ================= GET_HORARIO =================
	                        case "GET_HORARIO": {
	                            int idProfesor = entrada.readInt();
	
	                            List<Horarios> horarios = Horarios.obtenerHorarioProfesor(idProfesor);
	                      
	                            String jsonResponse = gson.toJson(horarios);
	
	                            salida.writeUTF(jsonResponse);
	                            salida.flush();
	                            System.out.println(jsonResponse);
	
	                            System.out.println("Horario enviado para profesor: " + idProfesor);
	                            break;
	                        }

                        // ================= GET_PROFESORES =================
                        case "GET_PROFESORES": {
                            List<Users> profesores = perfilService.getProfesores();
                    
                            String jsonResponse = gson.toJson(profesores);

                            salida.writeUTF(jsonResponse);
                            salida.flush();
                            System.out.println(jsonResponse);

                            System.out.println("Profesores enviados");
                            break;
                        }

                        // ================= CREAR_REUNION =================
                        case "CREAR_REUNION": {
                            try {
                                String jsonReunion = entrada.readUTF();
                                Reuniones reunion = gson.fromJson(jsonReunion, Reuniones.class);

                                boolean creada = false;
                                try {
                                    creada = reunion.crearReunion();
                                } catch (Exception inner) {
                                    inner.printStackTrace();
                                    creada = false;
                                }

                                salida.writeBoolean(creada);
                                salida.flush();

                                System.out.println("Reunión " + (creada ? "creada" : "fallida") + ": " + reunion.getTitulo());
                            } catch (Exception ex) {
                                // Error procesando petición -> intentar responder false y continuar
                                ex.printStackTrace();
                                try {
                                    salida.writeBoolean(false);
                                    salida.flush();
                                } catch (Exception ioex) {
                                    ioex.printStackTrace();
                                }
                                activo = false;
                            }
                            break;
                        }

                        // ================= MODIFICAR_REUNION =================
                        case "MODIFICAR_REUNION": {
                            try {
                                String jsonReunion = entrada.readUTF();
                                Reuniones reunion = gson.fromJson(jsonReunion, Reuniones.class);

                                boolean modificado = false;
                                try {
                                    modificado = Reuniones.actualizarReunion(reunion);
                                } catch (Exception inner) {
                                    inner.printStackTrace();
                                    modificado = false;
                                }

                                salida.writeBoolean(modificado);
                                salida.flush();

                                System.out.println("Reunión " + (modificado ? "modificada" : "fallida") + ": id=" + reunion.getIdReunion());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                try {
                                    salida.writeBoolean(false);
                                    salida.flush();
                                } catch (Exception ioex) {
                                    ioex.printStackTrace();
                                }
                                activo = false;
                            }
                            break;
                        }

                        // ================= GET_CENTROS =================
                        case "GET_CENTROS": {
                            List<Centro> centros = Centro.obtenerCentros();
                            String jsonResponse = gson.toJson(centros);

                            byte[] data = jsonResponse.getBytes("UTF-8");
                            salida.writeInt(data.length);
                            salida.write(data);
                            salida.flush();

                            System.out.println("Centros enviados");
                            break;
                        }

                        // ================= GET_REUNIONES (pendiente) =================
                        case "GET_REUNIONES": {
                            int idProfesor = entrada.readInt();
                            List<Reuniones> reuniones = Reuniones.obtenerReunionesProfesor(idProfesor);

                            String json = gson.toJson(reuniones);
                            byte[] data = json.getBytes("UTF-8");
                            salida.writeInt(data.length);
                            salida.write(data);
                            salida.flush();
                            System.out.println("Reuniones enviadas para profesor: " + idProfesor);
                            break;
                        }

                        // ================= DESCONOCIDO =================
                        default:
                            System.out.println("Comando desconocido: " + operacion);
                            break;
                    }

                } catch (Exception e) {
                   // System.out.println("Error procesando comando: " + e.getMessage());
                   // e.printStackTrace();
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
