package serverSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends Thread {

    private boolean terminar = false;

    @Override
    public void run() {
        try (ServerSocket servidor = new ServerSocket(5000)) {
            System.out.println("Servidor : ON");
            System.out.println("Esperando Conexiones......");

            while (!terminar) {
                Socket conx = servidor.accept();
                HiloServidor hiloSrvdr = new HiloServidor(conx);
                hiloSrvdr.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
