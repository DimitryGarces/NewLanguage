package principal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Diego
 */
public class Sintactico {

    int mg[][] = {{},
    {23, 188, 152, 151, 22, 51, 12},
    {151, 31, 51, 13},
    {},
    {152, 153},
    {},
    {156},
    {192},
    {185},
    {182},
    {154, 155},
    {},
    {156},
    {170},
    {168},
    {159},
    {171},
    {173},
    {174},
    {177},
    {31, 193, 52},
    {31, 52, 24},
    {158, 35},
    {166},
    {17},
    {18},
    {160, 23, 154, 22, 21, 161, 20, 3},
    {23, 154, 22, 4},
    {},
    {162, 166, 27, 166},
    {161, 26},
    {},
    {50},
    {51},
    {164},
    {53},
    {54},
    {165, 52},
    {189},
    {},
    {167, 163},
    {167, 163, 25},
    {},
    {31, 21, 169, 166, 20, 1},
    {169, 166, 34},
    {},
    {31, 21, 52, 20, 2},
    {23, 154, 22, 21, 172, 50, 30, 50, 20, 5},
    {50, 29},
    {},
    {23, 154, 22, 21, 161, 20, 6},
    {31, 21, 161, 20, 6, 23, 154, 22, 7},
    {31, 11, 154, 30, 176, 8},
    {53},
    {51},
    {50},
    {23, 178, 22, 21, 52, 20, 9},
    {178, 175},
    {179},
    {154, 30, 10},
    {},
    {52, 24},
    {180, 16},
    {23, 31, 52, 19, 154, 22, 21, 183, 20, 181},
    {184, 180},
    {183, 29},
    {},
    {23, 154, 22, 21, 186, 20, 51, 15},
    {187, 180},
    {186, 29},
    {},
    {23, 154, 22, 14},
    {21, 190, 20},
    {191, 52},
    {191, 52, 29},
    {},
    {31, 157, 52},
    {189},
    {157}
    };
    int mt[][] = new int[45][55];

    int vecMov[]
            = {
                45, 150
            };

    int[] pp
            = {
                45
            };
    Renglon p = new Renglon(pp);
    int vecMovAux[], pr, pc, nl, tv;

    boolean banderaErrores = true, error = false;
    int[] palabras;

    private String errores = "";

    Renglon[] codigoFuente;
    Lexico le = new Lexico();

    public String getErrores() {
        return errores;
    }

    public Sintactico() {
        for (int i = 0; i < 45; i++) {
            for (int j = 0; j < 55; j++) {
                mt[i][j] = -1;
            }
        }

        mt[0][12] = 1;
        mt[1][13] = 2;
        mt[1][14] = 3;
        mt[1][15] = 3;
        mt[1][16] = 3;
        mt[1][24] = 3;
        mt[1][52] = 3;
        mt[2][14] = 5;
        mt[2][15] = 4;
        mt[2][16] = 4;
        mt[2][24] = 4;
        mt[2][52] = 4;
        mt[3][15] = 8;
        mt[3][16] = 9;
        mt[3][24] = 6;
        mt[3][31] = 7;
        mt[3][52] = 7;
        mt[4][1] = 10;
        mt[4][2] = 10;
        mt[4][3] = 10;
        mt[4][5] = 10;
        mt[4][6] = 10;
        mt[4][7] = 10;
        mt[4][9] = 10;
        mt[4][11] = 11;
        mt[4][19] = 11;
        mt[4][23] = 11;
        mt[4][24] = 10;
        mt[4][52] = 10;
        mt[5][1] = 14;
        mt[5][2] = 13;
        mt[5][3] = 15;
        mt[5][5] = 16;
        mt[5][6] = 17;
        mt[5][7] = 18;
        mt[5][9] = 19;
//        mt[5][31] = 20;
        mt[5][24] = 12;
//        mt[5][34] = 10;
        mt[5][52] = 20;
        mt[6][24] = 21;
        mt[7][35] = 22;
        mt[8][17] = 24;
        mt[8][18] = 25;

        mt[8][50] = 23;
        mt[8][51] = 23;
        mt[8][52] = 23;
        mt[8][53] = 23;
        mt[8][54] = 23;

        mt[9][3] = 26;
        mt[10][1] = 28;
        mt[10][2] = 28;
        mt[10][3] = 28;
        mt[10][4] = 27;
        mt[10][5] = 28;
        mt[10][6] = 28;
        mt[10][7] = 28;
        mt[10][9] = 28;
        mt[10][11] = 28;
        mt[10][19] = 28;
        mt[10][23] = 28;
        mt[10][24] = 28;
        mt[10][52] = 28;

        mt[11][50] = 29;
        mt[11][51] = 29;
        mt[11][52] = 29;
        mt[11][53] = 29;
        mt[11][54] = 29;
//        mt[11][21] = 29;
//        mt[11][25] = 29;
        mt[11][27] = 29;
//        mt[11][31] = 29;
//        mt[11][34] = 29;
        mt[12][21] = 31;
        mt[12][26] = 30;

        mt[13][50] = 32;
        mt[13][51] = 33;
        mt[13][52] = 34;
        mt[13][53] = 35;
        mt[13][54] = 36;

        mt[14][52] = 37;

        mt[15][20] = 38;
        mt[15][21] = 39;
        mt[15][25] = 39;
        mt[15][26] = 39;
        mt[15][27] = 39;
        mt[15][31] = 39;
        mt[15][34] = 39;

        mt[16][50] = 40;
        mt[16][51] = 40;
        mt[16][52] = 40;
        mt[16][53] = 40;
        mt[16][54] = 40;

        mt[17][21] = 42;
        mt[17][25] = 41;
        mt[17][26] = 42;
        mt[17][27] = 42;
        mt[17][31] = 42;
        mt[17][34] = 42;

        mt[18][1] = 43;

        mt[19][21] = 45;
        mt[19][34] = 44;

        mt[20][2] = 46;

        mt[21][5] = 47;

        mt[22][21] = 49;
        mt[22][29] = 48;

        mt[23][6] = 50;

        mt[24][7] = 51;

        mt[25][8] = 52;

        mt[26][53] = 53;
        mt[26][51] = 54;
        mt[26][50] = 55;

        mt[27][9] = 56;

        mt[28][8] = 57;
        mt[28][10] = 58;
        mt[28][23] = 58;

        mt[29][10] = 59;
        mt[29][23] = 60;

        mt[30][24] = 61;

        mt[31][16] = 62;

        mt[32][16] = 63;

        mt[33][24] = 64;

        mt[34][29] = 65;
        mt[34][21] = 66;

        mt[35][15] = 67;

        mt[36][24] = 68;

        mt[37][29] = 69;
        mt[37][21] = 70;

        mt[38][14] = 71;

        mt[39][20] = 72;

        mt[40][52] = 73;

        mt[41][29] = 74;
        mt[41][21] = 75;

        mt[42][52] = 76;

        mt[43][20] = 77;
        mt[43][35] = 78;
    }

    public boolean ejecutar(Renglon[] codigo) {
//        System.out.println(codigo.length);
        codigoFuente = codigo;
//        System.out.println(codigoFuente.length);
        codigoFuente[codigoFuente.length - 1] = p;
        for (int h = 0; h < codigoFuente.length; h++) {
            transformaMemoria(h);
            int i = 0;
            do {
                if (palabras.length == 0) {
//                    System.out.println("Z");
                    break;
                }
                pr = vecMov[vecMov.length - 1];
                if (pr >= 150) {
                    pr = pr - 150;
                    pc = palabras[i];
                    nl = mt[pr][pc];
                    if (nl != -1) {
                        vecMovAux = vecMov;
                        vecMov = new int[(vecMovAux.length + mg[nl].length) - 1];
                        tv = 0;
                        for (int j = 0; j < vecMovAux.length - 1; j++) {
                            vecMov[j] = vecMovAux[j];
                        }
                        for (int j = vecMovAux.length - 1; j < vecMov.length; j++) {
                            vecMov[j] = mg[nl][tv];
                            tv++;
                        }
                    } else {
                        banderaErrores = false;
                        error = true;
                        String b = le.EtiquetarInvertido(palabras[i], false);

                        errores += "Error sintactico de linea " + (h + 1) + " al recibir " + b + "\n";
                        int contador = 0;
                        i++;
                        vecMovAux = vecMov;
                        vecMov = new int[vecMovAux.length - contador];
                        for (int j = 0; j < vecMov.length; j++) {
                            vecMov[j] = vecMovAux[j];
                        }
                        break;
                    }
                } else {
                    if (palabras[i] == vecMov[vecMov.length - 1]) {
                        i++;
                        vecMovAux = vecMov;
                        vecMov = new int[vecMovAux.length - 1];
                        for (int j = 0; j < vecMov.length; j++) {
                            vecMov[j] = vecMovAux[j];
                        }
                    } else {
                        banderaErrores = false;
                        error = true;
                        String b = le.EtiquetarInvertido(palabras[i], false);

                        errores += "Error sintactico en linea " + (h + 1) + " al recibir " + b + "\n";

                        i++;
                        int contador = 0;
                        vecMovAux = vecMov;
                        vecMov = new int[vecMovAux.length - contador];
                        for (int j = 0; j < vecMov.length; j++) {
                            vecMov[j] = vecMovAux[j];
                        }
                        break;
                    }
                }
            } while (i < palabras.length);
            if (error) {
                banderaErrores = false;
                break;
            }
        }
        if (vecMov.length == 0 && banderaErrores == true) {
            errores = "";
        }
        return banderaErrores;
    }

    public void transformaMemoria(int h) {
        Pattern pattern = Pattern.compile("\\[(.*?)\\]"); //Expresion regular para encontrar los datos dentro de [ ]
        Matcher matcher = pattern.matcher(Arrays.toString(codigoFuente[h].getPalabras()));
        ArrayList<Integer> numeros = new ArrayList<>();
        while (matcher.find()) {
            String match = matcher.group(1); // Obtiene el contenido dentro de los corchetes
            String[] numerosStr = match.split(",\\s*"); // Divide la cadena en números separados por comas
            for (String numeroStr : numerosStr) {
                numeroStr = numeroStr.trim(); // Elimina espacios en blanco
                if (!numeroStr.isEmpty()) {
                    try {
                        int numero = Integer.parseInt(numeroStr); // Convierte la cadena a un entero
                        numeros.add(numero); // Agrega el entero al ArrayList
                    } catch (NumberFormatException e) {
                        // Maneja el caso en el que no se pueda convertir la cadena a un entero
                        //Sin embargo como estamos en un metodo externo, no podemos imprimir mensajes uwu
                    }
                }
            }
        }
        //Pasamos todo a una variable global para trabajar con eso
        palabras = new int[numeros.size()];
        for (int i = 0; i < numeros.size(); i++) {
            palabras[i] = numeros.get(i);
        }
    }

}
