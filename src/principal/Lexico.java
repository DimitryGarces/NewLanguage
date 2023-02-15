/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

/**
 *
 * @author Diego
 */
public class Lexico {

    String lexema;
    String nombre;
    int numero;
    String tipo;
    public String dic[][] = {
        {"IMP", "Imprimir", "1", "PR"},
        {"CAP", "Capturar", "2", "PR"},
        {"STF", "Satisfacer", "3", "PR"},
        {"NSTF", "No Satisface", "4", "PR"},
        {"CND", "Cuando", "5", "PR"},
        {"MIENTRAS", "While", "6", "PR"},
        {"HACER", "DoWhile", "7", "PR"},
        {"CASO", "Case", "8", "PR"},
        {"CAMBIO", "Cambio", "9", "PR"},
        {"PREDET", "Predet", "10", "PR"},
        {"DESCANSO", "Break", "11", "PR"},
        {"CLASE", "Class", "12", "PR"},
        {"IMPLEM", "Implementar", "13", "PR"},
        {"PRINCIPAL", "Principal", "14", "PR"},
        {"MET", "Método", "15", "PR"},
        {"FUN", "Función", "16", "PR"},
        {"VER", "Verdadero", "17", "PR"},
        {"FALS", "Falso", "18", "PR"},
        {"RETORNA", "return", "19", "PR"},
        {"(", "Parentesis abre", "20", "PA"},
        {")", "Parentesis cierra", "21", "PC"},
        {"{", "Llave abre", "22", "LLA"},
        {"}", "Llave cierra", "23", "LLC"},
        {"NUM", "Numerico", "24", "INT"},
        {"CAD", "Cadena", "24", "STR"},
        {"CHAR", "Caracter", "24", "CHR"},
        {"BOOL", "Boolean", "24", "BOL"},
        {"-", "Resta", "25", "RES"},
        {"*", "Multiplicacion", "25", "MUL"},
        {"/", "Division", "25", "DIV"},
        {"^", "Potencia", "25", "POT"},
        {"+", "Suma", "25", "SUM"},
        {"&", "Y", "26", "Y"},
        {"|", "Or", "26", "OR"},
        {"<", "Menor que", "27", "MRQ"},
        {">", "Mayor que", "27", "MYQ"},
        {"<=", "Menor o igual que", "27", "MRIQ"},
        {">=", "Mayor o igual que", "27", "MYIQ"},
        {"!", "Diferente", "27", "DIF"},
        {"==", "Igualdad", "27", "IGL"},
        {"\"", "Comillas", "28", "COMILLA"},
        {",", "Coma", "29", "COMA"},
        {":", "Dos puntos", "30", "DPTS"},
        {";", "Punto y coma", "31", "PUNTCOMA"},
        {".", "Punto", "32", "PUNTO"},
        {"#", "Gato", "33", "GATO"},
        {"%", "concatenacion", "34", "CONCAT"},
        {"=", "Igual", "35", "IGUAL"}};
//    public String dic[][] = {
//        {"IMP", "Imprimir", "1"},
//        {"CAP", "Captura", "2"},
//        {"STF", "Satisfacer", "3"},
//        {"NSTF", "No Satisface", "4"},
//        {"(", "Parentesis abre", "5"},
//        {")", "Parentesis cierra", "6"},
//        {"{", "Llave abre", "7"},
//        {"}", "Llave cierra", "8"},
//        {"CND", "Cuando", "9"},
//        {"NUM", "Numerico", "10"},
//        {"CAD", "Cadena", "10"},
//        {"+", "Suma/Concatenación", "21"},
//        {"-", "Resta", "12"},
//        {"*", "Multiplicacion", "12"},
//        {"/", "Division", "12"},
//        {"^", "Potencia", "12"},
//        {"&", "Y", "13"},
//        {"|", "Or", "13"},
//        {"<", "Menor que", "14"},
//        {">", "Mayor que", "14"},
//        {"<=", "Menor o igual que", "14"},
//        {">=", "Mayor o igual que", "14"},
//        {"!", "Diferente que", "14"},
//        {"==", "Igualdad", "14"},
//        {"\"", "Comillas", "15"},
//        {",", "Coma", "16"},
//        {":", "Dos puntos", "17"},
//        {";", "Punto y coma", "18"},
//        {".", "Punto", "19"},
//        {"#", "Gato", "20"},
//        {"=", "Igual", "22"}
//    };

    public String EtiquetarInvertido(int token, boolean b) {
        if (b) {
            if (token >= 1 && token <= 19) {
                return "PR";
            } else if (token <= 55 && token >= 50) {
                return "DAT";
            }
            for (int i = 0; i < dic.length; i++) {
                if (Integer.valueOf(dic[i][2]) == token) {
                    return dic[i][1];
                }
            }

        } else {
            for (int i = 0; i < dic.length; i++) {
                if (dic[i][2].equals(token + "")) {
                    return dic[i][1];
                }
            }

        }
        switch (token) {
            case 50:
                return "Numerico";
            case 51:
                return "Cadena";
            case 52:
                return "Variable";
            case 53:
                return "Caracter";
            case 54:
                return "Bool";
            case 100:
                return "Numerico no valido";
            case 101:
                return "Cadena no valido";
            case 102:
                return "Variable no valido";
            case 103:
                return "Caracter no valido";
            case 104:
                return "Bool no valido";
            default:
                return "Caracter desconocido";
        }

    }

    public Lexico Etiquetar(String palabra) {
        Lexico objLex = new Lexico();
        objLex.lexema = palabra;
        int i = 0;
        boolean ban = false;
        while (i < objLex.dic.length) {
            if (palabra.equals(dic[i][0])) {
                ban = true;
                objLex.nombre = dic[i][1];
                objLex.numero = Integer.parseInt(dic[i][2]);
                objLex.tipo = dic[i][3];
                break;
            }
            i++;

        }
        if (ban) {
            return objLex;
        }

        //Se agrega lo del automata general
        int maT[][] = {
            {1, -1, 6, 6, -1, 3, 7, 10, 10},
            {1, -1, -1, -1, 2, -1, -1, -1, -1},
            {2, -1, -1, -1, -1, -1, -1, -1, -1},
            {4, 4, 4, 4, - 1, 5, -1, -1, -1},
            {4, 4, 4, 4, - 1, 5, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1},
            {6, -1, 6, 6, -1, -1, -1, -1, -1},
            {8, 8, 8, 8, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, 9, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1}
        };
        String vecNombre[] = {"Caracter desconocido", "Numerico", "Numerico", "CadenaQ3", "CadenaQ4", "CadenaQ5", "Variable", "Caracter", "Caracter", "Caracter", "Bool"};
        int vecNumero[] = {105, 50, 50, 51, 51, 51, 52, 53, 53, 53, 54};
        String vecErrores[] = {"Caracter desconocido", "Numerico no valido", "Numerico no valido", "Cadena no validaQ3", "Cadena no validaQ4", "Cadena no validaQ5", "Variable no valida", "Caracter no valido", "Caracter no valido", "Caracter no valido", "Booleano no valido"};
        boolean band2 = true;
        int vecError[] = {105, 100, 100, 101, 101, 101, 102, 103, 103, 103, 104};
        int edo = 0, pos = 0;
        char vec[] = palabra.toCharArray();
        if (palabra.equals("VER")) {
            pos = 7;
        } else if (palabra.equals("FALS")) {
            pos = 8;
        } else {
            for (int j = 0; j < vec.length; j++) {
                if (Character.isDigit(vec[j])) {
                    pos = 0;
                } else if (vec[j] == '"') {
                    pos = 5;
                } else if (vec[j] == '\'') {
                    pos = 6;
                } else if (Character.isUpperCase(vec[j])) {
                    pos = 2;
                } else if (Character.isLowerCase(vec[j])) {
                    pos = 3;
                } else if (vec[j] == '.') {
                    pos = 4;
                } else {
                    pos = 1;
                }
                if (maT[edo][pos] != -1) {
                    edo = maT[edo][pos];
                } else {
                    band2 = false;
                    break;
                }
            }
        }

        if (band2) {
            if (edo == 3 || edo == 4) {
                objLex.nombre = vecErrores[edo];
                objLex.numero = vecError[edo];
            } else {
                objLex.nombre = vecNombre[edo];
                objLex.numero = vecNumero[edo];
            }
        } else {
            objLex.nombre = vecErrores[edo];
            objLex.numero = vecError[edo];
        }
        return objLex;
    }
}
