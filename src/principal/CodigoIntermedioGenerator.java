/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

public class CodigoIntermedioGenerator {

    private int temporalCount = 1;
    private int etiquetaActual = 10;  // Empieza en 10 y aumenta según sea necesario

    // Función para generar una nueva etiqueta
    private String nuevaEtiqueta() {
        String etiqueta = "E" + etiquetaActual;
        etiquetaActual += 10;
        return etiqueta;
    }

    public String generarCodigoIntermedio(String[] reglas) {
        StringBuilder codigoIntermedio = new StringBuilder();
        int condicionesAbiertas = 0;
        List<String> bloqueCondicion = new ArrayList<>();
        boolean bloqueAbierto = false;

        for (String regla : reglas) {
            if (regla.startsWith("STF") || regla.startsWith("MIENTRAS") || regla.startsWith("CND")) {
                bloqueAbierto = true;
                bloqueCondicion.add(regla);
                condicionesAbiertas++;
            } else {
                if (bloqueAbierto) {
                    bloqueCondicion.add(regla);
                    if (regla.contains("}")) {
                        condicionesAbiertas--;
                        if (condicionesAbiertas == 0) {
                            bloqueAbierto = false;
                            String resultado = procesarBloque(bloqueCondicion);
                            codigoIntermedio.append(resultado).append("\n");
                            bloqueCondicion.clear();
                        }
                    }
                } else {
                    String resultado = codigoGeneral(regla);
                    codigoIntermedio.append(resultado).append("\n");
                }
            }
        }
        return codigoIntermedio.toString();
    }

//    // Función para generar código intermedio
//    public String generarCodigoIntermedio(String[] reglas) {
//        StringBuilder codigoIntermedio = new StringBuilder();
//        int condicionesAbiertas = 0;
//        List<String> bloqueCondicion = new ArrayList<>();
//        Stack<List<String>> bloques = new Stack<>();
//        List<String> bloqueActual = new ArrayList<>();
//
//        for (String regla : reglas) {
//            String resultado = "";
//            if (regla.startsWith("STF") || regla.startsWith("MIENTRAS") || regla.startsWith("CND")) {
//                bloqueActual.add(regla);
//                bloques.push(bloqueActual);
//                bloqueActual = new ArrayList<>();
//            } else {
//                bloqueActual.add(regla);
//                if (regla.contains("}")) {
//                    resultado = procesarBloque(bloqueActual);
//                    bloqueActual = bloques.pop();
//                    bloqueActual.add(resultado);
//                }
//            }
//        }
//
//        for (String regla : reglas) {
//            String resultado = "";
//            if (regla.startsWith("STF")) {
//                bloqueCondicion.add(regla);
//                condicionesAbiertas++;
//            } else {
//                if (condicionesAbiertas > 0) {
//                    bloqueCondicion.add(regla);
//                    if (regla.contains("}")) {
//                        condicionesAbiertas--;
//                        if (condicionesAbiertas == 0) {
//                            resultado = codigoSTF(bloqueCondicion.toArray(new String[0]));
//                            codigoIntermedio.append(resultado).append("\n");
//                            bloqueCondicion.clear();
//                        } else if (regla.contains("{")) {
//                            condicionesAbiertas++;
//                        } else {
//                            resultado = codigoGeneral(regla);
//                            codigoIntermedio.append(resultado).append("\n");
//                        }
//                    }
//                    int x = 0;
//                }
//            }
//            if (regla.startsWith("MIENTRAS")) {
//                resultado = codigoMIENTRAS(regla);
//                codigoIntermedio.append(resultado).append("\n");
//            } else if (regla.startsWith("CND")) {
//                resultado = codigoCND(regla);
//                codigoIntermedio.append(resultado).append("\n");
//            } else {
//                resultado = codigoGeneral(regla);
//                codigoIntermedio.append(resultado).append("\n");
//            }
//        }
//        return codigoIntermedio.toString();
//    }
    public String codigoGeneral(String linea) {
        String[] tokens = linea.split("(?=[+\\-*/=])|(?<=[+\\-*/=])");
        StringBuilder resultado = new StringBuilder();
        Stack<String> operadores = new Stack<>();
        Stack<String> temporales = new Stack<>();
        int contadorTemporal = 1;

        if (!linea.contains("+") && !linea.contains("-") && !linea.contains("*") && !linea.contains("/")) {
            //            System.out.println("**" + linea);
            return linea; // Si no hay operadores aritméticos, devuelve la línea sin cambios
        } else {
            for (String token : tokens) {
                if (esOperadorAritmetico(token)) {
                    while (!operadores.isEmpty() && prioridad(token) <= prioridad(operadores.peek())) {
                        String operador = operadores.pop();
                        String op2 = temporales.pop();
                        String op1 = temporales.pop();
                        String temporal = "temporal" + contadorTemporal;
                        contadorTemporal++;
//                        System.out.println(temporal + " = " + op1 + " " + operador + " " + op2 + ";");
                        resultado.append(temporal).append(" = ").append(op1).append(" ").append(operador).append(" ").append(op2).append(";").append("\n");
                        temporales.push(temporal);
                    }
                    operadores.push(token);
                } else {
                    temporales.push(token);
                }
            }
            while (!operadores.isEmpty()) {
                String operador = operadores.pop();
                String op2 = temporales.pop();
                String op1 = temporales.pop();
                String temporal = "temporal" + contadorTemporal;
                contadorTemporal++;
//                System.out.println(temporal + " = " + op1 + " " + operador + " " + op2 + ";");
                temporales.push(temporal);
                resultado.append(temporal).append(" = ").append(op1).append(" ").append(operador).append(" ").append(op2).append(";").append("\n");
            }
//            System.out.println("operacion = " + temporales.pop() + ";");
            resultado.append(tokens[0]).append(" = ").append(temporales.pop()).append(";");
        }
        return resultado.toString();
    }

    public static int prioridad(String operador) {
        if (operador.equals("+") || operador.equals("-")) {
            return 1;
        } else if (operador.equals("*") || operador.equals("/")) {
            return 2;
        }
        return 0;
    }

    private boolean esOperadorAritmetico(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    public String codigoSTF(String[] linea) {
        for (int i = 0; i < linea.length; i++) {
            System.out.println(linea[i]);
        }
        return null;

    }

    public String codigoCND(String linea) {

        return null;

    }

    public String codigoMIENTRAS(String linea) {

        return null;

    }

    private String procesarBloque(List<String> bloqueActual) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
