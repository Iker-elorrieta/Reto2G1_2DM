package controlador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import modelo.Horarios;
import modelo.Users;

public class Controlador {

	private Socket socket;
	private DataInputStream entrada;
	private DataOutputStream salida;

	private int usuarioId;

	public int getUsuarioId() {
		return usuarioId;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Controlador(Socket socket) {
		this.socket = socket;
		try {
			this.entrada = new DataInputStream(socket.getInputStream());
			this.salida = new DataOutputStream(socket.getOutputStream());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String generarHash(String usu) {
		// TODO Auto-generated method stub
		String cifrado = "";

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] dataBytes = usu.getBytes();
			md.update(dataBytes);
			byte[] resumen = md.digest();

			StringBuilder sb = new StringBuilder();
			for (byte b : resumen) {
				sb.append(String.format("%02x", b));
			}
			cifrado = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return cifrado;
	}

	public int verificarLogin(String usuario, String password) {
		try {
			salida.writeUTF("LOGIN");
			salida.writeUTF(usuario);
			salida.writeUTF(password);
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

	public Users obtenerPerfil(int idUsuario) {
		try {
			salida.writeUTF("GET_PERFIL");
			salida.writeInt(idUsuario);
			salida.flush();

			String jsonResponse = entrada.readUTF();

			if (jsonResponse != null && !jsonResponse.isEmpty()) {
				Gson gson = new Gson();
				return gson.fromJson(jsonResponse, Users.class);
			}

			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void desconectar() {
		// TODO Auto-generated method stub
		try {
            if (socket != null && !socket.isClosed()) {

           //Avisamos y luego se sale
                salida.writeUTF("LOGOUT");
                salida.flush();

                socket.close();
                System.out.println("Socket cerrado correctamente");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public List<Users> obtenerAlumnos(int idProfesor) {
		try {
			salida.writeUTF("GET_ALUMNOS");
			salida.writeInt(idProfesor);
			salida.flush();

			String jsonResponse = entrada.readUTF();

			if (jsonResponse != null && !jsonResponse.isEmpty()) {
				Gson gson = new Gson();
				Users[] alumnosArray = gson.fromJson(jsonResponse, Users[].class);
				return Arrays.asList(alumnosArray);
			}

			return new ArrayList<>();

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public List<Horarios> obtenerHorario(int idProfesor) {
		try {
			salida.writeUTF("GET_HORARIO");
			salida.writeInt(idProfesor);
			salida.flush();

			String jsonResponse = entrada.readUTF();

			if (jsonResponse != null && !jsonResponse.isEmpty()) {
				Gson gson = new Gson();
				Horarios[] horarioArray = gson.fromJson(jsonResponse, Horarios[].class);
				return Arrays.asList(horarioArray);
			}

			return new ArrayList<>();

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	}
