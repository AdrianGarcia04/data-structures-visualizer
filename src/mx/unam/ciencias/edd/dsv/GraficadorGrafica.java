package mx.unam.ciencias.edd.dsv;

import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Coleccion;
import java.util.Iterator;

/**
 * <p>Clase para graficar graficas no dirigidas. </p>
 */
public class GraficadorGrafica extends Graficador {

    /* Diametro en pixeles de los vertices de la grafica. */
    private double diametro;

    /* Coleccion de elementos a graficar. */
    private Coleccion<Integer> coleccion;

    /* Grafica de enteros para graficar la coleccion. */
    private Grafica<Integer> grafica;

    /**
     * Clase interna privada para vértices de graficas.
     * Los vertices graficables nos permiten guardar las coordenadas de cada
     * vértice en el plano SVG, para poder graficar las aristas entre vértices
     * y el texto del elemento correspondiente.
     */
    private class VerticeGraficable {

        /* Coordenada x del vértice. */
        double x;

        /* Coordenada y del vértice. */
        double y;

        /* Número entero del vértice. */
        Integer entero;

        /**
        * Constructor único de un vértice graficable.
        * @param x enumeracion de la estructura a graficar.
        * @param y coleccion con los elementos a graficar.
        * @param elemento coleccion con los elementos a graficar.
        */
        public VerticeGraficable(double x, double y, Integer entero) {
            this.x = x;
            this.y = y;
            this.entero = entero;
        }

    }

    /**
    * Constructor único que determina el díametro de cada vértice y grafica la
    * gráfica.
    * Si la sucesión de aristas es de longitud impar, ocurre un error.
    * @param coleccion coleccion con los elementos a graficar.
    */
    public GraficadorGrafica(Coleccion<Integer> coleccion) {
        this.coleccion = coleccion;
        grafica = new Grafica<>();
        diametro = getAnchoContenedor(coleccion, 5, 20);
        if (coleccion.getElementos() % 2 != 0)
            salidaError("El número de elementos en el archivo debe ser par.");
        pueblaGrafica();
        if (coleccion.getElementos() == 0)
            SVG.escribeCabecera(1, 1, false);
        else
            grafica();
        SVG.escribePiecera();
    }

    /**
    * Construye la gráfica con los elementos de la colección, y conecta los
    * elementos que sean consecutivos. Si dos elementos consecutivos son iguales,
    * se crea un único vértice sin crear una arista a sí mismo.
    * Si se añade el mismo vértice dos veces, ocurre un error.
    * Si se añade la misma arista dos veces, ocurre un error.
    */
    private void pueblaGrafica() {
        Iterator<Integer> it = coleccion.iterator();
        while (it.hasNext()) {
            int el1 = it.next();
            int el2 = it.next();
            if (el1 != el2) {
                if (!(grafica.contiene(el1)))
                    grafica.agrega(el1);
                if (!(grafica.contiene(el2)))
                    grafica.agrega(el2);
                if (grafica.sonVecinos(el1, el2))
                    salidaError("Las gráficas no pueden tener aristas repetidas.");
                grafica.conecta(el1, el2);
            }
            else if (el1 == el2 && !(grafica.contiene(el1)))
                grafica.agrega(el1);
            else
                salidaError("Las gráficas no pueden tener vértices repetidos.");
        }
    }

    /**
    * Grafica la gráfica.
    */
    @Override protected void grafica() {
        if (grafica.getElementos() == 1) {
            for (Integer numero : grafica) {
                SVG.escribeCabecera(50, 50, false);
                SVG.dibujaCirculo(25, 25, 10, "white", "black");
                SVG.dibujaTexto(25, 29, String.valueOf(numero), "black", 10);
            }
            return;
        }
        double radio = grafica.getElementos() * 10 + diametro * 3;
        double tamaño = radio * 2 + diametro + 20;
        SVG.escribeCabecera(tamaño, tamaño, false);
        double centroX = tamaño/2, centroY = tamaño/2;
        double grados = 360.0 / grafica.getElementos();
        int i = 0;
        Iterator<Integer> it = grafica.iterator();
        VerticeGraficable[] vertices = new VerticeGraficable[grafica.getElementos()];
        for (Integer entero : grafica) {
            double x = centroX + (radio * Math.cos((Math.PI * grados*i)/180));
            double y = centroY + (radio * Math.sin((Math.PI * grados*i)/180));
            vertices[i++] = new VerticeGraficable(x, y, entero);
        }
        for (i = 0; i < vertices.length; i++){
            VerticeGraficable v1 = vertices[i];
            for (int j = i + 1; j < vertices.length; j++) {
                VerticeGraficable v2 = vertices[j];
                if (grafica.sonVecinos(v1.entero, v2.entero))
                    SVG.dibujaLinea(v1.x, v1.y, v2.x, v2.y, false, false);
            }
        }
        for (i = 0; i < vertices.length; i++) {
            VerticeGraficable v = vertices[i];
            SVG.dibujaCirculo(v.x, v.y, diametro/2, "white", "black");
            SVG.dibujaTexto(v.x, v.y + 4, String.valueOf(v.entero), "black", 10);
        }
    }
}
