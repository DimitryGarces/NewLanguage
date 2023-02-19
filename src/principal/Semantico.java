package principal;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

public class Semantico {

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
                    1, -1, -1, -1
                },
                {
                    -1, 1, -1, -1
                },
                {
                    -1, -1, 1, -1
                },
                {
                    -1, -1, -1, 1
                }
            };
    int operAri[][]
            = {
                {
                    1, -1, -1, -1
                },
                {
                    -1, -1, -1, -1
                },
                {
                    -1, -1, -1, -1
                },
                {
                    -1, -1, 1, -1
                }
            };

    public String conversionNum(String s) {
        switch (s) {
            case "NUM":
                return "51";
            case "CAD":
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
            case "51":
                return "NUM";
            case "52":
                return "CAD";
            case "53":
                return "CHAR";
            case "54":
                return "BOOL";
            default:
                return "No valido";
        }
    }

    public boolean operCompatibles(String fila, String colum) {
        int f = Integer.parseInt(fila) - 50;
        int c = Integer.parseInt(colum) - 50;
        if (c != 52 && c != -1 && asig[f][c] == 1) {
            return true;
        } else {
            return false;
        }
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
        panel.setSize(100, 100);
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
        if (objLex.Etiquetar(a).numero == 51 && objLex.Etiquetar(b).numero == 51) {
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
        } else if ((objLex.Etiquetar(a).numero == 54 || objLex.Etiquetar(a).numero == 53)
                && (objLex.Etiquetar(b).numero == 54 || objLex.Etiquetar(b).numero == 53)) {
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
}
