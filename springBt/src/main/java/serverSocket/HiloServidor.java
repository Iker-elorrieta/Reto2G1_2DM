package serverSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import com.example.springBt.LoginService;
import modelo.Users;

public class HiloServidor extends Thread {

    private Socket conx;

    public HiloServidor(Socket conx) {
        this.conx = conx;
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

                    if (operacion.equalsIgnoreCase("LOGIN")) {

                        String usuario = entrada.readUTF();
                        String password = entrada.readUTF();

                        LoginService loginService = new LoginService();
                        Users user = loginService.login(usuario, password);

                        int codigo = 0;
                        int id = -1;

                        if (user != null) {
                       
                        	if (user.getTipos().getId() == 4) {
                        	    codigo = 2;
                        	} else {
                        	    codigo = 1;
                        	}

                            id = user.getId();
                        }

                        salida.writeInt(codigo);
                        salida.writeInt(id);
                        salida.flush();

                        System.out.println("Login procesado: usuario=" + usuario + " | codigo=" + codigo + " | id=" + id);

                    } else {
                        System.out.println("Comando desconocido: " + operacion);
                    }

                } catch (Exception e) {
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
