/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

/**
 *
 * @author Marco
 */
public class Etiquetas {
    private int etiquetaActual;
    private String tipo;
    private int eVerdadera;
    private int eFalsa;
    private int eSiguiente;
    private int e1Verdadera;
    private int e1Falsa;
    private int e2Verdadera;
    private int e2Falsa;

    /**
     * @return the etiquetaActual
     */
    public int getEtiquetaActual() {
        return etiquetaActual;
    }

    /**
     * @param etiquetaActual the etiquetaActual to set
     */
    public void setEtiquetaActual(int etiquetaActual) {
        this.etiquetaActual = etiquetaActual;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the eVerdadera
     */
    public int geteVerdadera() {
        return eVerdadera;
    }

    /**
     * @param eVerdadera the eVerdadera to set
     */
    public void seteVerdadera(int eVerdadera) {
        this.eVerdadera = eVerdadera;
    }

    /**
     * @return the eFalsa
     */
    public int geteFalsa() {
        return eFalsa;
    }

    /**
     * @param eFalsa the eFalsa to set
     */
    public void seteFalsa(int eFalsa) {
        this.eFalsa = eFalsa;
    }

    /**
     * @return the eSiguiente
     */
    public int geteSiguiente() {
        return eSiguiente;
    }

    /**
     * @param eSiguiente the eSiguiente to set
     */
    public void seteSiguiente(int eSiguiente) {
        this.eSiguiente = eSiguiente;
    }

    /**
     * @return the e1Verdadera
     */
    public int getE1Verdadera() {
        return e1Verdadera;
    }

    /**
     * @param e1Verdadera the e1Verdadera to set
     */
    public void setE1Verdadera(int e1Verdadera) {
        this.e1Verdadera = e1Verdadera;
    }

    /**
     * @return the e1Falsa
     */
    public int getE1Falsa() {
        return e1Falsa;
    }

    /**
     * @param e1Falsa the e1Falsa to set
     */
    public void setE1Falsa(int e1Falsa) {
        this.e1Falsa = e1Falsa;
    }

    /**
     * @return the e2Verdadera
     */
    public int getE2Verdadera() {
        return e2Verdadera;
    }

    /**
     * @param e2Verdadera the e2Verdadera to set
     */
    public void setE2Verdadera(int e2Verdadera) {
        this.e2Verdadera = e2Verdadera;
    }

    /**
     * @return the e2Falsa
     */
    public int getE2Falsa() {
        return e2Falsa;
    }

    /**
     * @param e2Falsa the e2Falsa to set
     */
    public void setE2Falsa(int e2Falsa) {
        this.e2Falsa = e2Falsa;
    }

    public Etiquetas(String tipo, int eVerdadera, int eFalsa) {
        this.tipo = tipo;
        this.eVerdadera = eVerdadera;
        this.eFalsa = eFalsa;
    }

    public Etiquetas(String tipo, int eVerdadera, int eFalsa, int eSiguiente) {
        this.tipo = tipo;
        this.eVerdadera = eVerdadera;
        this.eFalsa = eFalsa;
        this.eSiguiente = eSiguiente;
    }

    public Etiquetas(String tipo, int e1Verdadera, int e1Falsa, int e2Verdadera, int e2Falsa) {
        this.tipo = tipo;
        this.e1Verdadera = e1Verdadera;
        this.e1Falsa = e1Falsa;
        this.e2Verdadera = e2Verdadera;
        this.e2Falsa = e2Falsa;
    }
    
    
}
