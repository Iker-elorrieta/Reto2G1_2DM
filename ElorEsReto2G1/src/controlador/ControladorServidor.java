package controlador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import modelo.Reuniones;

public class ControladorServidor {

	private static ControladorServidor instance;

	private Socket socket;
	private DataInputStream entrada;
	private DataOutputStream salida;

	private int usuarioId;

	private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

	private ControladorServidor() {
	}

	public static synchronized ControladorServidor getInstance() {
		if (instance == null) {
			instance = new ControladorServidor();
		}
		return instance;
	}

	public synchronized void connect(String host, int port) throws Exception {
		if (socket != null && !socket.isClosed())
			return; // ya conectados
		socket = new Socket(host, port);
		entrada = new DataInputStream(socket.getInputStream());
		salida = new DataOutputStream(socket.getOutputStream());
	}

	public int getUsuarioId() {
		return usuarioId;
	}

	public synchronized int verificarLogin(String usuario, String password) {
		try {
			if (socket == null || socket.isClosed())
				connect("localhost", 5000);

			salida.writeUTF("LOGIN");
			// Ciframos las credenciales antes de enviarlas
			salida.writeUTF(CryptoUtils.encrypt(usuario));
			salida.writeUTF(CryptoUtils.encrypt(password));
			salida.flush();

			int codigo = entrada.readInt();
			int id = entrada.readInt();

			this.usuarioId = id;

			return codigo;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private void ensureConnected() throws Exception {
		if (socket == null || socket.isClosed())
			connect("localhost", 5000);
	}

	public synchronized String obtenerPerfilJson(int idUsuario) {
		try {
			ensureConnected();
			salida.writeUTF("GET_PERFIL");
			salida.writeInt(idUsuario);
			salida.flush();

			String json = entrada.readUTF();
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public synchronized String obtenerAlumnosJson(int idProfesor) {
		try {
			ensureConnected();
			salida.writeUTF("GET_ALUMNOS");
			salida.writeInt(idProfesor);
			salida.flush();

			String json = entrada.readUTF();
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public synchronized String obtenerProfesoresJson() {
		try {
			ensureConnected();
			salida.writeUTF("GET_PROFESORES");
			salida.flush();

			String json = entrada.readUTF();
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public synchronized String obtenerHorarioJson(int idProfesor) {
		try {
			ensureConnected();
			salida.writeUTF("GET_HORARIO");
			salida.writeInt(idProfesor);
			salida.flush();

			String json = entrada.readUTF();
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public synchronized String obtenerCentrosJson() {
		try {
			ensureConnected();
			salida.writeUTF("GET_CENTROS");
			salida.flush();

			int len = entrada.readInt();
			byte[] data = new byte[len];
			entrada.readFully(data);
			return new String(data, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public synchronized String obtenerReunionesProfesorJson(int idProfesor) {
		try {
			ensureConnected();
			salida.writeUTF("GET_REUNIONES");
			salida.writeInt(idProfesor);
			salida.flush();

			int len = entrada.readInt();
			byte[] data = new byte[len];
			entrada.readFully(data);
			return new String(data, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public synchronized boolean crearReunion(Reuniones reunion) {
		try {
			ensureConnected();
			String json = gson.toJson(reunion);
			salida.writeUTF("CREAR_REUNION");
			salida.writeUTF(json);
			salida.flush();

			boolean creada = entrada.readBoolean();
			return creada;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

    public synchronized boolean modificarReunion(Reuniones reunion) {
        try {
            ensureConnected();
            String json = gson.toJson(reunion);
            salida.writeUTF("MODIFICAR_REUNION");
            salida.writeUTF(json);
            salida.flush();

            boolean modificado = entrada.readBoolean();
            return modificado;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	
	

	public synchronized void desconectar() {
		try {
			if (socket != null && !socket.isClosed()) {
				try {
					salida.writeUTF("LOGOUT");
					salida.flush();
				} catch (Exception e) {
					// ignore
				}
				socket.close();
				socket = null;
				entrada = null;
				salida = null;
				System.out.println("Socket cerrado correctamente");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
