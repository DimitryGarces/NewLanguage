/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodigoIntermedioGenerator {

    int etiquetaActual2 = 0;  // Empieza en 0 y aumenta de 10 en 10
    int SSiguiente = 0;

    // Función para generar una nueva etiqueta
//    private String nuevaEtiqueta() {
//        String etiqueta = "E" + etiquetaActual;
//        etiquetaActual += 10;
//        return etiqueta;
//    }
    public String generarCodigoIntermedio(String[] reglas, boolean optimizacion) {
        StringBuilder codigoIntermedio = new StringBuilder();
        int condicionesAbiertas = 0;
        List<String> bloqueCondicion = new ArrayList<>();
        boolean bloqueAbierto = false;
        for (String regla : reglas) {
            if (regla.startsWith("STF") || regla.startsWith("MIENTRAS") || regla.startsWith("CAMBIO") || regla.startsWith("HACER") || regla.contains("NSTF")) {
                if (regla.contains("NSTF")) {
                    condicionesAbiertas--;
                }
                bloqueAbierto = true;
                bloqueCondicion.add(regla);
                condicionesAbiertas++;
            } else {
                if (bloqueAbierto) {
                    bloqueCondicion.add(regla);
                    if (regla.contains("}")) {
                        condicionesAbiertas--;
                        if (regla.contains("NSTF")) {
                            if (condicionesAbiertas == 0) {
                                condicionesAbiertas--;
                                bloqueAbierto = false;
                                StringBuilder resultado2 = procesarBloque(bloqueCondicion, etiquetaActual2, optimizacion);
                                codigoIntermedio.append(resultado2).append("\n");
                                bloqueCondicion.clear();
                            }
                        } else {
                            if (condicionesAbiertas == 0) {
                                bloqueAbierto = false;
                                StringBuilder resultado2 = procesarBloque(bloqueCondicion, etiquetaActual2, optimizacion);
                                codigoIntermedio.append(resultado2).append("\n");
                                bloqueCondicion.clear();
                            }
                        }
                    }
                } else {
                    String resultado = codigoGeneral(regla, optimizacion);
//                    if (resultado.equals("ERROR DIVISION ENTRE CERO NO DEFINIDA")) {
//                        
//                    }
                    codigoIntermedio.append(resultado).append("\n");
                }
            }
        }
        return codigoIntermedio.toString();
    }

    public String codigoGeneral(String linea, boolean optimizacion) {
        String linea3 = linea.replaceAll(" ", "").replaceAll(";", "");
        String linea4 = "";
        System.out.println("==Evaluando linea: " + linea3);
        if (optimizacion) {
            System.out.println("ENTRAMOS!" + linea3);
            if (linea3.contains("+") || linea3.contains("-") || linea3.contains("*") || linea3.contains("/")) {
                for (int i = 0; i < linea3.length(); i++) {
                    if (!linea4.equals("")) {
                        linea3 = linea4;
                        linea4 = "";
                    }
                    if (linea3.charAt(i) == '0') {
                        System.out.println("LINEA A PROCESAR: " + linea3);
                        if (linea3.charAt(i - 1) == '+' || linea3.charAt(i - 1) == '-') {
                            System.out.println("ENTRE ACA");
                            for (int j = 0; j < linea3.length(); j++) {
                                if (j > i || j < i - 1) {
                                    linea4 += linea3.charAt(j);
                                    System.out.println(linea4);
                                }
                            }
                            for (int j = 0; j < linea4.length(); j++) {
                                if (linea4.charAt(j) == '0' && (linea4.charAt(j - 1) == '+' || linea4.charAt(j - 1) == '-')) {
                                    i = 0;
                                    System.out.println("SEGUNDA VUELTA");
                                    break;
                                } else {
                                    i = linea3.length();
                                    System.out.println("FINALIZA");
                                }
                            }
                        } else if (linea3.charAt(i - 1) == '/' || linea3.charAt(i - 1) == '*') {
                            if (linea3.charAt(i - 1) == '/') {
                                linea4 = "ERROR DIVISION ENTRE CERO NO DEFINIDA";
                                break;
                            }
                            System.out.println("ENTRE AQUI");
                            for (int j = i - 2; j >= 0; j--) {
                                System.out.println(linea3.charAt(j));
                                if (linea3.charAt(j) == '+' || linea3.charAt(j) == '-' || linea3.charAt(j) == '/' || linea3.charAt(j) == '*' || linea3.charAt(j) == '=') {
                                    for (int k = 0; k < linea3.length(); k++) {
                                        System.out.println("i: " + i + " k: " + k + " j: " + j);
                                        if (k > i || k < j) {//JHhjhjh
                                            linea4 += linea3.charAt(k);
                                            System.out.println("Linea4: " + linea4);
                                        }
                                    }
                                    j = -1;
                                    i = linea3.length();
                                }
                            }
                            System.out.println(linea4 + " NUEVA LINEA CODIGO INTERMEDIO");
                        }
                    }
                }
            }
        }
        String linea2 = linea.replaceAll(";", "").replaceAll(" ", "");
        if (optimizacion && !linea4.equals("")) {
            linea2 = linea4;
        }
        String[] tokens = linea2.split("(?=[()+\\-*/=])|(?<=[()+\\-*/=])");
        StringBuilder resultado = new StringBuilder();
        Stack<String> operadores = new Stack<>();
        Stack<String> temporales = new Stack<>();
        int contadorTemporal = 1;

        if (!linea.contains("+") && !linea.contains("-") && !linea.contains("*") && !linea.contains("/")) {
            return linea;
        } else {
            for (String token : tokens) {
                if (token.equals("(")) {
                    operadores.push(token);
                } else if (token.equals(")")) {
                    while (!operadores.isEmpty() && !operadores.peek().equals("(")) {
                        procesarOperador(operadores, temporales, resultado, contadorTemporal);
                        contadorTemporal++;
                    }
                    operadores.pop(); // Sacar el '(' de la pila
                } else if (esOperadorAritmetico(token)) {
                    while (!operadores.isEmpty() && prioridad(token) <= prioridad(operadores.peek())) {
                        procesarOperador(operadores, temporales, resultado, contadorTemporal);
                        contadorTemporal++;
                    }
                    operadores.push(token);
                } else {
                    temporales.push(token);
                }
            }

            while (!operadores.isEmpty()) {
                procesarOperador(operadores, temporales, resultado, contadorTemporal);
                contadorTemporal++;
            }

            resultado.append(tokens[0]).append(" = ").append(temporales.pop()).append(";");
            return resultado.toString();
        }
    }

    private void procesarOperador(Stack<String> operadores, Stack<String> temporales, StringBuilder resultado, int contadorTemporal) {
        String operador = operadores.pop();
        if (operador.equals("*") || operador.equals("/")) {
            String op2 = temporales.pop();
            String op1 = temporales.pop();
            String temporal = "temporal" + contadorTemporal;
            resultado.append(temporal).append(" = ").append(op1).append(" ").append(operador).append(" ").append(op2).append("\n");
            temporales.push(temporal);
        } else {
            String op2 = temporales.pop();
            String op1 = temporales.pop();
            String temporal = "temporal" + contadorTemporal;
            resultado.append(temporal).append(" = ").append(op1).append(" ").append(operador).append(" ").append(op2).append("\n");
            temporales.push(temporal);
        }
    }

    private int prioridad(String operador) {
        if (operador.equals("+") || operador.equals("-")) {
            return 1;
        } else if (operador.equals("*") || operador.equals("/")) {
            return 2;
        }
        return 0; // Prioridad para otros tokens (por ejemplo, paréntesis)
    }

    private boolean esOperadorAritmetico(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }
//    public String codigoGeneral(String linea) {
//        String[] tokens = linea.split("(?=[()+\\-*/=])|(?<=[()+\\-*/=])");
//        StringBuilder resultado = new StringBuilder();
//        Stack<String> operadores = new Stack<>();
//        Stack<String> temporales = new Stack<>();
//        int contadorTemporal = 1;
//
//        if (!linea.contains("+") && !linea.contains("-") && !linea.contains("*") && !linea.contains("/")) {
//            return linea; // Si no hay operadores aritméticos, devuelve la línea sin cambios
//        } else {
//            for (String token : tokens) {
//                if (esOperadorAritmetico(token)) {
//                    while (!operadores.isEmpty() && prioridad(token) <= prioridad(operadores.peek())) {
//                        String operador = operadores.pop();
//                        String op2 = temporales.pop();
//                        String op1 = temporales.pop();
//                        String temporal = "temporal" + contadorTemporal;
//                        contadorTemporal++;
//                        resultado.append(temporal).append(" = ").append(op1).append(" ").append(operador).append(" ").append(op2).append("\n");
//                        temporales.push(temporal);
//                    }
//                    operadores.push(token);
//                } else {
//                    temporales.push(token);
//                }
//            }
//            while (!operadores.isEmpty()) {
//                String operador = operadores.pop();
//                String op2 = temporales.pop();
//                String op1 = temporales.pop();
//                String temporal = "temporal" + contadorTemporal;
//                contadorTemporal++;
//                resultado.append(temporal).append(" = ").append(op1).append(" ").append(operador).append(" ").append(op2).append("\n");
//                temporales.push(temporal);
//            }
//            resultado.append(tokens[0]).append(" = ").append(temporales.pop()).append(";");
//        }
//        return resultado.toString();
//    }
//
//    public static int prioridad(String operador) {
//        if (operador.equals("+") || operador.equals("-")) {
//            return 1;
//        } else if (operador.equals("*") || operador.equals("/")) {
//            return 2;
//        } 
//        return 0;
//    }
//
//    private boolean esOperadorAritmetico(String token) {
//        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
//    }

    public String codigoSTF(String[] linea) {
        for (String linea1 : linea) {
            System.out.println("#######" + linea1);
        }
        return null;

    }

    public String codigoCND(String linea) {

        return null;

    }

    public String codigoMIENTRAS(String linea) {

        return null;

    }

    private StringBuilder procesarBloque(List<String> bloqueActual, int etiquetaActual, boolean optimizacion) {
        StringBuilder resultado = new StringBuilder();
//        for (int i = 0; i < bloqueActual.size(); i++) {
//            System.out.println("**" + bloqueActual.get(i));
//            if (i == bloqueActual.size() - 1) {
//                System.out.println("=============");
//            }
//        }
        String primeraLinea = bloqueActual.get(0);
        if (primeraLinea.startsWith("STF")) {
            int EVerdadera = etiquetaActual + 10;
            int EFalsa = etiquetaActual + 20;
            boolean nstf = false;
            for (int i = 1; i < bloqueActual.size(); i++) {
                String linea = bloqueActual.get(i);
                if (linea.contains("NSTF")) {
                    SSiguiente = etiquetaActual + 30;
                    etiquetaActual2 += 10;
                    nstf = true;
                }
            }
            etiquetaActual2 += 20;
            String nuevoSTF = primeraLinea.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\{", "");
            if (primeraLinea.contains("&&") || primeraLinea.contains("||")) {
                String patron = "\\((.*?)\\)";
                Pattern pattern = Pattern.compile(patron);
                Matcher matcher = pattern.matcher(primeraLinea);
                String parentesis = "";
                if (matcher.find()) {
                    parentesis = matcher.group(1);
                }
                System.out.println(parentesis);
                String[] partes = parentesis.split("\\|\\| | && ");
                String operadores = parentesis.replaceAll("[^|&]+", "");
                StringBuilder nuevaLinea = new StringBuilder();
                for (int i = 0; i < operadores.length(); i += 2) {
                    nuevaLinea.append(operadores.substring(i, Math.min(i + 2, operadores.length())));
                    if (i + 2 < operadores.length()) {
                        nuevaLinea.append(" ");
                    }
                }
                operadores = nuevaLinea.toString();
                String[] operadoresSeparados = operadores.split(" ");
                Etiquetas[] etiquetas = null;
                if (operadoresSeparados.length == 1) {
                    if (operadoresSeparados[0].equals("||")) {
                        resultado.append("STF ").append(partes[0]).append(" goto E").append(EVerdadera).append("\n");
                        resultado.append("goto E").append(EFalsa).append("\n");
                        resultado.append("E").append(EFalsa).append(":\n");
                        int EFalsa2 = etiquetaActual2 + 10;
                        EFalsa = EFalsa2;
                        etiquetaActual2 += 10;
                        resultado.append("STF ").append(partes[1]).append(" goto E").append(EVerdadera).append("\n");
                        resultado.append("goto E").append(EFalsa2).append("\n");
                        resultado.append("E").append(EVerdadera).append(":\n");
                    } else {
                        if (operadoresSeparados[0].equals("&&")) {
                            resultado.append("STF ").append(partes[0]).append(" goto E").append(EVerdadera).append("\n");
                            resultado.append("goto E").append(EFalsa).append("\n");
                            resultado.append("E").append(EVerdadera).append(":\n");
                            int EVerdadera2 = etiquetaActual2 + 10;
                            etiquetaActual2 += 10;
                            resultado.append("STF ").append(partes[1]).append(" goto E").append(EVerdadera2).append("\n");
                            resultado.append("goto E").append(EFalsa).append("\n");
                            resultado.append("E").append(EVerdadera2).append(":\n");
                        }
                    }
                } else {
                    int contador = 0;
                    etiquetas = new Etiquetas[operadoresSeparados.length + 1];
                    for (int i = operadoresSeparados.length - 1; i >= 0; i--) {
                        System.out.println(operadoresSeparados.length);
                        if (i == operadoresSeparados.length - 1) {
                            if (nstf) {
                                etiquetas[contador] = new Etiquetas("ifelse", EVerdadera, EFalsa, SSiguiente);
                                contador++;
                            } else {
                                etiquetas[contador] = new Etiquetas("if", EVerdadera, EFalsa);
                                contador++;
                            }
                            if (operadoresSeparados[i].equals("&&")) {
                                etiquetas[contador] = new Etiquetas("and", etiquetaActual2 + 10, EFalsa, EVerdadera, EFalsa);
                                etiquetaActual2 += 10;
                            } else if (operadoresSeparados[i].equals("||")) {
                                etiquetas[contador] = new Etiquetas("or", EVerdadera, etiquetaActual2 + 10, EVerdadera, EFalsa);
                                etiquetaActual2 += 10;
                            }
                        } else {
                            Etiquetas etiquetas2 = etiquetas[contador];
                            contador++;
                            if (operadoresSeparados[i].equals("&&")) {
                                etiquetas[contador] = new Etiquetas("and", etiquetaActual2 + 10, etiquetas2.getE1Falsa(), etiquetas2.getE1Verdadera(), etiquetas2.getE1Falsa());
                                etiquetaActual2 += 10;
                            } else if (operadoresSeparados[i].equals("||")) {
                                etiquetas[contador] = new Etiquetas("or", etiquetas2.getE1Verdadera(), etiquetaActual2 + 10, etiquetas2.getE1Verdadera(), etiquetas2.getE1Falsa());
                                etiquetaActual2 += 10;
                            }
                        }
                    }
                    for (int i = 0; i < etiquetas.length; i++) {
                        System.out.println("===================");
                        System.out.println("Tipo: " + etiquetas[i].getTipo());
                        System.out.println("EVerdadera: " + etiquetas[i].geteVerdadera());
                        System.out.println("EFalsa: " + etiquetas[i].geteFalsa());
                        System.out.println("ESiguiente: " + etiquetas[i].geteSiguiente());
                        System.out.println("E1Verdadera: " + etiquetas[i].getE1Verdadera());
                        System.out.println("E1Falsa: " + etiquetas[i].getE1Falsa());
                        System.out.println("E2Verdadera: " + etiquetas[i].getE2Verdadera());
                        System.out.println("E2Falsa: " + etiquetas[i].getE2Falsa());
                    }
                    int contador2 = operadoresSeparados.length;
                    for (int i = 0; i < operadoresSeparados.length; i++) {
                        if (!(operadoresSeparados.length % 2 == 0) && i == operadoresSeparados.length - 1) {
                            if (operadoresSeparados[i].equals("||")) {
                                resultado.append("STF").append(partes[i]).append(" goto E").append(etiquetas[contador2 - 1].getE1Verdadera()).append("\n");
                                resultado.append("goto E").append(etiquetas[contador2 - 1].getE1Falsa()).append("\n");
                                resultado.append("E").append(etiquetas[contador2 - 1].getE1Falsa()).append(":\n");
                                resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2 - 1].getE2Verdadera()).append("\n");
                                resultado.append("goto E").append(etiquetas[contador2 - 1].getE2Falsa()).append("\n");
                                resultado.append("E").append(etiquetas[contador2 - 2].geteVerdadera()).append(":\n");
                            } else {
                                resultado.append("STF ").append(partes[i]).append(" goto E").append(etiquetas[contador2].getE2Verdadera()).append("\n");
                                resultado.append("goto E").append(etiquetas[contador2 - 1].getE2Falsa()).append("\n");
                                resultado.append("E").append(etiquetas[contador2].getE2Verdadera()).append(":\n");
                                resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2 - 1].getE2Verdadera()).append("\n");
                                resultado.append("goto E").append(etiquetas[contador2 - 1].getE2Falsa()).append("\n");
                                resultado.append("E").append(etiquetas[contador2 - 2].geteVerdadera()).append(":\n");
                            }
                        } else if (i == operadoresSeparados.length - 1) {
                            if (operadoresSeparados[i].equals("||")) {
                                resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2].getE2Verdadera()).append("\n");
                                resultado.append("goto E").append(etiquetas[contador2].getE2Falsa()).append("\n");
                                resultado.append("E").append(etiquetas[contador2 - 1].geteVerdadera()).append(":\n");
                            } else if (operadoresSeparados[i].equals("&&")) {
                                resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2].getE2Verdadera()).append("\n");
                                resultado.append("goto E").append(etiquetas[contador2].getE2Falsa()).append("\n");
                                resultado.append("E").append(etiquetas[contador2 - 1].geteVerdadera()).append(":\n");
                            }
                        } else if (operadoresSeparados[i].equals("||")) {
                            resultado.append("STF ").append(partes[i]).append(" goto E").append(etiquetas[contador2].getE1Verdadera()).append("\n");
                            resultado.append("goto E").append(etiquetas[contador2].getE1Falsa()).append("\n");
                            resultado.append("E").append(etiquetas[contador2].getE1Falsa()).append(":\n");
                            resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2].getE2Verdadera()).append("\n");
                            resultado.append("goto E").append(etiquetas[contador2].getE2Falsa()).append("\n");
                            if (operadoresSeparados[i + 1].equals("||")) {
                                resultado.append("E").append(etiquetas[contador2].getE2Falsa()).append(":\n");
                            } else {
                                resultado.append("E").append(etiquetas[contador2 - 1].getE1Verdadera()).append(":\n");
                            }
                            if (operadoresSeparados.length % 2 != 0) {
                                i++;
                            }
                            contador2--;
                        } else if (operadoresSeparados[i].equals("&&")) {
                            resultado.append("STF ").append(partes[i]).append(" goto E").append(etiquetas[contador2].getE1Verdadera()).append("\n");
                            resultado.append("goto E").append(etiquetas[contador2].getE1Falsa()).append("\n");
                            resultado.append("E").append(etiquetas[contador2].getE1Verdadera()).append(":\n");
                            resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2].getE2Verdadera()).append("\n");
                            resultado.append("goto E").append(etiquetas[contador2].getE2Falsa()).append("\n");
                            if (operadoresSeparados[i + 1].equals("||")) {
                                resultado.append("E").append(etiquetas[contador2 - 1].getE1Falsa()).append(":\n");
                            } else {
                                resultado.append("E").append(etiquetas[contador2].getE2Verdadera()).append(":\n");
                            }

                            if (operadoresSeparados.length % 2 != 0) {
                                i++;
                            }
                            contador2--;
                        }
                    }
                }
            } else {
                resultado.append(nuevoSTF).append(" goto E").append(EVerdadera).append("\n");
                resultado.append("goto E").append(EFalsa).append("\n");
                resultado.append("E").append(EVerdadera).append(":").append("\n");
            }
            int condicionesAbiertas2 = 0;
            List<String> bloqueCondicion2 = new ArrayList<>();
            boolean bloqueAbierto2 = false;
            for (int i = 1; i < bloqueActual.size(); i++) {
                String linea = bloqueActual.get(i);

                if (linea.startsWith("STF") || linea.startsWith("MIENTRAS") || linea.startsWith("CAMBIO") || linea.startsWith("HACER") || linea.contains("NSTF")) {
                    if (linea.contains("NSTF")) {
                        if (condicionesAbiertas2 != 0) {
                            condicionesAbiertas2--;
                        }
                    }
                    bloqueAbierto2 = true;
                    bloqueCondicion2.add(linea);
                    condicionesAbiertas2++;
                } else {
                    if (bloqueAbierto2) {
                        bloqueCondicion2.add(linea);
                        if (linea.contains("}")) {
                            condicionesAbiertas2--;
                            if (linea.contains("NSTF")) {
                                if (condicionesAbiertas2 == 0) {
                                    bloqueAbierto2 = false;
                                    StringBuilder resultado2 = procesarBloque(bloqueCondicion2, EFalsa, optimizacion);
                                    resultado.append(resultado2).append("\n");
                                    bloqueCondicion2.clear();
                                }
                            } else {
                                if (condicionesAbiertas2 == 0) {
                                    bloqueAbierto2 = false;
                                    StringBuilder resultado2 = procesarBloque(bloqueCondicion2, EFalsa, optimizacion);
                                    resultado.append(resultado2).append("\n");
                                    bloqueCondicion2.clear();
                                }
                            }
                        }
                    } else {
                        if (linea.startsWith("}")) {
                            resultado.append("E").append(EFalsa).append(":").append("\n");
                        } else {
                            if (optimizacion) {
                                System.out.println("Entro Aqui Optimizacion STF: " + linea);
                            }
                            System.out.println("Entro linea: " + linea);
                            String resultado2 = codigoGeneral(linea, optimizacion);
                            resultado.append(resultado2).append("\n");
                        }
                    }
                }
            }
            return resultado;
        } else if (primeraLinea.startsWith("HACER")) {
            int SComienzo = etiquetaActual + 10;
            int EVerdadera = SComienzo;
            int EFalsa = etiquetaActual + 20;
            etiquetaActual2 += 20;
            resultado.append("E").append(SComienzo).append(":\n");
            int condicionesAbiertas2 = 0;
            List<String> bloqueCondicion2 = new ArrayList<>();
            boolean bloqueAbierto2 = false;
            for (int i = 1; i < bloqueActual.size(); i++) {
                String linea = bloqueActual.get(i);
                if (linea.startsWith("STF") || linea.startsWith("MIENTRAS") || linea.startsWith("CAMBIO") || linea.startsWith("HACER") || linea.contains("NSTF")) {
                    if (linea.contains("NSTF")) {
                        if (condicionesAbiertas2 != 0) {
                            condicionesAbiertas2--;
                        }
                    }
                    bloqueAbierto2 = true;
                    bloqueCondicion2.add(linea);
                    condicionesAbiertas2++;
                } else {
                    if (bloqueAbierto2) {
                        bloqueCondicion2.add(linea);
                        if (linea.contains("}")) {
                            condicionesAbiertas2--;
                            if (linea.contains("NSTF")) {
                                if (condicionesAbiertas2 == 0) {
                                    bloqueAbierto2 = false;
                                    StringBuilder resultado2 = procesarBloque(bloqueCondicion2, EFalsa, optimizacion);
                                    resultado.append(resultado2).append("\n");
                                    bloqueCondicion2.clear();
                                }
                            } else {
                                if (condicionesAbiertas2 == 0) {
                                    bloqueAbierto2 = false;
                                    StringBuilder resultado2 = procesarBloque(bloqueCondicion2, EFalsa, optimizacion);
                                    resultado.append(resultado2).append("\n");
                                    bloqueCondicion2.clear();
                                }
                            }
                        }
                    } else {
                        if (linea.startsWith("}")) {

                        } else {
                            String resultado2 = codigoGeneral(linea, optimizacion);
                            resultado.append(resultado2).append("\n");
                        }
                    }
                }
            }
            String patron2 = "\\((.*?)\\)";
            Pattern pattern2 = Pattern.compile(patron2);
            Matcher matcher2 = pattern2.matcher(bloqueActual.get(bloqueActual.size() - 1));
            String parentesis2 = "";
            if (matcher2.find()) {
                parentesis2 = matcher2.group(1); // El grupo 1 contiene el contenido entre paréntesis.                
            }
            if (parentesis2.matches(".*\\|\\|.*") || parentesis2.matches(".*&&.*")) {
//                EVerdadera = etiquetaActual2 + 10;
//                etiquetaActual2 += 10;
                System.out.println("ENTRO AQUI ¨********");
                String[] partes = parentesis2.split("\\|\\| | && ");
                String operadores = parentesis2.replaceAll("[^|&]+", "");
                StringBuilder nuevaLinea = new StringBuilder();
                for (int i = 0; i < operadores.length(); i += 2) {
                    nuevaLinea.append(operadores.substring(i, Math.min(i + 2, operadores.length())));
                    if (i + 2 < operadores.length()) {
                        nuevaLinea.append(" ");
                    }
                }
                operadores = nuevaLinea.toString();
                String[] operadoresSeparados = operadores.split(" ");
                Etiquetas[] etiquetas = null;
                if (operadoresSeparados.length == 1) {
                    if (operadoresSeparados[0].equals("||")) {
                        resultado.append("STF ").append(partes[0]).append(" goto E").append(EVerdadera).append("\n");
                        resultado.append("goto E").append(EFalsa).append("\n");
                        resultado.append("E").append(EFalsa).append(":\n");
                        int EFalsa2 = etiquetaActual2 + 10;
                        etiquetaActual2 += 10;
                        resultado.append("STF ").append(partes[1]).append(" goto E").append(EVerdadera).append("\n");
                        resultado.append("goto E").append(EFalsa2).append("\n");
                        resultado.append("E").append(EVerdadera).append(":\n");
                    } else {
                        if (operadoresSeparados[0].equals("&&")) {
                            resultado.append("STF ").append(partes[0]).append(" goto E").append(EVerdadera).append("\n");
                            resultado.append("goto E").append(EFalsa).append("\n");
                            resultado.append("E").append(EVerdadera).append(":\n");
                            int EVerdadera2 = etiquetaActual2 + 10;
                            etiquetaActual2 += 10;
                            resultado.append("STF ").append(partes[1]).append(" goto E").append(EVerdadera2).append("\n");
                            resultado.append("goto E").append(EFalsa).append("\n");
                            resultado.append("E").append(EVerdadera2).append(":\n");
                        }
                    }
                } else {
                    int contador = 0;
                    etiquetas = new Etiquetas[operadoresSeparados.length + 1];
                    for (int i = operadoresSeparados.length - 1; i >= 0; i--) {
                        System.out.println(operadoresSeparados.length + "**");
                        if (i == operadoresSeparados.length - 1) {
                            etiquetas[contador] = new Etiquetas("while", EVerdadera, EFalsa, SComienzo);
                            contador++;
                            if (operadoresSeparados[i].equals("&&")) {
                                etiquetas[contador] = new Etiquetas("and", etiquetaActual2 + 10, EFalsa, EVerdadera, EFalsa);
                                etiquetaActual2 += 10;
                            } else if (operadoresSeparados[i].equals("||")) {
                                etiquetas[contador] = new Etiquetas("or", EVerdadera, etiquetaActual2 + 10, EVerdadera, EFalsa);
                                etiquetaActual2 += 10;
                            }
                        } else {
                            Etiquetas etiquetas2 = etiquetas[contador];
                            contador++;
                            if (operadoresSeparados[i].equals("&&")) {
                                etiquetas[contador] = new Etiquetas("and", etiquetaActual2 + 10, etiquetas2.getE1Falsa(), etiquetas2.getE1Verdadera(), etiquetas2.getE1Falsa());
                                etiquetaActual2 += 10;
                            } else if (operadoresSeparados[i].equals("||")) {
                                etiquetas[contador] = new Etiquetas("or", etiquetas2.getE1Verdadera(), etiquetaActual2 + 10, etiquetas2.getE1Verdadera(), etiquetas2.getE1Falsa());
                                etiquetaActual2 += 10;
                            }
                        }
                    }
                    for (int i = 0; i < etiquetas.length; i++) {
                        System.out.println("===================");
                        System.out.println("Tipo: " + etiquetas[i].getTipo());
                        System.out.println("EVerdadera: " + etiquetas[i].geteVerdadera());
                        System.out.println("EFalsa: " + etiquetas[i].geteFalsa());
                        System.out.println("ESiguiente: " + etiquetas[i].geteSiguiente());
                        System.out.println("E1Verdadera: " + etiquetas[i].getE1Verdadera());
                        System.out.println("E1Falsa: " + etiquetas[i].getE1Falsa());
                        System.out.println("E2Verdadera: " + etiquetas[i].getE2Verdadera());
                        System.out.println("E2Falsa: " + etiquetas[i].getE2Falsa());
                    }
                    int contador2 = operadoresSeparados.length;
                    for (int i = 0; i < operadoresSeparados.length; i++) {
                        if (!(operadoresSeparados.length % 2 == 0) && i == operadoresSeparados.length - 1) {
                            if (i == operadoresSeparados.length - 1) {
                                if (operadoresSeparados[i].equals("||")) {
                                    resultado.append("STF").append(partes[i]).append(" goto E").append(etiquetas[contador2 - 1].getE1Verdadera()).append("\n");
                                    resultado.append("goto E").append(etiquetas[contador2 - 1].getE1Falsa()).append("\n");
                                    resultado.append("E").append(etiquetas[contador2 - 1].getE1Falsa()).append(":\n");
                                    resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2 - 1].getE2Verdadera()).append("\n");
                                    resultado.append("goto E").append(etiquetas[contador2 - 1].getE2Falsa()).append("\n");
                                    resultado.append("E").append(etiquetas[contador2 - 2].geteFalsa()).append(":\n");
                                } else {
                                    resultado.append("STF ").append(partes[i]).append(" goto E").append(etiquetas[contador2].getE2Verdadera()).append("\n");
                                    resultado.append("goto E").append(etiquetas[contador2 - 1].getE2Falsa()).append("\n");
                                    resultado.append("E").append(etiquetas[contador2].getE2Verdadera()).append(":\n");
                                    resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2 - 1].getE2Verdadera()).append("\n");
                                    resultado.append("goto E").append(etiquetas[contador2 - 1].getE2Falsa()).append("\n");
                                    resultado.append("E").append(etiquetas[contador2 - 2].geteFalsa()).append(":\n");
                                }
                            }
                        } else if (i == operadoresSeparados.length - 1) {
                            if (operadoresSeparados[i].equals("||")) {
                                resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2].getE2Verdadera()).append("\n");
                                resultado.append("goto E").append(etiquetas[contador2].getE2Falsa()).append("\n");
                                resultado.append("E").append(etiquetas[contador2 - 1].geteFalsa()).append(":\n");
                            } else if (operadoresSeparados[i].equals("&&")) {
                                resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2].getE2Verdadera()).append("\n");
                                resultado.append("goto E").append(etiquetas[contador2].getE2Falsa()).append("\n");
                                resultado.append("E").append(etiquetas[contador2 - 1].geteFalsa()).append(":\n");
                            }
                        } else if (operadoresSeparados[i].equals("||")) {
                            resultado.append("STF ").append(partes[i]).append(" goto E").append(etiquetas[contador2].getE1Verdadera()).append("\n");
                            resultado.append("goto E").append(etiquetas[contador2].getE1Falsa()).append("\n");
                            resultado.append("E").append(etiquetas[contador2].getE1Falsa()).append(":\n");
                            resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2].getE2Verdadera()).append("\n");
                            resultado.append("goto E").append(etiquetas[contador2].getE2Falsa()).append("\n");
                            if (operadoresSeparados[i + 1].equals("||")) {
                                resultado.append("E").append(etiquetas[contador2].getE2Falsa()).append(":\n");
                            } else {
                                resultado.append("E").append(etiquetas[contador2 - 1].getE1Verdadera()).append(":\n");
                            }
                            if (operadoresSeparados.length % 2 != 0) {
                                i++;
                            }
                            contador2--;
                        } else if (operadoresSeparados[i].equals("&&")) {
                            resultado.append("STF ").append(partes[i]).append(" goto E").append(etiquetas[contador2].getE1Verdadera()).append("\n");
                            resultado.append("goto E").append(etiquetas[contador2].getE1Falsa()).append("\n");
                            resultado.append("E").append(etiquetas[contador2].getE1Verdadera()).append(":\n");
                            resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2].getE2Verdadera()).append("\n");
                            resultado.append("goto E").append(etiquetas[contador2].getE2Falsa()).append("\n");
                            if (operadoresSeparados[i + 1].equals("||")) {
                                resultado.append("E").append(etiquetas[contador2 - 1].getE1Falsa()).append(":\n");
                            } else {
                                resultado.append("E").append(etiquetas[contador2].getE2Verdadera()).append(":\n");
                            }

                            if (operadoresSeparados.length % 2 != 0) {
                                i++;
                            }
                            contador2--;
                        }
                    }
                }
            } else {
                resultado.append("STF ").append(parentesis2).append(" goto E").append(EVerdadera).append("\n");
                resultado.append("goto E").append(EFalsa).append("\n");
                resultado.append("E").append(EFalsa).append(":\n");
            }
            return resultado;
        } else if (primeraLinea.startsWith("MIENTRAS")) {
            int SComienzo = etiquetaActual + 10;
            int EVerdadera = etiquetaActual + 20;
            int EFalsa = etiquetaActual + 30;
            etiquetaActual2 += 30;
            if (primeraLinea.contains("&&") || primeraLinea.contains("||")) {
                System.out.println("ENTRO AQUI");
                resultado.append("E").append(SComienzo).append(":\n");
                String patron = "\\((.*?)\\)";
                Pattern pattern = Pattern.compile(patron);
                Matcher matcher = pattern.matcher(primeraLinea);
                String parentesis = "";
                if (matcher.find()) {
                    parentesis = matcher.group(1);
                }
                System.out.println(parentesis);
                String[] partes = parentesis.split("\\|\\| | && ");
                String operadores = parentesis.replaceAll("[^|&]+", "");
                StringBuilder nuevaLinea = new StringBuilder();
                for (int i = 0; i < operadores.length(); i += 2) {
                    nuevaLinea.append(operadores.substring(i, Math.min(i + 2, operadores.length())));
                    if (i + 2 < operadores.length()) {
                        nuevaLinea.append(" ");
                    }
                }
                operadores = nuevaLinea.toString();
                String[] operadoresSeparados = operadores.split(" ");
                Etiquetas[] etiquetas = null;
                if (operadoresSeparados.length == 1) {
                    if (operadoresSeparados[0].equals("||")) {
                        resultado.append("STF ").append(partes[0]).append(" goto E").append(EVerdadera).append("\n");
                        resultado.append("goto E").append(EFalsa).append("\n");
                        resultado.append("E").append(EFalsa).append(":\n");
                        int EFalsa2 = etiquetaActual2 + 10;
                        etiquetaActual2 += 10;
                        resultado.append("STF ").append(partes[1]).append(" goto E").append(EVerdadera).append("\n");
                        resultado.append("goto E").append(EFalsa2).append("\n");
                        resultado.append("E").append(EVerdadera).append(":\n");
                    } else {
                        if (operadoresSeparados[0].equals("&&")) {
                            resultado.append("STF ").append(partes[0]).append(" goto E").append(EVerdadera).append("\n");
                            resultado.append("goto E").append(EFalsa).append("\n");
                            resultado.append("E").append(EVerdadera).append(":\n");
                            int EVerdadera2 = etiquetaActual2 + 10;
                            etiquetaActual2 += 10;
                            resultado.append("STF ").append(partes[1]).append(" goto E").append(EVerdadera2).append("\n");
                            resultado.append("goto E").append(EFalsa).append("\n");
                            resultado.append("E").append(EVerdadera2).append(":\n");
                        }
                    }
                } else {
                    int contador = 0;
                    etiquetas = new Etiquetas[operadoresSeparados.length + 1];
                    for (int i = operadoresSeparados.length - 1; i >= 0; i--) {
                        System.out.println(operadoresSeparados.length + "**");
                        if (i == operadoresSeparados.length - 1) {
                            etiquetas[contador] = new Etiquetas("while", EVerdadera, EFalsa, SComienzo);
                            contador++;
                            if (operadoresSeparados[i].equals("&&")) {
                                etiquetas[contador] = new Etiquetas("and", etiquetaActual2 + 10, EFalsa, EVerdadera, EFalsa);
                                etiquetaActual2 += 10;
                            } else if (operadoresSeparados[i].equals("||")) {
                                etiquetas[contador] = new Etiquetas("or", EVerdadera, etiquetaActual2 + 10, EVerdadera, EFalsa);
                                etiquetaActual2 += 10;
                            }
                        } else {
                            Etiquetas etiquetas2 = etiquetas[contador];
                            contador++;
                            if (operadoresSeparados[i].equals("&&")) {
                                etiquetas[contador] = new Etiquetas("and", etiquetaActual2 + 10, etiquetas2.getE1Falsa(), etiquetas2.getE1Verdadera(), etiquetas2.getE1Falsa());
                                etiquetaActual2 += 10;
                            } else if (operadoresSeparados[i].equals("||")) {
                                etiquetas[contador] = new Etiquetas("or", etiquetas2.getE1Verdadera(), etiquetaActual2 + 10, etiquetas2.getE1Verdadera(), etiquetas2.getE1Falsa());
                                etiquetaActual2 += 10;
                            }
                        }
                    }
                    for (int i = 0; i < etiquetas.length; i++) {
                        System.out.println("===================");
                        System.out.println("Tipo: " + etiquetas[i].getTipo());
                        System.out.println("EVerdadera: " + etiquetas[i].geteVerdadera());
                        System.out.println("EFalsa: " + etiquetas[i].geteFalsa());
                        System.out.println("ESiguiente: " + etiquetas[i].geteSiguiente());
                        System.out.println("E1Verdadera: " + etiquetas[i].getE1Verdadera());
                        System.out.println("E1Falsa: " + etiquetas[i].getE1Falsa());
                        System.out.println("E2Verdadera: " + etiquetas[i].getE2Verdadera());
                        System.out.println("E2Falsa: " + etiquetas[i].getE2Falsa());
                    }
                    int contador2 = operadoresSeparados.length;
                    for (int i = 0; i < operadoresSeparados.length; i++) {
                        if (!(operadoresSeparados.length % 2 == 0) && i == operadoresSeparados.length - 1) {
                            if (i == operadoresSeparados.length - 1) {
                                if (operadoresSeparados[i].equals("||")) {
                                    resultado.append("STF ").append(partes[i]).append(" goto E").append(etiquetas[contador2 - 1].getE1Verdadera()).append("\n");
                                    resultado.append("goto E").append(etiquetas[contador2 - 1].getE1Falsa()).append("\n");
                                    resultado.append("E").append(etiquetas[contador2 - 1].getE1Falsa()).append(":\n");
                                    resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2 - 1].getE2Verdadera()).append("\n");
                                    resultado.append("goto E").append(etiquetas[contador2 - 1].getE2Falsa()).append("\n");
                                    resultado.append("E").append(etiquetas[contador2 - 1].getE2Verdadera()).append(":\n");
                                } else {
                                    resultado.append("STF ").append(partes[i]).append(" goto E").append(etiquetas[contador2 - 1].getE1Verdadera()).append("\n");
                                    resultado.append("goto E").append(etiquetas[contador2 - 1].getE2Falsa()).append("\n");
                                    resultado.append("E").append(etiquetas[contador2].getE2Verdadera()).append(":\n");
                                    resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2 - 1].getE2Verdadera()).append("\n");
                                    resultado.append("goto E").append(etiquetas[contador2 - 1].getE2Falsa()).append("\n");
                                    resultado.append("E").append(etiquetas[contador2 - 1].getE2Verdadera()).append(":\n");
                                }
                            }
                        } else if (i == operadoresSeparados.length - 1) {
                            if (operadoresSeparados[i].equals("||")) {
                                resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2].getE2Verdadera()).append("\n");
                                resultado.append("goto E").append(etiquetas[contador2].getE2Falsa()).append("\n");
                                resultado.append("E").append(etiquetas[contador2 - 1].geteVerdadera()).append(":\n");
                            } else if (operadoresSeparados[i].equals("&&")) {
                                resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2].getE2Verdadera()).append("\n");
                                resultado.append("goto E").append(etiquetas[contador2].getE2Falsa()).append("\n");
                                resultado.append("E").append(etiquetas[contador2 - 1].geteVerdadera()).append(":\n");
                            }
                        } else if (operadoresSeparados[i].equals("||")) {
                            resultado.append("STF ").append(partes[i]).append(" goto E").append(etiquetas[contador2].getE1Verdadera()).append("\n");
                            resultado.append("goto E").append(etiquetas[contador2].getE1Falsa()).append("\n");
                            resultado.append("E").append(etiquetas[contador2].getE1Falsa()).append(":\n");
                            resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2].getE2Verdadera()).append("\n");
                            resultado.append("goto E").append(etiquetas[contador2].getE2Falsa()).append("\n");
                            if (operadoresSeparados[i + 1].equals("||")) {
                                resultado.append("E").append(etiquetas[contador2].getE2Falsa()).append(":\n");
                            } else {
                                resultado.append("E").append(etiquetas[contador2 - 1].getE1Verdadera()).append(":\n");
                            }
                            if (operadoresSeparados.length % 2 != 0) {
                                i++;
                            }
                            contador2--;
                        } else if (operadoresSeparados[i].equals("&&")) {
                            resultado.append("STF ").append(partes[i]).append(" goto E").append(etiquetas[contador2].getE1Verdadera()).append("\n");
                            resultado.append("goto E").append(etiquetas[contador2].getE1Falsa()).append("\n");
                            resultado.append("E").append(etiquetas[contador2].getE1Verdadera()).append(":\n");
                            resultado.append("STF ").append(partes[i + 1]).append(" goto E").append(etiquetas[contador2].getE2Verdadera()).append("\n");
                            resultado.append("goto E").append(etiquetas[contador2].getE2Falsa()).append("\n");
                            if (operadoresSeparados[i + 1].equals("||")) {
                                resultado.append("E").append(etiquetas[contador2 - 1].getE1Falsa()).append(":\n");
                            } else {
                                resultado.append("E").append(etiquetas[contador2].getE2Verdadera()).append(":\n");
                            }
                            if (operadoresSeparados.length % 2 != 0) {
                                i++;
                            }
                            contador2--;
                        }
                    }
                }
            } else {
                String patron = "\\((.*?)\\)";
                Pattern pattern = Pattern.compile(patron);
                Matcher matcher = pattern.matcher(primeraLinea);
                String parentesis = "";
                if (matcher.find()) {
                    parentesis = matcher.group(1); // El grupo 1 contiene el contenido entre paréntesis.
                }
                resultado.append("E").append(SComienzo).append(":\n");
                resultado.append("STF ").append(parentesis).append(" goto E").append(EVerdadera).append("\n");
                resultado.append("goto E").append(EFalsa).append("\n");
                resultado.append("E").append(EVerdadera).append(":\n");
            }
            int condicionesAbiertas2 = 0;
            List<String> bloqueCondicion2 = new ArrayList<>();
            boolean bloqueAbierto2 = false;
            for (int i = 1; i < bloqueActual.size(); i++) {
                String linea = bloqueActual.get(i);

                if (linea.startsWith("STF") || linea.startsWith("MIENTRAS") || linea.startsWith("CAMBIO") || linea.startsWith("HACER") || linea.contains("NSTF")) {
                    if (linea.contains("NSTF")) {
                        if (condicionesAbiertas2 != 0) {
                            condicionesAbiertas2--;
                        }
                    }
                    bloqueAbierto2 = true;
                    bloqueCondicion2.add(linea);
                    condicionesAbiertas2++;
                } else {
                    if (bloqueAbierto2) {
                        bloqueCondicion2.add(linea);
                        if (linea.contains("}")) {
                            condicionesAbiertas2--;
                            if (linea.contains("NSTF")) {
                                if (condicionesAbiertas2 == 0) {
                                    bloqueAbierto2 = false;
                                    StringBuilder resultado2 = procesarBloque(bloqueCondicion2, EFalsa, optimizacion);
                                    resultado.append(resultado2).append("\n");
                                    bloqueCondicion2.clear();
                                }
                            } else {
                                if (condicionesAbiertas2 == 0) {
                                    bloqueAbierto2 = false;
                                    StringBuilder resultado2 = procesarBloque(bloqueCondicion2, EFalsa, optimizacion);
                                    resultado.append(resultado2).append("\n");
                                    bloqueCondicion2.clear();
                                }
                            }
                        }
                    } else {
                        if (linea.startsWith("}")) {
                            resultado.append("goto E").append(SComienzo).append(":").append("\n");
                            resultado.append("E").append(EFalsa).append(":").append("\n");
                        } else {
                            String resultado2 = codigoGeneral(linea, optimizacion);
                            resultado.append(resultado2).append("\n");
                        }
                    }
                }
            }
            return resultado;
        } else if (primeraLinea.startsWith("CAMBIO")) {
            int EFinal = etiquetaActual + 10;
            int EVerdadera;
            int EFalsa = 0;
            etiquetaActual2 += 10;
            String patron = "\\((.*?)\\)";
            Pattern pattern = Pattern.compile(patron);
            Matcher matcher = pattern.matcher(primeraLinea);
            String parentesis = "";
            if (matcher.find()) {
                parentesis = matcher.group(1); // El grupo 1 contiene el contenido entre paréntesis.
            }
            int condicionesAbiertas2 = 0;
            List<String> bloqueCondicion2 = new ArrayList<>();
            boolean bloqueAbierto2 = false;
            for (int i = 1; i < bloqueActual.size(); i++) {
                String linea = bloqueActual.get(i);
                if (linea.startsWith("STF") || linea.startsWith("MIENTRAS") || linea.startsWith("CAMBIO") || linea.startsWith("HACER") || linea.contains("NSTF")) {
                    if (linea.contains("NSTF")) {
                        if (condicionesAbiertas2 != 0) {
                            condicionesAbiertas2--;
                        }
                    }
                    bloqueAbierto2 = true;
                    bloqueCondicion2.add(linea);
                    condicionesAbiertas2++;
                } else {
                    if (bloqueAbierto2) {
                        bloqueCondicion2.add(linea);
                        if (linea.contains("}")) {
                            condicionesAbiertas2--;
                            if (linea.contains("NSTF")) {
                                if (condicionesAbiertas2 == 0) {
                                    bloqueAbierto2 = false;
                                    StringBuilder resultado2 = procesarBloque(bloqueCondicion2, EFalsa, optimizacion);
                                    resultado.append(resultado2).append("\n");
                                    bloqueCondicion2.clear();
                                }
                            } else {
                                if (condicionesAbiertas2 == 0) {
                                    bloqueAbierto2 = false;
                                    StringBuilder resultado2 = procesarBloque(bloqueCondicion2, EFalsa, optimizacion);
                                    resultado.append(resultado2).append("\n");
                                    bloqueCondicion2.clear();
                                }
                            }
                        }
                    } else {
                        if (linea.startsWith("}")) { //Ultima linea
                            resultado.append("E").append(EFinal).append(":").append("\n");
                        } else {
                            if (linea.startsWith("CASO")) {
                                EVerdadera = etiquetaActual2 + 10;
                                EFalsa = etiquetaActual2 + 20;
                                etiquetaActual2 += 20;
                                Pattern patron2 = Pattern.compile("CASO\\s+([^:]+):");
                                Matcher matcher2 = patron2.matcher(linea);
                                String valorExtraido = "";
                                if (matcher2.find()) {
                                    valorExtraido = matcher2.group(1);
                                }
                                resultado.append("STF ").append(parentesis).append("==").append(valorExtraido).append(" goto E").append(EVerdadera).append("\n");
                                resultado.append("goto E").append(EFalsa).append("\n");
                                resultado.append("E").append(EVerdadera).append(":\n");
                            } else {
                                if (linea.startsWith("DESCANSO")) {
                                    resultado.append("goto E").append(EFinal).append("\n");
                                    resultado.append("E").append(EFalsa).append(":\n");
                                } else {
                                    if (linea.startsWith("PREDET")) {
                                    } else {
                                        String resultado2 = codigoGeneral(linea, optimizacion);
                                        resultado.append(resultado2).append("\n");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return resultado;
        } else if (primeraLinea.contains("NSTF")) {
            resultado.append("goto E").append(SSiguiente).append("\n");
            resultado.append("E").append(etiquetaActual).append(":").append("\n");
            int condicionesAbiertas3 = 0;
            List<String> bloqueCondicion3 = new ArrayList<>();
            boolean bloqueAbierto3 = false;

            for (int i = 1; i < bloqueActual.size(); i++) {
                String linea = bloqueActual.get(i);

                if (linea.startsWith("STF") || linea.startsWith("MIENTRAS") || linea.startsWith("CAMBIO") || linea.startsWith("HACER")) {
                    if (linea.contains("NSTF")) {
                        condicionesAbiertas3--;
                    }
                    bloqueAbierto3 = true;
                    bloqueCondicion3.add(linea);
                    condicionesAbiertas3++;
                } else {
                    if (bloqueAbierto3) {
                        bloqueCondicion3.add(linea);
                        if (linea.contains("}")) {
                            condicionesAbiertas3--;
                            if (condicionesAbiertas3 == 0) {
                                bloqueAbierto3 = false;
                                StringBuilder resultado3 = procesarBloque(bloqueCondicion3, SSiguiente, optimizacion);
                                resultado.append(resultado3).append("\n");
                                bloqueCondicion3.clear();
                            }
                        }
                    } else {
                        if (linea.startsWith("}")) {
                            resultado.append("E").append(SSiguiente).append(":").append("\n");
                        } else {
                            String resultado3 = codigoGeneral(linea, optimizacion);
                            resultado.append(resultado3).append("\n");
                        }
                    }
                }
            }

            return resultado;
        }
        return resultado;
    }
}
