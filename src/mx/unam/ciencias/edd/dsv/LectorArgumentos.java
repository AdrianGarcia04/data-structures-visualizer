package mx.unam.ciencias.edd.dsv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import mx.unam.ciencias.edd.Lista;

/**
 * <p>Clase abstracta para leer los argumentos que recibe el programa.</p>
 *
 * <p>La clase es capaz de regresar una lista de los elementos leidos y la
 * enumeracion de la estructura a graficar correspondiente.</p>
 */
public abstract class LectorArgumentos {

    /** La estructura a graficar leida */
    private static Estructura estructura;

    /** Lista elementos de la estructura de datos a graficar. */
    private static Lista<Integer> listaEnteros;

    /** La entrada para leer el archivo. */
    private static BufferedReader in;

    /**
    * Lee los argumentos que recibe el programa al ejecutarse y los guarda.
    * @param args un arreglo de argumentos.
    * @return Una coleccion con los enteros leidos del archivo.
    */
    public static Lista<Integer> lee(String[] args) {
        listaEnteros = new Lista<>();
        if (args.length == 0)
            try {
                if (System.in.available() != 0)
                    in = new BufferedReader(new InputStreamReader(System.in));
                else
                    salidaError("No se especificó la entrada");
            } catch (IOException ioe) {
                salidaError("No pudo leerse el archivo.");
            }
        else
            try {
                in = new BufferedReader(
                        new InputStreamReader(
                            new FileInputStream(args[0])));
            } catch (FileNotFoundException fnfe) {
                salidaError("No se encontró el archivo.");
            }
        leerArchivo(in);
        return listaEnteros;
    }

    /**
     * Lee un archivo linea por linea, guardando el nombre de la estructura a
     * graficar así como sus elementos y/o relaciones entre los mismos.
     * @param in la entrada de donde leer el archivo.
     */
    private static void leerArchivo(BufferedReader in) {
        String linea;
        Lista<String> palabras = new Lista<>();
        try {
            while ((linea = in.readLine()) != null) {
                int index = linea.indexOf('#');
                if (index != -1)
                    linea = linea.substring(0, index);
                linea = linea.trim();
                if (linea.length() > 0)
                    for (String cadena : linea.split("\\s+"))
                        palabras.agrega(cadena);
            }
        } catch (IOException ioe) {
            salidaError("Ocurrió un error al leer el archivo");
        }
        if (palabras.getElementos() == 0)
            salidaError("No se especificó la estructura");
        estructura = determinaEstructura(palabras.eliminaPrimero());
        for (String palabra : palabras)
            listaEnteros.agrega(verificaDigitoEntero(palabra));
    }

    /**
     * Determina si la cadena recibida es un numero entero y regresa su
     * representacion como numero entero.
     * @param cadena la cadena a evaluar.
     */
    private static int verificaDigitoEntero(String cadena) {
        int num = 0;
        try {
            num = Integer.parseInt(cadena);
        } catch (Exception e) {
            salidaError("Solo se permiten números enteros positivos como elementos");
        }
        return num;
    }

    /**
     * Determina la enumeracion a la que corresponde la estructura recibida.
     * @param estructura la cadena a evaluar.
     * @return la enumeracion correspondiente a la estructura.
     */
    private static Estructura determinaEstructura(String estructura) {
        switch (estructura) {
            case "L":
                return Estructura.L;
            case "Q":
                return Estructura.Q;
            case "S":
                return Estructura.S;
            case "CBT":
                return Estructura.CBT;
            case "BST":
                return Estructura.BST;
            case "RBT":
                return Estructura.RBT;
            case "AVL":
                return Estructura.AVL;
            case "G":
                return Estructura.G;
            case "MH":
                return Estructura.MH;
            default:
                salidaError("Estructura desconocida");
        }
        return null;
    }


    /** Regresa la estructura a graficar leida.  */
    public static Estructura getEstructura() {
        return estructura;
    }

    /**
     * Imprime el mensaje recibido en el flujo de error estandar y termina el programa.
     * @param mensaje el mensaje a imprimir.
     */
    private static void salidaError(String mensaje) {
        System.err.println(mensaje);
        System.exit(1);
    }

}
