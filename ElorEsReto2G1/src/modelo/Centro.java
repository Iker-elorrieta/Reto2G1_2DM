package modelo;

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
}
