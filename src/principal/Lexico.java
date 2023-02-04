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
    public String dic[][] = {
        {"IMP", "Imprimir", "1"},
        {"CAP", "Captura", "2"},
        {"STF", "Satisfacer", "3"},
        {"NSTF", "No Satisface", "4"},
        {"(", "Parentesis abre", "5"},
        {")", "Parentesis cierra", "6"},
        {"{", "Llave abre", "7"},
        {"}", "Llave cierra", "8"},
        {"CND", "Cuando", "9"},
        {"NUM", "Numerico", "10"},
        {"CAD", "Cadena", "10"},
        {"+", "Suma/Concatenación", "21"},
        {"-", "Resta", "12"},
        {"*", "Multiplicacion", "12"},
        {"/", "Division", "12"},
        {"^", "Potencia", "12"},
        {"&", "Y", "13"},
        {"|", "Or", "13"},
        {"<", "Menor que", "14"},
        {">", "Mayor que", "14"},
        {"<=", "Menor o igual que", "14"},
        {">=", "Mayor o igual que", "14"},
        {"!", "Diferente que", "14"},
        {"==", "Igualdad", "14"},
        {"\"", "Comillas", "15"},
        {",", "Coma", "16"},
        {":", "Dos puntos", "17"},
        {";", "Punto y coma", "18"},
        {".", "Punto", "19"},
        {"#", "Gato", "20"},
        {"=", "Igual", "22"}
    };

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
                break;
            }
            i++;

        }
        if (ban) {
            return objLex;
        }

        //Se agrega lo del automata general
        int maT[][] = {
            {1, -1, 6, 6, -1, 3},
            {1, -1, -1, -1, 2, -1},
            {2, -1, -1, -1, -1, -1},
            {4, 4, 4, 4, - 1, 5},
            {4, 4, 4, 4, - 1, 5},
            {-1, -1, -1, -1, -1, -1},
            {6, -1, 6, 6, -1, -1}
        };
        String vecNombre[] = {"Caracter desconocido", "Numerico", "Numerico", "CadenaQ3", "CadenaQ4", "CadenaQ5", "Variable"};
        int vecNumero[] = {103, 50, 50, 51, 51, 51, 52};
        String vecErrores[] = {"Caracter desconocido", "Numerico no valido", "Numerico no valido", "Cadena no validaQ3", "Cadena no validaQ4", "Cadena no validaQ5", "Variable no valida"};
        boolean band2 = true;
        int vecError[] = {103, 100, 100, 101, 101, 101, 102};
        int edo = 0, pos = 0;
        char vec[] = palabra.toCharArray();
        for (int j = 0; j < vec.length; j++) {
            if (Character.isDigit(vec[j])) {
                pos = 0;
            } else if (vec[j] == '"') {
                pos = 5;
            } else if (Character.isUpperCase(vec[j])) {
                pos = 2;
            } else if (Character.isLowerCase(vec[j])) {
                pos = 3;
            } else if (vec[j] == '.') {
                pos = 4;
            } else {
                pos = 1;
//                band2 = false;
//                break;
            }
            if (maT[edo][pos] != -1) {
                edo = maT[edo][pos];
            } else {
                band2 = false;
                break;
            }
        }
        if (band2) {
            if (edo == 3 || edo == 4 ) {
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
