package com.example.springBt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import modelo.Centro;
import modelo.CentrosData;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CentrosService {

    private List<Centro> centros;

    public CentrosService() {
        this.centros = new ArrayList<>();
        cargarCentrosDesdeJSON();
    }

    /**
     * Carga los centros desde el archivo JSON usando Gson
     */
    private void cargarCentrosDesdeJSON() {
        try {
            // Cargar el archivo JSON desde resources
            ClassPathResource resource = new ClassPathResource("EuskadiLatLon.json");
            InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);

            // Crear instancia de Gson
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Deserializar el JSON a un objeto CentrosData
            CentrosData centrosData = gson.fromJson(reader, CentrosData.class);

            // Convertir todos los campos a String
            if (centrosData != null && centrosData.getCENTROS() != null) {
                for (Centro centro : centrosData.getCENTROS()) {
                    // Convertir todos los campos a String si no lo son
                    convertirCamposAString(centro);
                    this.centros.add(centro);
                }
                System.out.println("Se cargaron " + this.centros.size() + " centros desde el JSON");
            }

            reader.close();

        } catch (IOException e) {
            System.err.println("Error al cargar el archivo JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Convierte todos los campos del centro a String
     */
    private void convertirCamposAString(Centro centro) {
        if (centro.getCCEN() != null) centro.setCCEN(String.valueOf(centro.getCCEN()));
        if (centro.getNOM() != null) centro.setNOM(String.valueOf(centro.getNOM()));
        if (centro.getNOME() != null) centro.setNOME(String.valueOf(centro.getNOME()));
        if (centro.getDGENRC() != null) centro.setDGENRC(String.valueOf(centro.getDGENRC()));
        if (centro.getDGENRE() != null) centro.setDGENRE(String.valueOf(centro.getDGENRE()));
        if (centro.getGENR() != null) centro.setGENR(String.valueOf(centro.getGENR()));
        if (centro.getMUNI() != null) centro.setMUNI(String.valueOf(centro.getMUNI()));
        if (centro.getDMUNIC() != null) centro.setDMUNIC(String.valueOf(centro.getDMUNIC()));
        if (centro.getDMUNIE() != null) centro.setDMUNIE(String.valueOf(centro.getDMUNIE()));
        if (centro.getDTERRC() != null) centro.setDTERRC(String.valueOf(centro.getDTERRC()));
        if (centro.getDTERRE() != null) centro.setDTERRE(String.valueOf(centro.getDTERRE()));
        if (centro.getDEPE() != null) centro.setDEPE(String.valueOf(centro.getDEPE()));
        if (centro.getDTITUC() != null) centro.setDTITUC(String.valueOf(centro.getDTITUC()));
        if (centro.getDTITUE() != null) centro.setDTITUE(String.valueOf(centro.getDTITUE()));
        if (centro.getDOMI() != null) centro.setDOMI(String.valueOf(centro.getDOMI()));
        if (centro.getCPOS() != null) centro.setCPOS(String.valueOf(centro.getCPOS()));
        if (centro.getTEL1() != null) centro.setTEL1(String.valueOf(centro.getTEL1()));
        if (centro.getTFAX() != null) centro.setTFAX(String.valueOf(centro.getTFAX()));
        if (centro.getEMAIL() != null) centro.setEMAIL(String.valueOf(centro.getEMAIL()));
        if (centro.getPAGINA() != null) centro.setPAGINA(String.valueOf(centro.getPAGINA()));
        if (centro.getCOOR_X() != null) centro.setCOOR_X(String.valueOf(centro.getCOOR_X()));
        if (centro.getCOOR_Y() != null) centro.setCOOR_Y(String.valueOf(centro.getCOOR_Y()));
        if (centro.getLATITUD() != null) centro.setLATITUD(String.valueOf(centro.getLATITUD()));
        if (centro.getLONGITUD() != null) centro.setLONGITUD(String.valueOf(centro.getLONGITUD()));
    }

    /**
     * Obtiene todos los centros
     * @return Lista de todos los centros
     */
    public List<Centro> obtenerTodosCentros() {
        return this.centros;
    }

    /**
     * Obtiene un centro por su código
     * @param ccen Código del centro
     * @return Centro encontrado o null
     */
    public Centro obtenerCentroPorCodigo(String ccen) {
        return this.centros.stream()
                .filter(centro -> centro.getCCEN().equals(ccen))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene centros por municipio
     * @param municipio Nombre del municipio
     * @return Lista de centros del municipio
     */
    public List<Centro> obtenerCentrosPorMunicipio(String municipio) {
        List<Centro> resultado = new ArrayList<>();
        for (Centro centro : this.centros) {
            if (centro.getDMUNIC() != null && centro.getDMUNIC().equalsIgnoreCase(municipio)) {
                resultado.add(centro);
            }
        }
        return resultado;
    }

    /**
     * Obtiene el número total de centros
     * @return Número de centros
     */
    public int obtenerNumeroCentros() {
        return this.centros.size();
    }
}
