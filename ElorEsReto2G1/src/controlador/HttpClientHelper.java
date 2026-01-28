package controlador;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class HttpClientHelper {

    private static final String BASE_URL = "http://localhost:8080/api/";

    public static String get(String endpoint) throws Exception {
        HttpURLConnection con = abrirConexion(BASE_URL + endpoint, "GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            return br.readLine();
        }
    }

    public static String postJson(String endpoint, String json) throws Exception {
        HttpURLConnection con = abrirConexion(BASE_URL + endpoint, "POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        con.getOutputStream().write(json.getBytes("UTF-8"));

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            return br.readLine();
        }
    }

    private static HttpURLConnection abrirConexion(String endpoint, String metodo) throws Exception {
        URI uri = new URI(endpoint);
        URL url = uri.toURL(); 

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(metodo);
        return con;
    }

}
