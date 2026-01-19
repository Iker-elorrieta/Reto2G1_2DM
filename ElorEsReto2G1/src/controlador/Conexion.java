package controlador;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/eduelorrietacondatos";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConexion() {
        Connection con = null;

        try {
            con = DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }
}
