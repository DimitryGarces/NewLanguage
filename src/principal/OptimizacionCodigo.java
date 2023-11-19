/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Marco
 */
public class OptimizacionCodigo {

    boolean nvoOptim = false;
    String resultadoOptm = "";

    public String procesarCodigoIntermedio(String[] codigo, String[] lineArray2) {
        String resultado = "";
        for (int i = 0; i < codigo.length; i++) {
            if (codigo[i].startsWith("temporal1")) {
                String bloqueTemporal[];
                bloqueTemporal = new String[1];
                bloqueTemporal[0] = codigo[i];
                System.out.println("********" + codigo[i]);
                boolean first = true;
                do {
                    if (bloqueTemporal.length != 1) {
                        String bloqueTemporalAux[] = new String[bloqueTemporal.length + 1];
                        System.arraycopy(bloqueTemporal, 0, bloqueTemporalAux, 0, bloqueTemporal.length);
                        bloqueTemporalAux[bloqueTemporal.length] = codigo[i];
                        bloqueTemporal = bloqueTemporalAux;
                        i++;
                    } else {
                        if (codigo[i].equals(bloqueTemporal[0]) && first) {
                            String bloqueTemporalAux[] = new String[bloqueTemporal.length + 1];
                            System.arraycopy(bloqueTemporal, 0, bloqueTemporalAux, 0, bloqueTemporal.length);
                            bloqueTemporalAux[bloqueTemporal.length] = codigo[i + 1];
                            bloqueTemporal = bloqueTemporalAux;
                            i++;
                            first = false;
                        }
                        i++;
                    }
                    if (!codigo[i].startsWith("temporal") && codigo[i].contains("temporal")) {
                        String bloqueTemporalAux[] = new String[bloqueTemporal.length + 1];
                        System.arraycopy(bloqueTemporal, 0, bloqueTemporalAux, 0, bloqueTemporal.length);
                        bloqueTemporalAux[bloqueTemporal.length] = codigo[i];
                        bloqueTemporal = bloqueTemporalAux;
                    }
                } while (codigo[i].startsWith("temporal"));
                resultado += procesarBloque(bloqueTemporal, lineArray2);
                if (nvoOptim && !resultadoOptm.equals("")) {
                    return resultadoOptm;
                }
            } else {
                resultado += codigo[i] + "\n";
            }
        }
        return resultado;
    }

    private String procesarBloque(String[] bloqueTemporal, String[] lineArray2) {
        String resultado = "";

        for (int i = 0; i < bloqueTemporal.length; i++) {
            String linea = bloqueTemporal[i].replaceAll(" ", "");
            String partes[] = linea.split("=");
            boolean uno = true;
            if (partes.length != 1) {
                String op[] = partes[1].split("[*/+-]");
                if (op.length != 1) {
                    if (esNumero(op[0]) && !esNumero(op[1])) {
                        double temporal = Double.parseDouble(op[0]);
                        if (temporal == 0) {
                            CodigoIntermedioGenerator nvoIntermedio = new CodigoIntermedioGenerator();
                            String nvoResultado = nvoIntermedio.generarCodigoIntermedio(lineArray2, true);
                            System.out.println(nvoResultado + "===");
                            String[] lineas = nvoResultado.split("\n");
                            OptimizacionCodigo nvoOpt = new OptimizacionCodigo();
                            resultadoOptm = nvoOpt.procesarCodigoIntermedio(lineas, lineArray2);
                            nvoOptim = true;
                            return resultado;
                        }
                    } else if (!esNumero(op[0]) && esNumero(op[1])) {
                        double temporal = Double.parseDouble(op[1]);
                        if (temporal == 0) {
                            CodigoIntermedioGenerator nvoIntermedio = new CodigoIntermedioGenerator();
                            String nvoResultado = nvoIntermedio.generarCodigoIntermedio(lineArray2, true);
                            System.out.println(nvoResultado + "===");
                            String[] lineas = nvoResultado.split("\n");
                            OptimizacionCodigo nvoOpt = new OptimizacionCodigo();
                            resultadoOptm = nvoOpt.procesarCodigoIntermedio(lineas, lineArray2);
                            nvoOptim = true;
                            return resultado;
                        }
                    } else if (esNumero(op[0]) && esNumero(op[1])) {
                        double temporal = Double.parseDouble(op[0]);
                        double temporal2 = Double.parseDouble(op[1]);
                        if (temporal == 0 || temporal2 == 0) {
                            CodigoIntermedioGenerator nvoIntermedio = new CodigoIntermedioGenerator();
                            String nvoResultado = nvoIntermedio.generarCodigoIntermedio(lineArray2, true);
                            System.out.println(nvoResultado + "===");
                            String[] lineas = nvoResultado.split("\n");
                            OptimizacionCodigo nvoOpt = new OptimizacionCodigo();
                            resultadoOptm = nvoOpt.procesarCodigoIntermedio(lineas, lineArray2);
                            nvoOptim = true;
                            return resultado;
                        }
                        double temporal3;
                        if (partes[1].contains("*")) {
                            temporal3 = temporal * temporal2;
                            bloqueTemporal[i] = "";
                            String temporal3s = String.valueOf(temporal3);
                            for (int j = 0; j < bloqueTemporal.length; j++) {
                                if (bloqueTemporal[j].contains(partes[0])) {
                                    String replace = bloqueTemporal[j].replaceAll(partes[0], temporal3s);
                                    bloqueTemporal[j] = replace;
                                }
                            }
                        } else if (partes[1].contains("/")) {
                            temporal3 = temporal / temporal2;
                            bloqueTemporal[i] = "";
                            String temporal3s = String.valueOf(temporal3);
                            for (int j = 0; j < bloqueTemporal.length; j++) {
                                if (bloqueTemporal[j].contains(partes[0])) {
                                    String replace = bloqueTemporal[j].replaceAll(partes[0], temporal3s);
                                    bloqueTemporal[j] = replace;
                                }
                            }
                        } else if (partes[1].contains("+")) {
                            temporal3 = temporal + temporal2;
                            bloqueTemporal[i] = "";
                            String temporal3s = String.valueOf(temporal3);
                            for (int j = 0; j < bloqueTemporal.length; j++) {
                                if (bloqueTemporal[j].contains(partes[0])) {
                                    String replace = bloqueTemporal[j].replaceAll(partes[0], temporal3s);
                                    bloqueTemporal[j] = replace;
                                }
                            }
                        } else if (partes[1].contains("-")) {
                            temporal3 = temporal - temporal2;
                            bloqueTemporal[i] = "";
                            String temporal3s = String.valueOf(temporal3);
                            for (int j = 0; j < bloqueTemporal.length; j++) {
                                if (bloqueTemporal[j].contains(partes[0])) {
                                    String replace = bloqueTemporal[j].replaceAll(partes[0], temporal3s);
                                    bloqueTemporal[j] = replace;
                                }
                            }
                        }

                    }
                } else if (partes[1].contains("*") || partes[1].contains("/")) {
                    String operacion[] = partes[1].split("[*/]");
                    if (operacion[0].equals("1") || operacion[1].equals("1")) {
                        if (partes[1].contains("*")) {
                            boolean izq = true;
                            System.out.println("Izquierda: " + operacion[0] + " Derecha: " + operacion[1]);
                            bloqueTemporal[i] = "";
                            if (!operacion[0].equals("1")) {
                                izq = false;
                            }
                            for (int j = 0; j < bloqueTemporal.length; j++) {
                                String opera = bloqueTemporal[j].replaceAll(" ", "");
                                String part[] = opera.split("=");
                                if (part.length != 1) {
                                    if (part[1].contains(partes[0])) {
                                        if (izq) {
                                            String replace = part[1].replaceAll(partes[0], operacion[1]);
                                            bloqueTemporal[j] = part[0] + " = " + replace;
                                        } else {
                                            String replace = part[1].replaceAll(partes[0], operacion[0]);
                                            bloqueTemporal[j] = part[0] + " = " + replace;
                                        }

                                    }
                                }

                            }
                            uno = false;
                        } else {
                            if (operacion[1].equals("1")) {
                                System.out.println("Izquierda: " + operacion[0] + " Derecha: " + operacion[1]);
                                bloqueTemporal[i] = "";
                                for (int j = 0; j < bloqueTemporal.length; j++) {
                                    String opera = bloqueTemporal[j].replaceAll(" ", "");
                                    String part[] = opera.split("=");
                                    if (part.length != 1) {
                                        if (part[1].contains(partes[0])) {
                                            String replace = part[1].replaceAll(partes[0], operacion[0]);
                                            bloqueTemporal[j] = part[0] + " = " + replace;
                                        }
                                    }

                                }
                                uno = false;
                            }
                        }
                    }
                }
            }
            if (partes.length != 1 && uno) {
                System.out.println("Izquierda: " + partes[0] + " Derecha: " + partes[1]);
                for (int j = 0; j < bloqueTemporal.length; j++) {
                    String linea2 = bloqueTemporal[j].replaceAll(" ", "");
                    String partes2[] = linea2.split("=");
                    if (partes2.length != 1) {
                        if (!partes2[0].equals(partes[0])) {
                            if (partes2[1].equals(partes[1])) {
                                bloqueTemporal[j] = "";
                                for (int k = 0; k < bloqueTemporal.length; k++) {
                                    String linea3 = bloqueTemporal[k].replaceAll(" ", "");
                                    String partes3[] = linea3.split("=");
                                    if (partes3.length != 1) {
                                        if (partes3[1].contains(partes2[0])) {
                                            String replace = partes3[1].replaceAll(partes2[0], partes[0]);
                                            bloqueTemporal[k] = partes3[0] + " = " + replace;
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        for (String bloqueTemporal1 : bloqueTemporal) {
            resultado += bloqueTemporal1 + "\n";
        }
        return resultado;
    }

    private boolean esNumero(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
