package controlador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ControladorServidor {

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

	public DataInputStream getEntrada() {
		return entrada;
	}

	public DataOutputStream getSalida() {
		return salida;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ControladorServidor(Socket socket) {
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

}
