package modelo;

import java.util.List;

public class CentrosData {
    private List<Centro> CENTROS;

    public CentrosData() {
    }

    public CentrosData(List<Centro> CENTROS) {
        this.CENTROS = CENTROS;
    }

    public List<Centro> getCENTROS() {
        return CENTROS;
    }

    public void setCENTROS(List<Centro> CENTROS) {
        this.CENTROS = CENTROS;
    }
}
