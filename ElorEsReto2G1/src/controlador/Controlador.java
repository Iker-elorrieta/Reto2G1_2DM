package controlador;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
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
			URI uri = URI.create("http://localhost:8080/api/perfil/" + idUsuario);
			HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
			con.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;

			while ((line = in.readLine()) != null) {
				response.append(line);
			}
			in.close();

			Gson gson = new Gson();
			return gson.fromJson(response.toString(), Users.class);

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
		        URI uri = URI.create("http://localhost:8080/api/profesor/" + idProfesor + "/alumnos");
		        HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
		        con.setRequestMethod("GET");

		        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		        StringBuilder response = new StringBuilder();
		        String line;

		        while ((line = in.readLine()) != null) {
		            response.append(line);
		        }
		        in.close();

		        Gson gson = new Gson();
		        Users[] alumnosArray = gson.fromJson(response.toString(), Users[].class);

		        return Arrays.asList(alumnosArray);

		    } catch (Exception e) {
		        e.printStackTrace();
		        return new ArrayList<>();
		    }
		}

	public List<Horarios> obtenerHorario(int idProfesor) {
	    try {
	        URI uri = URI.create("http://localhost:8080/api/horario/" + idProfesor);
	        HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
	        con.setRequestMethod("GET");

	        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        StringBuilder response = new StringBuilder();
	        String line;

	        while ((line = in.readLine()) != null) {
	            response.append(line);
	        }
	        in.close();

	        Gson gson = new Gson();
	        Horarios[] horarioArray = gson.fromJson(response.toString(), Horarios[].class);

	        return Arrays.asList(horarioArray);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}

	}
