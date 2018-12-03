package mx.unam.ciencias.edd.dsv;

import mx.unam.ciencias.edd.Coleccion;

/**
 * <p> Clase abstracta para graficar estructuras de datos. </p>
 *
 * <p> Un graficador permite crear un archivo xml que genera una imagen
 *  SVG dada una estructura de datos y sus elementos y/o relaciones entre
 *  dichos elementos. </p>
 */
public abstract class Graficador {

    /**
    * Regresa en ancho en pixéles de un contenedor para un elemento de la
    * colección recibida. El ancho es tal que es mínimo para contener el elemento
    * "más grande" (con respecto a su tamaño en cadena) de toda la colección.
    * @param coleccion la colección donde se busca al elemento "más grande".
    * @param factor pixeles que ocupara cada caracter del elemento.
    * @param anchoOmision ancho mínimo del contenedor.
    */
    protected double getAnchoContenedor(Coleccion<Integer> coleccion,
                                        double factor, double anchoOmision) {
        int max = 0;
        for (Integer num : coleccion)
            max = (num > max) ? num : max;
        return String.valueOf(max).length() * factor + anchoOmision;
    }

    /**
     * Imprime el mensaje recibido en el flujo de error estandar y termina el programa.
     * @param mensaje el mensaje a imprimir.
     */
    protected void salidaError(String mensaje) {
        System.err.println(mensaje);
        System.exit(1);
    }

    /**
     * Grafica la estructura de datos especificada al instanciar un
     *  objeto la clase concreta.
     */
    protected abstract void grafica();
}
