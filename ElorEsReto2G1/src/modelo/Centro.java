package modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import controlador.HttpClientHelper;

public class Centro {
    private String CCEN;
    private String NOM;
    private String DMUNIC;
    private String DTERRC;

    public String getCCEN() { return CCEN; }
    public String getNOM() { return NOM; }
    public String getDMUNIC() { return DMUNIC; }
    public String getDTERRC() { return DTERRC; }

    @Override
    public String toString() {
        return NOM ;
    }
    public static List<Centro> obtenerCentrosREST() {
        try {
            String json = HttpClientHelper.get("centros");

            Gson gson = new Gson();
            Centro[] array = gson.fromJson(json, Centro[].class);
            return Arrays.asList(array);

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
