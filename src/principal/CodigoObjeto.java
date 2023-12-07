/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

/**
 *
 * @author PC
 */
public class CodigoObjeto {

    private String[] codigoNoProcesado;
    private String seccionDatos;
    private int numMsg;

    public CodigoObjeto(String[] codigoNoProcesado) {
        this.codigoNoProcesado = codigoNoProcesado;
        this.seccionDatos = ".DATA\n";
        this.numMsg = 0;
    }

    public String org() {
        String codigoGenerado = generarCodigoObjeto(0);
        String codigoCompleto = ".MODEL small\n.STACK\n" + seccionDatos + ".code\n" + codigoGenerado;
        return codigoCompleto;
    }

    public String generarCodigoObjeto(int indice) {
        if (codigoNoProcesado.length == indice) {
            return "";
        }
        if (codigoNoProcesado[indice].contains(":") && !codigoNoProcesado[indice].contains("goto")) {
            return codigoNoProcesado[indice] + "\n" + generarCodigoObjeto(indice + 1);
        }
        if (codigoNoProcesado[indice].startsWith("temporal")) {
            String exVal = codigoNoProcesado[indice].replaceAll("=", "@").replaceAll(" ", "");
            String[] linea = exVal.split("@");
            if (!seccionDatos.contains(linea[0])) {
                declaracion(linea[0], "db", "0");
            }
        }
        String codigoProcesado = "";
        String[] linea = codigoNoProcesado[indice].split(" ");
        switch (linea[0]) {
            case "NUM":
                declaracion(linea[1], "db", "0");
                return generarCodigoObjeto(indice + 1);
            case "CAD":
                return generarCodigoObjeto(indice + 1);
            case "CHAR":
                declaracion(linea[1], "db", "\"\"");
                return generarCodigoObjeto(indice + 1);
            case "BOOL":
                declaracion(linea[1], "db", "0");
                return generarCodigoObjeto(indice + 1);
            case "STF":
                codigoProcesado = Condicion(codigoNoProcesado[indice], codigoNoProcesado[indice + 1]);
                indice++;
                break;
            case "goto":
                codigoProcesado = "JMP " + codigoNoProcesado[indice].replaceAll("goto", "").replaceAll(" ", "").replaceAll(":", "");
                break;
            case "CLASE":
                return generarCodigoObjeto(indice + 1);
            case "PRINCIPAL":
                codigoProcesado = "PRINCIPAL:\nmov ax, @data\nmov ds, ax";
                break;
            case "CAP":
                codigoProcesado = "MOV Ah, 01h\nINT 21h"
                        +"\nSUB Al, 30h"
                        + "\nMOV  " + codigoNoProcesado[indice].replace("CAP", "").replace("(", "").replace(")", "").replace(";", "").replaceAll(" ", "") + ", Al";
                break;
            case "IMP":
                codigoProcesado = Impresion(codigoNoProcesado[indice]);
                break;
            default:
        }

        if (codigoNoProcesado[indice].contains("=")) {
            codigoProcesado = operacion(codigoNoProcesado[indice]);
        }

        return codigoProcesado + "\n\n" + generarCodigoObjeto(indice + 1);
    }

    public void declaracion(String var, String mem, String valor) {
        if (valor.contains("\"")) {
            valor="10,13, "+valor+ ", \"$\"";
        }
        seccionDatos += "     " + var + "\t" + mem + "\t" + valor + "\n";
    }

    public String Condicion(String linea1, String linea2) {
        String salto = "";
        String extrVariables = "";
        String[][] matCon = {
            {">", "JG "},
            {"<", "JL "},
            {">=", "JGE "},
            {"<=", "JLE "},
            {"==", "JE "},};
        for (int i = 0; i < matCon.length; i++) {
            if (linea1.contains(matCon[i][0])) {
                salto = matCon[i][1];
                extrVariables = linea1.replaceAll("STF", "").replaceAll("goto", "@").replaceAll(matCon[i][0], "@").replaceAll(" ", "");
            }
        }
        String[] linea = extrVariables.split("@");

        return "MOV Al, " + linea[0]
                + "\nCMP Al," + linea[1]
                + "\n" + salto + linea[2]
                + "\nJMP " + linea2.replaceAll("goto", "").replaceAll(" ", "");
    }

    public String operacion(String linea) {
        String operacion = "";
        String extrVariables = "";
        String direccion1 = "Al";
        String direccion2 = "Bl";
        String limpia = "\nMOV Ax, 0\nMOV Bx, 0";
        boolean bol = false;
        String[][] matOp = {
            {"+", "ADD Al, Bl "},
            {"-", "SUB  Al, Bl"},
            {"*", "MUL Bl "},
            {"/", "DIV Bl "},};
        for (int i = 0; i < matOp.length; i++) {
            if (i > 1) {
                limpia += "\nMOV Cx, 0";
            }
            if (linea.contains(matOp[i][0])) {
                operacion = matOp[i][1];
                String operador = matOp[i][0];
                if (i == 0) {
                    operador = "\\+";
                }
                if (i == 2) {
                    operador = "\\*";
                }
                extrVariables = linea.replaceAll("=", "@").replaceAll(operador, "@");
                bol = true;
                break;
            }
        }
        if (!bol) {
            extrVariables = linea.replaceAll("=", "@");
            String[] lineaD = extrVariables.split("@");
            if (lineaD[1].contains("\"") ) {
                if (!seccionDatos.contains(" "+lineaD[0]+" ")) {
                    declaracion(lineaD[0], "db", lineaD[1]);
                }
                return "";
            }
            limpia = "\nMOV Ax, 0";
            return "MOV Al" + ", " + lineaD[1]
                    + "\nMOV " + lineaD[0] + ", " + "Al";
        }
        String[] lineaD = extrVariables.split("@");
        return "MOV " + direccion1 + ", " + lineaD[1]
                + "\nMOV " + direccion2 + ", " + lineaD[2]
                + "\n" + operacion
                + "\nMOV " + lineaD[0] + ", " + direccion1;
    }
    public String Impresion(String linea) {
        String[] valores = linea.replace("IMP", "").replace("(", "").replace(")", "").replace(";", "").split("%");
        String impresiones = "";
        for (String valor : valores) {
            if (valor.contains("\"")) {
                declaracion("mensaje"+numMsg, "db", valor);
                impresiones+="MOV Ah, 09h\nLEA Dx, mensaje"+numMsg
                        + "\nint 21h\n\n";
                numMsg++;
            }else{
                impresiones+="MOV Al, " + valor
                        +"\nADD Al, 30h"
                        + "\nMOV " + valor + ", Al\n\n"
                        + "\nMOV Ah, 10"
                        + "\nMOV Bh, 0"
                        + "\nMOV Cx, 1 "
                        + "\nMOV Al, " + valor 
                        +"\nINT 10h\n\n"
                        + "MOV Al, " + valor
                        +"\nSUB Al, 30h"
                        + "\nMOV " + valor + ", Al\n\n";
            }
        }
        return impresiones;
    }

}
