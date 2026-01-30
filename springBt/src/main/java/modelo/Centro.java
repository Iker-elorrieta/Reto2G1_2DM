package modelo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Centro {
    private String CCEN;
    private String NOM;
    private String NOME;
    private String DGENRC;
    private String DGENRE;
    private String GENR;
    private String MUNI;
    private String DMUNIC;
    private String DMUNIE;
    private String DTERRC;
    private String DTERRE;
    private String DEPE;
    private String DTITUC;
    private String DTITUE;
    private String DOMI;
    private String CPOS;
    private String TEL1;
    private String TFAX;
    private String EMAIL;
    private String PAGINA;
    private String COOR_X;
    private String COOR_Y;
    private String LATITUD;
    private String LONGITUD;

    // Constructor vacío
    public Centro() {
    }

    // Constructor con todos los parámetros
    public Centro(String CCEN, String NOM, String NOME, String DGENRC, String DGENRE, 
                  String GENR, String MUNI, String DMUNIC, String DMUNIE, String DTERRC, 
                  String DTERRE, String DEPE, String DTITUC, String DTITUE, String DOMI, 
                  String CPOS, String TEL1, String TFAX, String EMAIL, String PAGINA, 
                  String COOR_X, String COOR_Y, String LATITUD, String LONGITUD) {
        this.CCEN = CCEN;
        this.NOM = NOM;
        this.NOME = NOME;
        this.DGENRC = DGENRC;
        this.DGENRE = DGENRE;
        this.GENR = GENR;
        this.MUNI = MUNI;
        this.DMUNIC = DMUNIC;
        this.DMUNIE = DMUNIE;
        this.DTERRC = DTERRC;
        this.DTERRE = DTERRE;
        this.DEPE = DEPE;
        this.DTITUC = DTITUC;
        this.DTITUE = DTITUE;
        this.DOMI = DOMI;
        this.CPOS = CPOS;
        this.TEL1 = TEL1;
        this.TFAX = TFAX;
        this.EMAIL = EMAIL;
        this.PAGINA = PAGINA;
        this.COOR_X = COOR_X;
        this.COOR_Y = COOR_Y;
        this.LATITUD = LATITUD;
        this.LONGITUD = LONGITUD;
    }

    // Getters y Setters
    public String getCCEN() {
        return CCEN;
    }

    public void setCCEN(String CCEN) {
        this.CCEN = CCEN;
    }

    public String getNOM() {
        return NOM;
    }

    public void setNOM(String NOM) {
        this.NOM = NOM;
    }

    public String getNOME() {
        return NOME;
    }

    public void setNOME(String NOME) {
        this.NOME = NOME;
    }

    public String getDGENRC() {
        return DGENRC;
    }

    public void setDGENRC(String DGENRC) {
        this.DGENRC = DGENRC;
    }

    public String getDGENRE() {
        return DGENRE;
    }

    public void setDGENRE(String DGENRE) {
        this.DGENRE = DGENRE;
    }

    public String getGENR() {
        return GENR;
    }

    public void setGENR(String GENR) {
        this.GENR = GENR;
    }

    public String getMUNI() {
        return MUNI;
    }

    public void setMUNI(String MUNI) {
        this.MUNI = MUNI;
    }

    public String getDMUNIC() {
        return DMUNIC;
    }

    public void setDMUNIC(String DMUNIC) {
        this.DMUNIC = DMUNIC;
    }

    public String getDMUNIE() {
        return DMUNIE;
    }

    public void setDMUNIE(String DMUNIE) {
        this.DMUNIE = DMUNIE;
    }

    public String getDTERRC() {
        return DTERRC;
    }

    public void setDTERRC(String DTERRC) {
        this.DTERRC = DTERRC;
    }

    public String getDTERRE() {
        return DTERRE;
    }

    public void setDTERRE(String DTERRE) {
        this.DTERRE = DTERRE;
    }

    public String getDEPE() {
        return DEPE;
    }

    public void setDEPE(String DEPE) {
        this.DEPE = DEPE;
    }

    public String getDTITUC() {
        return DTITUC;
    }

    public void setDTITUC(String DTITUC) {
        this.DTITUC = DTITUC;
    }

    public String getDTITUE() {
        return DTITUE;
    }

    public void setDTITUE(String DTITUE) {
        this.DTITUE = DTITUE;
    }

    public String getDOMI() {
        return DOMI;
    }

    public void setDOMI(String DOMI) {
        this.DOMI = DOMI;
    }

    public String getCPOS() {
        return CPOS;
    }

    public void setCPOS(String CPOS) {
        this.CPOS = CPOS;
    }

    public String getTEL1() {
        return TEL1;
    }

    public void setTEL1(String TEL1) {
        this.TEL1 = TEL1;
    }

    public String getTFAX() {
        return TFAX;
    }

    public void setTFAX(String TFAX) {
        this.TFAX = TFAX;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getPAGINA() {
        return PAGINA;
    }

    public void setPAGINA(String PAGINA) {
        this.PAGINA = PAGINA;
    }

    public String getCOOR_X() {
        return COOR_X;
    }

    public void setCOOR_X(String COOR_X) {
        this.COOR_X = COOR_X;
    }

    public String getCOOR_Y() {
        return COOR_Y;
    }

    public void setCOOR_Y(String COOR_Y) {
        this.COOR_Y = COOR_Y;
    }

    public String getLATITUD() {
        return LATITUD;
    }

    public void setLATITUD(String LATITUD) {
        this.LATITUD = LATITUD;
    }

    public String getLONGITUD() {
        return LONGITUD;
    }

    public void setLONGITUD(String LONGITUD) {
        this.LONGITUD = LONGITUD;
    }

    @Override
    public String toString() {
        return "Centro{" +
                "CCEN='" + CCEN + '\'' +
                ", NOM='" + NOM + '\'' +
                ", NOME='" + NOME + '\'' +
                ", DGENRC='" + DGENRC + '\'' +
                ", DGENRE='" + DGENRE + '\'' +
                ", GENR='" + GENR + '\'' +
                ", MUNI='" + MUNI + '\'' +
                ", DMUNIC='" + DMUNIC + '\'' +
                ", DMUNIE='" + DMUNIE + '\'' +
                ", DTERRC='" + DTERRC + '\'' +
                ", DTERRE='" + DTERRE + '\'' +
                ", DEPE='" + DEPE + '\'' +
                ", DTITUC='" + DTITUC + '\'' +
                ", DTITUE='" + DTITUE + '\'' +
                ", DOMI='" + DOMI + '\'' +
                ", CPOS='" + CPOS + '\'' +
                ", TEL1='" + TEL1 + '\'' +
                ", TFAX='" + TFAX + '\'' +
                ", EMAIL='" + EMAIL + '\'' +
                ", PAGINA='" + PAGINA + '\'' +
                ", COOR_X='" + COOR_X + '\'' +
                ", COOR_Y='" + COOR_Y + '\'' +
                ", LATITUD='" + LATITUD + '\'' +
                ", LONGITUD='" + LONGITUD + '\'' +
                '}';
    }
    
    private static List<Centro> centros;

    /**
     * Carga los centros desde el archivo JSON usando Gson
     */
    public static void cargarCentrosDesdeJSON() {
        try {
            // Cargar el archivo JSON desde resources
            ClassPathResource resource = new ClassPathResource("EuskadiLatLon.json");
            InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);

            // Crear instancia de Gson
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Deserializar el JSON a un objeto CentrosData
            Centro[] centrosData = gson.fromJson(reader, Centro[].class);

            // Convertir todos los campos a String
            if (centrosData != null && centrosData != null) {
                for (Centro centro : centrosData) {
                    // Convertir todos los campos a String si no lo son
                	centro.convertirCamposAString();
                    centros.add(centro);
                }
                System.out.println("Se cargaron " + centros.size() + " centros desde el JSON");
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
    private void convertirCamposAString() {
        if (getCCEN() != null) setCCEN(String.valueOf(getCCEN()));
        if (getNOM() != null) setNOM(String.valueOf(getNOM()));
        if (getNOME() != null) setNOME(String.valueOf(getNOME()));
        if (getDGENRC() != null) setDGENRC(String.valueOf(getDGENRC()));
        if (getDGENRE() != null) setDGENRE(String.valueOf(getDGENRE()));
        if (getGENR() != null) setGENR(String.valueOf(getGENR()));
        if (getMUNI() != null) setMUNI(String.valueOf(getMUNI()));
        if (getDMUNIC() != null) setDMUNIC(String.valueOf(getDMUNIC()));
        if (getDMUNIE() != null) setDMUNIE(String.valueOf(getDMUNIE()));
        if (getDTERRC() != null) setDTERRC(String.valueOf(getDTERRC()));
        if (getDTERRE() != null) setDTERRE(String.valueOf(getDTERRE()));
        if (getDEPE() != null) setDEPE(String.valueOf(getDEPE()));
        if (getDTITUC() != null) setDTITUC(String.valueOf(getDTITUC()));
        if (getDTITUE() != null) setDTITUE(String.valueOf(getDTITUE()));
        if (getDOMI() != null) setDOMI(String.valueOf(getDOMI()));
        if (getCPOS() != null) setCPOS(String.valueOf(getCPOS()));
        if (getTEL1() != null) setTEL1(String.valueOf(getTEL1()));
        if (getTFAX() != null) setTFAX(String.valueOf(getTFAX()));
        if (getEMAIL() != null) setEMAIL(String.valueOf(getEMAIL()));
        if (getPAGINA() != null) setPAGINA(String.valueOf(getPAGINA()));
        if (getCOOR_X() != null) setCOOR_X(String.valueOf(getCOOR_X()));
        if (getCOOR_Y() != null) setCOOR_Y(String.valueOf(getCOOR_Y()));
        if (getLATITUD() != null) setLATITUD(String.valueOf(getLATITUD()));
        if (getLONGITUD() != null) setLONGITUD(String.valueOf(getLONGITUD()));
    }
    /**
     * Obtiene todos los centros
     * @return Lista de todos los centros
     */
    public static List<Centro> obtenerTodosCentros() {
        return centros;
    }

    /**
     * Obtiene un centro por su código
     * @param ccen Código del centro
     * @return Centro encontrado o null
     */
    public static Centro obtenerCentroPorCodigo(String ccen) {
        return centros.stream()
                .filter(centro -> centro.getCCEN().equals(ccen))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene centros por municipio
     * @param municipio Nombre del municipio
     * @return Lista de centros del municipio
     */
    public static List<Centro> obtenerCentrosPorMunicipio(String municipio) {
        List<Centro> resultado = new ArrayList<>();
        for (Centro centro : centros) {
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
    public static int obtenerNumeroCentros() {
        return centros.size();
    }
}
