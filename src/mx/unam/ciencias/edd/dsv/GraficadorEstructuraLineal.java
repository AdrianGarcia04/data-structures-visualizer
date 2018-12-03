package mx.unam.ciencias.edd.dsv;

import mx.unam.ciencias.edd.Cola;
import mx.unam.ciencias.edd.Coleccion;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Pila;
import java.util.Iterator;

/**
 * <p>Clase para graficar estructuras lineales. Estructuras: </p>
 *
 * <ul>
 *   <li> Lista </li>
 *   <li> Cola </li>
 *   <li> Pila </li>
 * </ul>
 */
public class GraficadorEstructuraLineal extends Graficador {

    /* Ancho en pixeles de los contenedores de cada elemento. */
    private double anchoContenedor;

    /* Coleccion de elementos a graficar. */
    private Coleccion<Integer> coleccion;

    /* Enumeracion de la estructura a graficar. */
    private Estructura estructura;

     /**
     * Constructor único que determina el ancho de cada contenedor y grafica la
     * estructura.
     * @param estructura enumeracion de la estructura a graficar.
     * @param coleccion coleccion con los elementos a graficar.
     */
    public GraficadorEstructuraLineal(Estructura estructura,
                                        Coleccion<Integer> coleccion) {
        this.estructura = estructura;
        this.coleccion = coleccion;
        anchoContenedor = getAnchoContenedor(coleccion, 2, 45);
        if (coleccion.getElementos() == 0)
            SVG.escribeCabecera(1, 1, false);
        else
            grafica();
        SVG.escribePiecera();
    }

    /**
    * Grafica la estructura.
    */
    @Override protected void grafica() {
        if (estructura.equals(Estructura.LISTA))
            graficaLista();
        else if (estructura.equals(Estructura.COLA))
            graficaCola();
        else
            graficaPila();
    }

    /**
    * Grafica una lista.
    */
    private void graficaLista() {
        Lista<Integer> lista = new Lista<>();
        for (Integer numero : coleccion)
            lista.agrega(numero);
        double largoContenedor = 25;
        double separacionContenedor = anchoContenedor / 2;
        int numeroFlechas = (int) ((anchoContenedor / 2) + 1);
        double anchoSVG = (lista.getElementos() == 0) ? 1
                        : lista.getElementos() * anchoContenedor
                        + (lista.getElementos() - 1) * separacionContenedor + 10;;
        double largoSVG = largoContenedor + 10;
        SVG.escribeCabecera(anchoSVG, largoSVG, true);
        Iterator<Integer> it = lista.iterator();
        double y = 5;
        int i = 0;
        while (it.hasNext()) {
            int entero = it.next();
            double x = (anchoContenedor + separacionContenedor)*i + 5;
            double x2 = (anchoContenedor + separacionContenedor)*(i + 1) + 5;
            double mitadSVG = y + largoContenedor/2;
            SVG.dibujaRectangulo(x, y, anchoContenedor, largoContenedor, "white",
                                "black");
            SVG.dibujaTexto(x + anchoContenedor/2, mitadSVG + 3,
                            String.valueOf(entero), "black", 10);
            if (it.hasNext())
                SVG.dibujaLinea(x + anchoContenedor + 4, mitadSVG, x2 - 5,
                                mitadSVG, true, true);
            i++;
        }
    }

    /**
    * Grafica una cola.
    */
    private void graficaCola() {
        Cola<Integer> cola = new Cola<>();
        for (Integer numero : coleccion)
            cola.mete(numero);
        double largoContenedor = 25;
        double separacionContenedor = anchoContenedor / 2;
        int numeroFlechas = (int) ((anchoContenedor / 2) + 1);
        double anchoSVG = (coleccion.getElementos() == 0) ? 1
                        : coleccion.getElementos() * anchoContenedor
                        + (coleccion.getElementos() - 1) * separacionContenedor + 10;;
        double largoSVG = largoContenedor + 10;
        SVG.escribeCabecera(anchoSVG, largoSVG, true);
        double y = 5;
        int i = 0;
        while (!(cola.esVacia())) {
            int entero = cola.saca();
            double x = (anchoContenedor + separacionContenedor)*i + 5;
            double x2 = (anchoContenedor + separacionContenedor)*(i + 1) + 5;
            double mitadSVG = y + largoContenedor/2;
            SVG.dibujaRectangulo(x, y, anchoContenedor, largoContenedor, "white",
                                "black");
            SVG.dibujaTexto(x + anchoContenedor/2, mitadSVG + 3,
                            String.valueOf(entero), "black", 10);
            if (!(cola.esVacia()))
                SVG.dibujaLinea(x + anchoContenedor + 4, mitadSVG, x2 - 5,
                                mitadSVG, false, true);
            i++;
        }
    }

    /**
    * Grafica una pila.
    */
    private void graficaPila() {
        Pila<Integer> pila = new Pila<>();
        for (Integer num : coleccion)
            pila.mete(num);
        double largoSVG = coleccion.getElementos() * 50 + 10;
        double anchoSVG = anchoContenedor + 10;
        SVG.escribeCabecera(anchoSVG, largoSVG, true);
        double largoContenedor = 50;
        int i = 0;
        while (!(pila.esVacia())) {
            double x = (anchoSVG - anchoContenedor)/2;
            double x2 = (anchoSVG + anchoContenedor)/2;
            double y = largoContenedor*i + 5;
            double y2 = largoContenedor*(i + 1) + 5;
            SVG.dibujaRectangulo(x, y, anchoContenedor, largoContenedor, "white",
                                "white");
            SVG.dibujaLinea(x, y, x, y2, false, false);
            SVG.dibujaLinea(x2, y, x2, y2, false, false);
            SVG.dibujaTexto(anchoSVG/2, largoContenedor*i + largoContenedor/2 + 7,
                            String.valueOf(pila.saca()), "black", 10);
            i++;
        }
        i = 1;
        for (int elemento : coleccion) {
            double x = (anchoSVG - anchoContenedor)/2;
            double x2 = (anchoSVG + anchoContenedor)/2;
            double y = largoContenedor*(i++) + 5;
            SVG.dibujaLinea(x, y, x2, y, false, false);
        }
    }

}
