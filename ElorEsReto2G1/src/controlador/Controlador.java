package controlador;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.springBt.LoginService;
import com.google.gson.Gson;

import modelo.Users;
import vista.Login;



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
	        URL url = new URL("http://localhost:8080/api/perfil/" + idUsuario);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
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


	}
	
	
	
	
	
	


