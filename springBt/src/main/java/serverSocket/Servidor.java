package serverSocket
;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean terminar=false;
		ServerSocket servidor ;
		
		try {
			servidor = new ServerSocket(5000);
			System.out.println("Servidor : ON");
			System.out.println("Esperando Conexiones......");
			
			while (!terminar) {
				Socket conx = servidor.accept();
				HiloServidor hiloSrvdr = new HiloServidor(conx);
				hiloSrvdr.start();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
