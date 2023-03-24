package principal;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Semantico {

    /**
     * @return the resultadoFinal
     */
    public double getResultadoFinal() {
        return resultadoFinal;
    }

    /**
     * @param resultadoFinal the resultadoFinal to set
     */
    public void setResultadoFinal(double resultadoFinal) {
        this.resultadoFinal = resultadoFinal;
    }

    /**
     * @param valorFila the valorFila to set
     */
    public void setValorFila(String valorFila) {
        this.valorFila = valorFila;
    }

    /**
     * @return the valorFila
     */
    public String getValorFila() {
        return valorFila;
    }
    
    private String valorFila="";
    private double resultadoFinal=0;

    private DefaultTableModel modeloTabla = new DefaultTableModel();

    public Semantico() {
        modeloTabla.addColumn("Operador");
        modeloTabla.addColumn("Valor 1");
        modeloTabla.addColumn("Valor 2");
        modeloTabla.addColumn("Temp");
        modeloTabla.addColumn("Resultado");
    }

    StringBuilder postfix;
    Lexico objLexico = new Lexico();
    String[] operadores
            = {
                "-", "+", "*", "/", "^"
            };
    int contExp = 1, contTemp;
    ArrayList<String[]> listaCuadru = new ArrayList<>();

    Stack< String> entrada = new Stack<>();
    Stack< String> operTemp = new Stack<>();
    Stack< String> salida = new Stack<>();
    Codi dicCuadru;
    ArrayList<Codi> listaDicCuadru = new ArrayList<>();
    int asig[][]
            = {
                {
                    1, -1, -1, -1, -1
                },
                {
                    -1, 1, -1, -1, -1
                },
                {
                    -1, -1, 1, -1, -1
                },
                {
                    -1, -1, -1, 1, -1
                },
                {
                    -1, -1, -1, -1, 1
                }
            };
    int operAri[][]
            = {
                {
                    1, -1, -1, -1, -1
                },
                {
                    -1, -1, -1, -1, -1
                },
                {
                    -1, -1, -1, -1, -1
                },
                {
                    -1, -1, 1, -1, -1
                },
                {
                    -1, -1, 1, -1, -1
                }
            };
    int operLog[][]
            //Mayor-Menor-MayorQ-MenorQ-Igual-Diferente
            = {
                {
                    1, -1, 1, -1, -1
                },
                {
                    1, -1, 1, -1, -1
                },
                {
                    1, -1, 1, -1, -1
                },
                {
                    1, -1, 1, -1, -1
                },
                {
                    1, 1, 1, 1, 1
                },
                {
                    1, 1, 1, 1, 1
                }
            };

    public String conversionNum(String s) {
        switch (s) {
            case "NUM":
                return "50";
            case "CAD":
                return "51";
            case "VAR":
                return "52";
            case "CHAR":
                return "53";
            case "BOOL":
                return "54";
            case "MET":
                return "60";
            default:
                return "No valido";
        }
    }

    public String conversionString(String s) {
        switch (s) {
            case "50":
                return "NUM";
            case "51":
                return "CAD";
            case "52":
                return "VAR";
            case "53":
                return "CHAR";
            case "54":
                return "BOOL";
            default:
                return "No valido";
        }
    }

    public boolean operCompatibles(String fila, String colum) {
        if (fila.equals("54") && (colum.equals("17") || colum.equals("18"))) {
            return true;
        }
        int f = Integer.parseInt(fila) - 50;
        int c = Integer.parseInt(colum) - 50;
        return asig[f][c] == 1;
    }

    public boolean operLogCompatibles(String fila, String op, String colum) {
        if (fila.equals("50") && colum.equals("50")) {
            return true;
        } else if (fila.equals(colum)) {
            if (op.equals("==") || op.equals("!")) {
                return true;
            }
        }
        return false;
//        int f = Integer.parseInt(fila) - 50;
//        int c = Integer.parseInt(colum) - 50;
//        return operLog[f][c] == 1;
    }

    public String calcular(String expresion, JTabbedPane tabPanel, ArrayList<String[]> arrlist, String var) throws Exception {
        listaCuadru.clear();
        listaDicCuadru.clear();
        contTemp = 1;
        JPanel panel = new JPanel();
        JScrollPane scroll = new JScrollPane();
        JTable tabla = new JTable();
        JLabel label = new JLabel();
        JLabel labelPost = new JLabel();
        label.setText("Expresión Infija: " + expresion);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setEnabled(false);
        transExpresion(expresion);
        labelPost.setText("Expresión Postfija: " + salida.toString().replaceAll("[\\]\\[,]", ""));
        Stack stack = reversa(salida);
        int pri = 4;
        do {
            stack = reversa(calOrden(stack, pri, arrlist));
            pri--;
        } while (stack.size() > 1);
        String res;
        res = (String) stack.pop();
        for (int i = 0; i < listaDicCuadru.size(); i++) {
            if (listaDicCuadru.get(i).getNomTemp() == res) {
                res = listaDicCuadru.get(i).getValor();
                break;
            }
        }
        String vecCuadruF[]
                = {
                    "", "", "", var + " = " + res
                };
        listaCuadru.add(vecCuadruF);
        tabla.setModel(new javax.swing.table.DefaultTableModel(
                arraylistToArray(),
                new String[]{
                    "Operando 1", "Operando 2", "Operador", "Resultado"
                }
        ));
        tabla.setPreferredScrollableViewportSize(new Dimension(tabPanel.getWidth() - 25, tabPanel.getHeight() - 125));
        scroll.setViewportView(tabla);
        panel.add(label);
        panel.add(labelPost);
        panel.add(scroll);
        panel.setSize(50, 50);
        tabPanel.addTab("Expresión " + contExp, panel);
        contExp++;
        return res;
    }

    private String[][] arraylistToArray() {
        String arr[][] = new String[listaCuadru.size()][4];
        for (int i = 0; i < listaCuadru.size(); i++) {
            for (int j = 0; j < 4; j++) {
                arr[i][j] = listaCuadru.get(i)[j];
            }
        }
        return arr;
    }

    private Stack calOrden(Stack stack, int prioridad, ArrayList<String[]> tablaVars) {
        String cuadru[];
        String result;
        Stack stack1 = new Stack();
        while (!stack.isEmpty()) {
            String value = (String) stack.pop();
            if (!esOperador(value)) {
                stack1.push(value);
            } else {
                if (pref(value) == prioridad) {
                    String value1 = (String) stack1.pop();
                    String value2 = (String) stack1.pop();
                    cuadru = new String[4];
                    cuadru[0] = value2;
                    cuadru[1] = value1;
                    cuadru[2] = value;
                    cuadru[3] = "Temp" + contTemp;
                    try {
                        if (value1.substring(0, 4).equals("Temp")) {
                            for (int i = 0; i < listaDicCuadru.size(); i++) {
                                if (listaDicCuadru.get(i).getNomTemp() == value1) {
                                    value1 = listaDicCuadru.get(i).getValor();
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                    try {
                        if (value2.substring(0, 4).equals("Temp")) {
                            for (int i = 0; i < listaDicCuadru.size(); i++) {
                                if (listaDicCuadru.get(i).getNomTemp() == value2) {
                                    value2 = listaDicCuadru.get(i).getValor();
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                    if (objLexico.Etiquetar(value1).numero == 50) {
                        for (int i = 0; i < tablaVars.size(); i++) {
                            if (tablaVars.get(i)[2].equals(value1)) {
                                value1 = tablaVars.get(i)[3];
                                break;
                            }
                        }
                    }
                    if (objLexico.Etiquetar(value2).numero == 50) {
                        for (int i = 0; i < tablaVars.size(); i++) {
                            String a = tablaVars.get(i)[2];
                            if (tablaVars.get(i)[2].equals(value2)) {
                                value2 = tablaVars.get(i)[3];
                                break;
                            }
                        }
                    }
                    try {
                        result = cal(value1, value2, value);
                        dicCuadru = new Codi("Temp" + contTemp, result);
                        stack1.push(String.valueOf(dicCuadru.getNomTemp()));
                        contTemp++;
                        listaDicCuadru.add(dicCuadru);
                    } catch (Exception e) {
                        stack1.clear();
                        stack1.push("Incompatible");
                        return stack1;
                    }
                    listaCuadru.add(cuadru);
                } else {
                    stack1.push(value);
                }
            }
        }

        return stack1;
    }

    private Stack reversa(Stack s) throws Exception {
        Stack result = new Stack();
        while (!s.isEmpty()) {
            result.push(s.pop());
        }
        return result;
    }

    private boolean esOperador(String a) {
        for (String c : operadores) {
            if (c.equals(a)) {
                return true;
            }
        }
        return false;
    }

    private String cal(String a, String b, String oper) {
        Lexico objLex = new Lexico();
        if (objLex.Etiquetar(a).numero == 50 && objLex.Etiquetar(b).numero == 50) {
            int tempValue1Int = Integer.parseInt(a);
            int tempValue2Int = Integer.parseInt(b);
            if (oper.equals("+")) {
                return String.valueOf(tempValue1Int + tempValue2Int);
            }
            if (oper.equals("-")) {
                return String.valueOf(tempValue2Int - tempValue1Int);
            }
            if (oper.equals("*")) {
                return String.valueOf(tempValue1Int * tempValue2Int);
            }
            if (oper.equals("/")) {
                double res = (double) tempValue2Int / (double) tempValue1Int;
                String resulta = String.valueOf(res);
                if (resulta.charAt(resulta.length() - 1) == '0') {
                    return resulta.substring(0, resulta.length() - 2);
                } else {
                    return resulta;
                }
            }
            if (oper.equals("%")) {
                return String.valueOf(tempValue2Int % tempValue1Int);
            }
        } else if ((objLex.Etiquetar(a).numero == 51 || objLex.Etiquetar(a).numero == 53)
                && (objLex.Etiquetar(b).numero == 53 || objLex.Etiquetar(b).numero == 51)) {
            String tempValStr1 = a.substring(1, a.length() - 1);
            String tempValStr2 = b.substring(1, b.length() - 1);
            if (oper.equals("+")) {
                return "\"" + tempValStr2 + tempValStr1 + "\"";
            }
        } else {
            Double tempValue1 = Double.parseDouble(a);
            Double tempValue2 = Double.parseDouble(b);
            if (oper.equals("+")) {
                String v = String.valueOf(tempValue1 + tempValue2);
                String[] aux = v.split("\\.");
                if (aux[1].equals("0")) {
                    return aux[0];
                } else {
                    return v;
                }
            }
            if (oper.equals("-")) {
                String v = String.valueOf(tempValue2 - tempValue1);
                String[] aux = v.split("\\.");
                if (aux[1].equals("0")) {
                    return aux[0];
                } else {
                    return v;
                }
            }
            if (oper.equals("*")) {
                String v = String.valueOf(tempValue1 * tempValue2);
                String[] aux = v.split("\\.");
                if (aux[1].equals("0")) {
                    return aux[0];
                } else {
                    return v;
                }
            }
            if (oper.equals("/")) {
                String v = String.valueOf(tempValue2 / tempValue1);
                String[] aux = v.split("\\.");
                if (aux[1].equals("0")) {
                    return aux[0];
                } else {
                    return v;
                }
            }
            if (oper.equals("%")) {
                String v = String.valueOf(tempValue2 % tempValue1);
                String[] aux = v.split("\\.");
                if (aux[1].equals("0")) {
                    return aux[0];
                } else {
                    return v;
                }
            }
        }
        return "Incompatible";
    }

    private void transExpresion(String expression) throws Exception {
        ArrayList<String> arrlist = expresionArray(expression);
        for (int i = arrlist.size() - 1; i >= 0; i--) {
            entrada.push(arrlist.get(i));
        }

        try {
            while (!entrada.isEmpty()) {
                switch (pref(entrada.peek())) {
                    case 1:
                        operTemp.push(entrada.pop());
                        break;
                    case 3:
                    case 4:
                        while (pref(operTemp.peek()) >= pref(entrada.peek())) {
                            salida.push(operTemp.pop());
                        }
                        operTemp.push(entrada.pop());
                        break;
                    case 2:
                        while (!operTemp.peek().equals("(")) {
                            salida.push(operTemp.pop());
                        }
                        operTemp.pop();
                        entrada.pop();
                        break;
                    default:
                        salida.push(entrada.pop());
                }
            }
        } catch (Exception ex) {
            System.out.println("Error en la expresión algebraica");
            System.err.println(ex);
        }
    }

    private ArrayList expresionArray(String expression) {
        ArrayList<String> expresion = new ArrayList<>();
        expresion.add("(");
        String conca = "";
        for (int i = 0; i < expression.length(); i++) {
            if (esOperador(String.valueOf(expression.charAt(i)))) {
                expresion.add(conca);
                conca = "";
                expresion.add(String.valueOf(expression.charAt(i)));
            } else {
                conca = conca + expression.charAt(i);
            }
        }
        expresion.add(conca);
        expresion.add(")");

        return expresion;
    }

    private static int pref(String op) {
        int prf = 99;
        if (op.equals("*") || op.equals("/") || op.equals("%")) {
            prf = 4;
        }
        if (op.equals("+") || op.equals("-")) {
            prf = 3;
        }
        if (op.equals(")")) {
            prf = 2;
        }
        if (op.equals("(")) {
            prf = 1;
        }
        return prf;
    }

    /**
     * Obtiene el nombre de cada variable declarada de tipo CADENA y los guarda
     * en un ArrayList
     *
     * @param texto el texto que se quiere analizar para guardar las variables
     * de tipo CADENA
     * @return ArrayList con todos las variables de tipo CADENA que se
     * escribieron
     */
    static ArrayList<String> obtNomVar(String texto) {
        ArrayList<String> arrVariables = new ArrayList<String>();
        String[] subCad = texto.split("\\s*[;| |\n|(|)]\\s*");

        for (int i = 0; i < subCad.length; i++) {
            if (subCad[i].equals("CAD")) {
                arrVariables.add(subCad[i + 1]);
            }
        }

        return arrVariables;
    }

    /**
     * Verificación de Captura CAP
     *
     * @param texto Texto de codigo
     * @param cadeVariables Array con las variables de tipo de dato CAD
     * @return
     */
    static String verificaCAP(String texto, ArrayList<String> cadeVariables) {
        String error = "";
        ArrayList<String> arrVariables = new ArrayList<String>();
        String[] subCad = texto.split("\\s*[;| |\n|(|)]\\s*");

        String[] lineasError = texto.split("\n");
        int numLinErr = 0;

        for (int i = 0; i < subCad.length; i++) {
            if (subCad[i].equals("CAP") && cadeVariables.contains(subCad[i + 1])) {
                arrVariables.add(subCad[i + 1]);
            }
            if (subCad[i].equals("CAP") && !cadeVariables.contains(subCad[i + 1])) {
                for (int j = 0; j < lineasError.length; j++) {
                    lineasError[j] = lineasError[j].replace(" ", "");
                    if (lineasError[j].contains("CAP(" + subCad[i + 1] + ")")) {
                        numLinErr = j + 1;
                    }
                }
                error += "Error, no se encontro la variable [" + subCad[i + 1] + "] en la linea " + numLinErr + "\n";
            }
        }
        return error;
    }

    /**
     * Elimina el nombre de las funciones que se guardaron en algun ArrayList de
     * variables
     *
     * @param texto Texto del codigo
     * @param variables ArrayList del nombre de las variables
     * @return
     */
    static ArrayList<String> elimNomFunCad(String texto, ArrayList<String> variables) {
        String[] subCad = texto.split("\\s*[;| |\n|(|)]\\s*");
        for (int i = 0; i < subCad.length; i++) {
            if (subCad[i].equals("FUN")) {
                variables.remove(subCad[i + 2]);
            }
        }
        return variables;
    }

    /**
     * Obtiene el nombre de la variable dependiendo el tipo de dato y los guarda
     * en un ArrayList
     *
     * @param texto Texto del codigo
     * @param tipoDato El tipo de dato de las variables
     * @return ArrayList con todos los nombres de la variable segun su tipo de
     * dato
     */
    static ArrayList<String> obtNomVar(String texto, String tipoDato) {
        ArrayList<String> arrVariables = new ArrayList<String>();
        String[] subCad = texto.split("\\s*[;| |\n|(|)]\\s*");

        for (int i = 0; i < subCad.length; i++) {
            if (subCad[i].equals(tipoDato)) {
                arrVariables.add(subCad[i + 1]);
            }
        }

        return arrVariables;
    }

    /**
     * Verifica que exista la variable
     *
     * @param variable Variable que se quiera verificar
     * @param numerosVar ArrayList con los nombres de las variables de tipo
     * NUMERICO
     * @param boolsVar ArrayList con los nombres de las variables de tipo
     * BOOLEANO
     * @param carsVar ArrayList con los nombres de las variables de tipo
     * CARACTER
     * @param cadenasVar ArrayList con los nombres de las variables de tipo
     * CADENA
     * @return
     */
    static String verificaExistVaria(String variable, ArrayList<String> numerosVar, ArrayList<String> boolsVar, ArrayList<String> carsVar, ArrayList<String> cadenasVar) {
        String variExist = "";

        //Verifica que exista la variables
        if (numerosVar.contains(variable) | boolsVar.contains(variable) | carsVar.contains(variable) | cadenasVar.contains(variable)) {
            variExist = variable;
        }

        return variExist;
    }

    /**
     * Verifica que se haya hecho una asignación de la variable
     *
     * @param texto Texto del codigo
     * @param variable Variable que se quiere revisar su asignación
     * @return
     */
    static boolean verifAsignacion(String texto, String variable) {

        if (variable.equals("")) {
            return false;
        }

        String[] subCad = texto.split("\\s*[\n]\\s*");

        Pattern pattern = Pattern.compile("^[^=]*=[^=]*$");
        Matcher matcher = pattern.matcher(variable);

        //Revisa que tenga asignacion la variable por tener un "=" 
        for (int i = 0; i < subCad.length; i++) {
            if (matcher.find()) {
                if (subCad[i].contains("=")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Verificacion de la salidad de datos
     *
     * @param texto Texto del codigo
     * @param numerosVar ArrayList con los nombres de las variables de tipo
     * NUMERICO
     * @param boolsVar ArrayList con los nombres de las variables de tipo
     * BOOLEANO
     * @param carsVar ArrayList con los nombres de las variables de tipo
     * CARACTER
     * @param cadenasVar ArrayList con los nombres de las variables de tipo
     * CADENA
     */
    static String verificaIMP(String texto, ArrayList<String> numerosVar, ArrayList<String> boolsVar, ArrayList<String> carsVar, ArrayList<String> cadenasVar, ArrayList<String[]> tablaIdentCol) {
        String mensajeError = "";

        String[] lineasError = texto.split("\n");
        int numLinErr = 0;

        //Divide el texto por cada ;, espacio blanco, salto o parentesis
        String[] subCad = texto.split("\\s*[;|\n|(|)]\\s*");

        String[] concatSep = null;

        //Detectar si hay una impresion
        for (int i = 0; i < subCad.length; i++) {
            if (subCad[i].equals("IMP")) {

                //Divide el contenido de la IMP sin concatenar
                concatSep = subCad[i + 1].split("%");

                //Verifica si es una operacion aritmetica o no
                for (int j = 0; j < concatSep.length; j++) {
                    if (concatSep[j].contains("+") | concatSep[j].contains("-") | concatSep[j].contains("*") | concatSep[j].contains("/")) {
                        String operaAritVerf = verificaOperacion(concatSep[j], tablaIdentCol, numerosVar, texto);
                        if (!operaAritVerf.equals("")) {
                            String[] separacion = operaAritVerf.split("\\s*[/|\n]\\s*");

                            String variableError = separacion[0];
                            String tipoError = separacion[1];

                            //Revisa en que linea estuvo el error
                            for (int k = 0; k < lineasError.length; k++) {
                                if (lineasError[k].contains("IMP(") && lineasError[k].contains(variableError)) {
                                    numLinErr = k + 1;
                                }
                            }

                            for (int k = 0; k < separacion.length; k++) {
                                if (k % 2 == 0) {
                                    variableError = separacion[k];
                                    tipoError = separacion[k + 1];
                                    mensajeError += "Error en la linea " + numLinErr + " con [" + variableError + "] " + tipoError + "\n";
                                }
                            }
                        }
                    } else {
                        //Verifica si es un numero
                        boolean esNum = false;
                        try {
                            int numero = Integer.parseInt(concatSep[i]);
                            esNum = true;
                        } catch (Exception e) {
                        }

                        //Revisa que la variable exista
                        if (!(concatSep[j].contains("\"") | concatSep[j].contains("'") | esNum)) {
                            String verifDecl = verificaExistVaria(concatSep[j], numerosVar, boolsVar, carsVar, cadenasVar);
                            if (verifDecl.equals("")) {

                                //Revisa en que linea estuvo el error
                                for (int k = 0; k < lineasError.length; k++) {
                                    if (lineasError[k].contains("IMP(") && lineasError[k].contains(verifDecl)) {
                                        numLinErr = k + 1;
                                    }
                                }
                                mensajeError += "Error en la linea " + numLinErr + " [" + concatSep[j] + "] no existe\n";
                            }
                            boolean asignado = verifAsignacion(texto, verifDecl);
                            if (!asignado && !verifDecl.equals("")) {

                                //Revisa en que linea estuvo el error
                                for (int k = 0; k < lineasError.length; k++) {
                                    if (lineasError[k].contains("IMP(") && lineasError[k].contains(verifDecl)) {
                                        numLinErr = k + 1;
                                    }
                                }

                                mensajeError += "Error en la linea " + numLinErr + " [" + concatSep[j] + "] sin asignación\n";
                            }
                        }
                    }
                }
            }
        }
        return mensajeError;
    }

    /**
     * Verificación de que las operaciones sean de tipo de dato NUM
     *
     * @param operacionAritmetica Operacion aritmetica
     * @param tablaIdenCol Array con arreglos de las variables, tipo de dato y
     * lineas
     * @param variablesNumericos Array con las variables de tipo de dato NUM
     * @param texto Texto del codigo
     * @return Tipo de error
     */
    static String verificaOperacion(String operacionAritmetica, ArrayList<String[]> tablaIdenCol, ArrayList<String> variablesNumericos, String texto) {
        System.out.println("Se va a verificar = " + operacionAritmetica);

        String tipoError = "";

        String[] datos = operacionAritmetica.split("[+|\\-|*|/]");

        Pattern pattern = Pattern.compile("\\b(FALS|VERD)");
        Matcher matcher;

        for (String dato : datos) {

            matcher = pattern.matcher(dato);
            try {
                int num = Integer.parseInt(dato);
            } catch (Exception e) {
                //Aqui se verifica que lo que esta dentro sea una variable de tipo NUMERICO
                for (int i = 0; i < tablaIdenCol.size(); i++) {
                    System.out.println(tablaIdenCol.get(i)[2] + " comparando con " + dato);
                    if (tablaIdenCol.get(i)[2].equals(dato)) {
                        System.out.println("Encontrado");
                        if (tablaIdenCol.get(i)[1].equals("50")) {
                            System.out.println("Es numerico");
                        }
                    }
                }
                if (dato.contains("\"") | dato.contains("'") | matcher.find()) {
                    tipoError += dato + "/tipo de dato incorrecto\n";
                } else {
                    if (!variablesNumericos.contains(dato)) {
                        tipoError += dato + "/ la variable no existe\n";
                    } else {
                        //Verifica que despues la variables que se encuentren sean asignados a un numero
                        boolean asignado = verifAsignacion(texto, dato);
                        if (!asignado) {
                            tipoError += dato + "/la variable no tiene asignación\n";
                        }
                    }
                }
            }
        }

        return tipoError;
    }

    public Pila<String> convertInfijPos(Pila<String> infixPila) {
        Pila<String> pila = new Pila<>();
        Pila<String> postfixPila = new Pila<>();
        Pila<String> aux = new Pila<>();
        Pattern stringPattern = Pattern.compile("[a-zA-Z0-9]+");
        Pattern numberPattern = Pattern.compile("\\d+(\\.\\d+)?");

        while (!infixPila.estaVacia()) {
            aux.push(infixPila.pop());
        }
        infixPila = aux;
        while (!infixPila.estaVacia()) {
            String elemento = infixPila.pop();

            if (numberPattern.matcher(elemento).matches()) {
                postfixPila.push(elemento);
            } else if (stringPattern.matcher(elemento).matches()) {
                postfixPila.push(elemento);
            } else if (elemento.equals("(")) {
                pila.push(elemento);
            } else if (elemento.equals(")")) {
                while (!pila.estaVacia() && !pila.peek().equals("(")) {
                    postfixPila.push(pila.pop());
                }
                if (!pila.estaVacia() && pila.peek().equals("(")) {
                    pila.pop(); // Pop '('
                }
            } else { // elemento es un operador aritmetico
                while (!pila.estaVacia() && precedence(elemento.charAt(0)) <= precedence(pila.peek().charAt(0))) {
                    postfixPila.push(pila.pop());
                }
                pila.push(elemento);
            }
        }

        while (!pila.estaVacia()) {
            postfixPila.push(pila.pop());
        }

        // Invertimos la pila para que quede en el orden correcto
        Pila<String> reversedPila = new Pila<>();
        while (!postfixPila.estaVacia()) {
            reversedPila.push(postfixPila.pop());
        }
        return reversedPila;
    }

    public Pila<String> convertInfijPosOpLog(Pila<String> infijo) {

//        Pattern stringPattern = Pattern.compile("[a-zA-Z0-9]+");
        Pattern stringPattern = Pattern.compile("[a-zA-Z0-9]+(\\.[0-9]+)?");

        Pila<String> posfijo = new Pila<>();
        Pila<String> pila = new Pila<>();
        Pila<String> aux = new Pila<>();
        Map<String, Integer> precedencia = new HashMap<>();
        while (!infijo.estaVacia()) {
            aux.push(infijo.pop());
//System.out.println("Ex "+infijo.pop());
        }
        infijo = aux;
        precedencia.put("(", 0);
        precedencia.put(")", 0);
        precedencia.put("||", 1);
        precedencia.put("&&", 2);
        precedencia.put("==", 3);
        precedencia.put("!", 3);
        precedencia.put("<", 4);
        precedencia.put(">", 4);
        precedencia.put("<=", 4);
        precedencia.put(">=", 4);

        while (!infijo.estaVacia()) {
            String elemento = infijo.pop();

            if (stringPattern.matcher(elemento).matches()) {
                posfijo.push(elemento);
            } else if (elemento.equals("(")) {
                pila.push(elemento);
            } else if (elemento.equals(")")) {
                while (!pila.estaVacia() && !pila.peek().equals("(")) {
                    posfijo.push(pila.pop());
                }
                if (!pila.estaVacia() && pila.peek().equals("(")) {
                    posfijo.push(pila.pop()); // sacar "("
                }
            } else { // es operador
                while (!pila.estaVacia() && precedencia.containsKey(elemento) && precedencia.get(elemento) <= precedencia.get(pila.peek()) && !pila.peek().equals("(")) {
                    posfijo.push(pila.pop());
                }
                pila.push(elemento);
            }
        }
        while (!pila.estaVacia()) {
            posfijo.push(pila.pop());
        }
        // invertimos la pila para que quede en orden correcto
        Pila<String> posfijoInvertido = new Pila<>();
        while (!posfijo.estaVacia()) {
            String elemento = posfijo.pop();
            if (!elemento.equals("(") && !elemento.equals(")")) {
                posfijoInvertido.push(elemento);
            }
        }
        return posfijoInvertido;
    }

    public Pila<String> convertInfijPosCad(Pila<String> infixPila) {
        Pila<String> pila = new Pila<>();
        Pila<String> postfixPila = new Pila<>();
        Pila<String> aux = new Pila<>();
        Pattern stringPattern = Pattern.compile("\"[\\w\\s]+\"");

        while (!infixPila.estaVacia()) {
            aux.push(infixPila.pop());
        }
        infixPila = aux;
        while (!infixPila.estaVacia()) {
            String elemento = infixPila.pop();

            if (stringPattern.matcher(elemento).matches()) {
                postfixPila.push(elemento);
            } else { // elemento es un operador de concatenacion
                while (!pila.estaVacia()) {
                    postfixPila.push(pila.pop());
                }
                pila.push(elemento);
            }
        }

        while (!pila.estaVacia()) {
            postfixPila.push(pila.pop());
        }

        // Invertimos la pila para que quede en el orden correcto
        Pila<String> reversedPila = new Pila<>();
        while (!postfixPila.estaVacia()) {
            reversedPila.push(postfixPila.pop());
        }
        return reversedPila;
    }

    public Pila<String> convertInfijPosBooleans(Pila<String> infixPila) {
        Pila<String> pila = new Pila<>();
        Pila<String> postfixPila = new Pila<>();
        Pila<String> aux = new Pila<>();
        Pattern stringPattern = Pattern.compile("\\b(VER|FALS)\\b");

        while (!infixPila.estaVacia()) {
            aux.push(infixPila.pop());
        }
        infixPila = aux;
        while (!infixPila.estaVacia()) {
            String elemento = infixPila.pop();

            if (stringPattern.matcher(elemento).matches()) {
                postfixPila.push(elemento);
            } else if (elemento.equals("(")) {
                pila.push(elemento);
            } else if (elemento.equals(")")) {
                while (!pila.estaVacia() && !pila.peek().equals("(")) {
                    postfixPila.push(pila.pop());
                }
                if (!pila.estaVacia() && pila.peek().equals("(")) {
                    pila.pop(); // Pop '('
                }
            } else { // elemento es un operador aritmetico
                while (!pila.estaVacia() && precedenceLogica(elemento.charAt(0)) <= precedenceLogica(pila.peek().charAt(0))) {
                    postfixPila.push(pila.pop());
                }
                pila.push(elemento);
            }
        }

        while (!pila.estaVacia()) {
            postfixPila.push(pila.pop());
        }

        // Invertimos la pila para que quede en el orden correcto
        Pila<String> reversedPila = new Pila<>();
        while (!postfixPila.estaVacia()) {
            reversedPila.push(postfixPila.pop());
        }
        return reversedPila;
    }

    public double evaluar(Pila<String> expresionPosfija) {
//        String[] txt = postfix.split("([;=()+\\-*/^&|><])", -1);
        Pila<Double> pila = new Pila<>();
        int i = 1;
        while (!expresionPosfija.estaVacia()) {
            String elemento = expresionPosfija.pop();
            if (isOperador(elemento)) {
                double operand2 = pila.pop();
                double operand1 = pila.pop();
                double resultado = opValida(operand1, operand2, elemento);
                Object[] filaDatos = {elemento, operand1 + "", operand2 + "", "Temp" + i, resultado + ""};
                setValorFila(valorFila + elemento + "\t" + operand1 + "\t" +operand2 + "\t" + "varTemp" + i + "\n");
                modeloTabla.addRow(filaDatos);
                pila.push(resultado);
                setResultadoFinal(resultado);
                i++;
            } else {
                double numero = Double.parseDouble(elemento);
                pila.push(numero);
            }
        }
        return pila.pop();
    }

    public String evaluarCadenas(Pila<String> expresion) {
        Pila<String> pila = new Pila<>();
        int i = 1;
        while (!expresion.estaVacia()) {
            String elemento = expresion.pop();
            if (isOperadorConcat(elemento)) {
                String operand2 = pila.pop();
                String operand1 = pila.pop();
                String resultado = operand1 + operand2;
                Object[] filaDatos = {elemento, operand1 + "", operand2 + "", "Temp" + i, resultado + ""};
                modeloTabla.addRow(filaDatos);
                pila.push(resultado);
                pila.push(elemento);
                i++;
            } else {
                pila.push(elemento);
            }
        }
        return pila.pop();
    }

    /*public String evaluarLogicos(Pila<String> expresionPosfija) {
        Pila<String> pila = new Pila<>();
        int i = 1;
        while (!expresionPosfija.estaVacia()) {
            String elemento = expresionPosfija.pop();
            if (isOperadorLogico(elemento)) {
                String operand2 = pila.pop();
                String operand1 = pila.pop();
                String resultado = opValidaLogica(operand1, operand2, elemento);
                Object[] filaDatos = {elemento, operand1 + "", operand2 + "", "Temp" + i, resultado + ""};
                modeloTabla.addRow(filaDatos);
                pila.push(resultado);
            } else {
                pila.push(elemento);
            }
        }
        return pila.pop();
    }*/

    private boolean isOperador(String elemento) {
        return elemento.equals("+") || elemento.equals("-") || elemento.equals("*") || elemento.equals("/");
    }

    private boolean isOperadorConcat(String elemento) {
        return elemento.equals("%");
    }

    private boolean isOperadorLogico(String elemento) {
        return elemento.equals("&&")
                || elemento.equals("||")
                || elemento.equals(">")
                || elemento.equals("<")
                || elemento.equals("<=")
                || elemento.equals(">=")
                || elemento.equals("!")
                || elemento.equals("==");
    }

    private double opValida(double operand1, double operand2, String operador) {
        switch (operador) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                return operand1 / operand2;
            case "^":
                return (int) Math.pow(operand1, operand2);
            case "<":
                return operand1 < operand2 ? 1 : 0;
            case ">":
                return operand1 > operand2 ? 1 : 0;
            case "=":
                return operand1 == operand2 ? 1 : 0;
            case "&":
                return operand1 != 0 && operand2 != 0 ? 1 : 0;
            case "|":
                return operand1 != 0 || operand2 != 0 ? 1 : 0;
            default:
                throw new IllegalArgumentException("Operacion aritmetica invalida: " + operador);
        }
    }

    private String opValidaLogica(String operand1, String operand2, String operador) {
        boolean booleano1 = operand1.equalsIgnoreCase("VER");
        boolean booleano2 = operand2.equalsIgnoreCase("VER");
        boolean resultado = false;
        if (operador.equals("&&")) {
            resultado = booleano1 && booleano2;
        } else if (operador.equals("||")) {
            resultado = booleano1 || booleano2;
        } else {
            if (operador.equals(">")) {
                resultado = Double.parseDouble(operand1) > Double.parseDouble(operand2);
            } else if (operador.equals("<")) {
                resultado = Double.parseDouble(operand1) < Double.parseDouble(operand2);
            } else if (operador.equals("<=")) {
                resultado = Double.parseDouble(operand1) <= Double.parseDouble(operand2);
            } else if (operador.equals(">=")) {
                resultado = Double.parseDouble(operand1) >= Double.parseDouble(operand2);
            } else if (operador.equals("==")) {
                try {
                    resultado = Double.parseDouble(operand1) != Double.parseDouble(operand2);
                    System.out.println("Op logica " + resultado);
                } catch (Exception ex) {
                    resultado = operand1.equals(operador);
                }
            } else if (operador.equals("!")) {
                try {
                    resultado = Double.parseDouble(operand1) != Double.parseDouble(operand2);
                } catch (Exception ex) {
                    resultado = !operand1.equals(operand2);
                }
            }
        }
        if (resultado) {
            return "VER";
        }
        return "FALS";
    }

    private int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return -1;
        }
    }

    private int precedenceLogica(char operator) {
        switch (operator) {
            case '|':
                return 1;
            case '&':
                return 2;
            default:
                return -1;
        }
    }

    /**
     * @return the modeloTabla
     */
    public DefaultTableModel getModeloTabla() {
        return this.modeloTabla;
    }

    public void setModeloTabla() {
        DefaultTableModel modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Operador");
        modeloTabla.addColumn("Valor 1");
        modeloTabla.addColumn("Valor 2");
        modeloTabla.addColumn("Temporal");
        modeloTabla.addColumn("Resultado");
        this.modeloTabla = modeloTabla;
    }
    
    public void espacio(){
        Object[] filaDatos = {"", "", " ", " " + "", ""};
        modeloTabla.addRow(filaDatos);
    }

    /*public void addFila(int i) {

        Object[] filaDatos = {"", "Asign", "Fila ", (i + 1) + "", ""};
        modeloTabla.addRow(filaDatos);
    }*/
}
