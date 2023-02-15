package principal;

public class Codi {
    private String nomTemp;
    private String valor;
    public Codi(String nomTemp, String valor) {
        this.nomTemp = nomTemp;
        this.valor = valor;
    }
    public String getNomTemp() {
        return nomTemp;
    }
    public void setNomTemp(String nomTemp) {
        this.nomTemp = nomTemp;
    }
    public String getValor() {
        return valor;
    }
    public void setValor(String valor) {
        this.valor = valor;
    }
}
