/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodigoIntermedioGenerator {

    private int temporalCount = 1;
    int etiquetaActual2 = 0;  // Empieza en 0 y aumenta de 10 en 10

    // Función para generar una nueva etiqueta
//    private String nuevaEtiqueta() {
//        String etiqueta = "E" + etiquetaActual;
//        etiquetaActual += 10;
//        return etiqueta;
//    }
    public String generarCodigoIntermedio(String[] reglas) {
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
                                StringBuilder resultado2 = procesarBloque(bloqueCondicion, etiquetaActual2);
                                codigoIntermedio.append(resultado2).append("\n");
                                bloqueCondicion.clear();
                            }
                        } else {
                            if (condicionesAbiertas == 0) {
                                bloqueAbierto = false;
                                StringBuilder resultado2 = procesarBloque(bloqueCondicion, etiquetaActual2);
                                codigoIntermedio.append(resultado2).append("\n");
                                bloqueCondicion.clear();
                            }
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

    private StringBuilder procesarBloque(List<String> bloqueActual, int etiquetaActual) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < bloqueActual.size(); i++) {
            System.out.println("**" + bloqueActual.get(i));
            if (i == bloqueActual.size() - 1) {
                System.out.println("=============");
            }
        }
        String primeraLinea = bloqueActual.get(0);

        if (primeraLinea.startsWith("STF")) {
            int EVerdadera = etiquetaActual + 10;
            int EFalsa = etiquetaActual + 20;
            etiquetaActual2 += 20;
            String nuevoSTF = primeraLinea.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\{", "");
            resultado.append(nuevoSTF).append(" goto E").append(EVerdadera).append("\n");
            resultado.append("goto E").append(EFalsa).append("\n");
            resultado.append("E").append(EVerdadera).append(":").append("\n");
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
                                    StringBuilder resultado2 = procesarBloque(bloqueCondicion2, EFalsa);
                                    resultado.append(resultado2).append("\n");
                                    bloqueCondicion2.clear();
                                }
                            } else {
                                if (condicionesAbiertas2 == 0) {
                                    bloqueAbierto2 = false;
                                    StringBuilder resultado2 = procesarBloque(bloqueCondicion2, EFalsa);
                                    resultado.append(resultado2).append("\n");
                                    bloqueCondicion2.clear();
                                }
                            }
                        }
                    } else {
                        if (linea.startsWith("}")) {
                            resultado.append("E").append(EFalsa).append(":").append("\n");
                        } else {
                            String resultado2 = codigoGeneral(linea);
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
                                    StringBuilder resultado2 = procesarBloque(bloqueCondicion2, EFalsa);
                                    resultado.append(resultado2).append("\n");
                                    bloqueCondicion2.clear();
                                }
                            } else {
                                if (condicionesAbiertas2 == 0) {
                                    bloqueAbierto2 = false;
                                    StringBuilder resultado2 = procesarBloque(bloqueCondicion2, EFalsa);
                                    resultado.append(resultado2).append("\n");
                                    bloqueCondicion2.clear();
                                }
                            }
                        }
                    } else {
                        if (linea.startsWith("}")) {

                        } else {
                            String resultado2 = codigoGeneral(linea);
                            resultado.append(resultado2).append("\n");
                        }
                    }
                }
            }
            String patron = "\\((.*?)\\)";
            Pattern pattern = Pattern.compile(patron);
            Matcher matcher = pattern.matcher(bloqueActual.get(bloqueActual.size() - 1));
            String parentesis = "";
            if (matcher.find()) {
                parentesis = matcher.group(1); // El grupo 1 contiene el contenido entre paréntesis.
            }
            resultado.append("STF ").append(parentesis).append(" goto E").append(EVerdadera).append("\n");
            resultado.append("goto E").append(EFalsa).append("\n");
            resultado.append("E").append(EFalsa).append(":\n");
            return resultado;
        } else if (primeraLinea.startsWith("MIENTRAS")) {
            int SComienzo = etiquetaActual + 10;
            int EVerdadera = etiquetaActual + 20;
            int EFalsa = etiquetaActual + 30;
            etiquetaActual2 += 30;
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
                                    StringBuilder resultado2 = procesarBloque(bloqueCondicion2, EFalsa);
                                    resultado.append(resultado2).append("\n");
                                    bloqueCondicion2.clear();
                                }
                            } else {
                                if (condicionesAbiertas2 == 0) {
                                    bloqueAbierto2 = false;
                                    StringBuilder resultado2 = procesarBloque(bloqueCondicion2, EFalsa);
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
                            String resultado2 = codigoGeneral(linea);
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
                                    StringBuilder resultado2 = procesarBloque(bloqueCondicion2, EFalsa);
                                    resultado.append(resultado2).append("\n");
                                    bloqueCondicion2.clear();
                                }
                            } else {
                                if (condicionesAbiertas2 == 0) {
                                    bloqueAbierto2 = false;
                                    StringBuilder resultado2 = procesarBloque(bloqueCondicion2, EFalsa);
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
                                        String resultado2 = codigoGeneral(linea);
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
            int SSiguiente = etiquetaActual + 10;
            etiquetaActual2 += 10;

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
                                StringBuilder resultado3 = procesarBloque(bloqueCondicion3, SSiguiente);
                                resultado.append(resultado3).append("\n");
                                bloqueCondicion3.clear();
                            }
                        }
                    } else {
                        if (linea.startsWith("}")) {
                            resultado.append("E").append(SSiguiente).append(":").append("\n");
                        } else {
                            String resultado3 = codigoGeneral(linea);
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
