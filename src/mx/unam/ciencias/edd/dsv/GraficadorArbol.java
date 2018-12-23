package mx.unam.ciencias.edd.dsv;

import mx.unam.ciencias.edd.VerticeArbolBinario;
import mx.unam.ciencias.edd.Coleccion;
import mx.unam.ciencias.edd.Color;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.ArbolBinarioCompleto;
import mx.unam.ciencias.edd.ArbolBinarioOrdenado;
import mx.unam.ciencias.edd.MonticuloMinimo;
import mx.unam.ciencias.edd.Indexable;
import mx.unam.ciencias.edd.ArbolRojinegro;
import mx.unam.ciencias.edd.ArbolAVL;
import mx.unam.ciencias.edd.Lista;


/**
 * <p>Clase para graficar arboles binarios. Estructuras: </p>
 *
 * <ul>
 *   <li> Árbol binario completo </li>
 *   <li> Árbol binario ordenado </li>
 *   <li> Árbol rojinegro </li>
 *   <li> Árbol AVL </li>
 *   <li> Monticulo mínimo (árbol binario completo) </li>
 * </ul>
 */
public class GraficadorArbol extends Graficador {

    /* Árbol binario a graficar */
    private ArbolBinario<Integer> arbolBinario;

    /* Colección de elementos a graficar */
    private Coleccion<Integer> coleccion;

    /* Enumeracion de la estructura a graficar. */
    private Estructura estructura;

    /* El radio en pixeles de cada vértice del árbol. */
    private double radio;

    /* El ancho en pixeles de la imagen. */
    private double ancho;

    /* El largo en pixeles de la imagen. */
    private double largo;

    /* La separación en pixeles entre cada nivel del árbol. */
    private double separacionNivel;

    /**
    * Constructor único que construye el árbol binario a graficar, el radio
    * de los vértices, así como el ancho y largo de la imagen.
    * @param estructura enumeracion de la estructura a graficar.
    * @param coleccion coleccion con los elementos a graficar.
    */
    public GraficadorArbol(Estructura estructura, Coleccion<Integer> coleccion) {
        this.estructura = estructura;
        this.coleccion = coleccion;
        switch (estructura) {
            case CBT:
                arbolBinario = new ArbolBinarioCompleto<Integer>(coleccion);
                break;
            case BST:
                arbolBinario = new ArbolBinarioOrdenado<Integer>(coleccion);
                break;
            case RBT:
                arbolBinario = new ArbolRojinegro<Integer>(coleccion);
                break;
            case MH:
                pueblaMonticuloMinimo();
                break;
            default:
                arbolBinario = new ArbolAVL<Integer>(coleccion);
        }
        radio = getAnchoContenedor(coleccion, 2, 7);
        separacionNivel = 30;
        if (estructura.equals(Estructura.AVL)){
            ancho = Math.pow(2, arbolBinario.altura()) * (radio * 4);
            largo = arbolBinario.altura() * radio * 2
                    + arbolBinario.altura() * separacionNivel + radio * 3;
        }
        else {
            ancho = Math.pow(2, arbolBinario.altura()) * (radio * 3);
            largo = arbolBinario.altura() * radio * 2
                    + arbolBinario.altura() * separacionNivel + radio * 2;
        }
        if (arbolBinario.getElementos() == 0)
            SVG.escribeCabecera(1, 1, false);
        else
            grafica();
        SVG.escribePiecera();
    }

    /**
    * Grafica el árbol binario o en su caso, un montículo mínimo.
    */
    @Override protected void grafica() {
        if (estructura.equals(Estructura.MH))
            graficaMonticulo();
        else {
            SVG.escribeCabecera(ancho, largo, false);
            if (estructura.equals(Estructura.AVL))
                graficaArbol(arbolBinario.raiz(), 0, ancho, radio * 2, false);
            else
                graficaArbol(arbolBinario.raiz(), 0, ancho, radio, false);
        }
    }

    /**
    * Grafica un vértice dentro de los límites recibidos
    * @param vertice el vértice a graficar.
    * @param limiteIzquierdo el límite izquierdo desde donde puede graficarse el vértice.
    * @param limiteDerecho el límite derecho desde donde puede graficarse el vértice.
    * @param y la coordenada "y" del vértice
    * @param esHijoIzquierdo <tt>true</tt> si el vértice es hijo izquierdo,
    *              <tt>false</tt> en otro caso.
    */
    private void graficaArbol(VerticeArbolBinario<Integer> vertice,
                            double limiteIzquierdo, double limiteDerecho,
                            double y, boolean esHijoIzquierdo) {
        double x = (limiteIzquierdo + limiteDerecho) / 2;
        double x1 = x + (radio * Math.cos((Math.PI * 5)/4));
        double x2 = x + (radio * Math.cos((Math.PI * 7)/4));
        double y1 = y + (radio * Math.cos((Math.PI * 7)/4));
        String colorFondo = "white";
        String colorLinea = "black";
        String textColor = "black";
        if (estructura.equals(Estructura.RBT)) {
            ArbolRojinegro<Integer> arn = (ArbolRojinegro<Integer>) arbolBinario;
            if(arn.getColor(vertice) == Color.NEGRO)
                colorFondo = "black";
            else
                colorLinea = colorFondo = "red";
            textColor = "white";
        }
        if (estructura.equals(Estructura.AVL)) {
            int balance = getBalance(vertice);
            double x3 = (esHijoIzquierdo) ? x - radio : x + radio;
            SVG.dibujaTexto(x3, y - radio - 2, "(" + vertice.altura() + "/" + balance + ")", "black", 6);
        }
        SVG.dibujaCirculo(x, y, radio, colorFondo, colorLinea);
        SVG.dibujaTexto(x, y + 3, String.valueOf(vertice.get()), textColor, 8);
        if (vertice.hayIzquierdo()) {
            SVG.dibujaLinea(x1, y1, (limiteIzquierdo + x)/2, y + (radio * 2) + separacionNivel, false, false);
            graficaArbol(vertice.izquierdo(), limiteIzquierdo, x, y + (radio * 2) + separacionNivel, true);
        }
        if (vertice.hayDerecho()) {
            SVG.dibujaLinea(x2, y1, (limiteDerecho + x)/2, y + (radio * 2) + separacionNivel, false, false);
            graficaArbol(vertice.derecho(), x, limiteDerecho, y + (radio * 2) + separacionNivel, false);
        }
    }

    /**
    * Regresa el balance de un vértice de árbol AVL.
    * @param vertice el vértice del cuál obtener su balance.
    * @return el balance del vértice.
    */
    private int getBalance(VerticeArbolBinario<Integer> vertice) {
        if (vertice == null)
            return -1;
        VerticeArbolBinario<Integer> vIzq = (vertice.hayIzquierdo())
                                            ? vertice.izquierdo() : null;
        VerticeArbolBinario<Integer> vDer = (vertice.hayDerecho())
                                            ? vertice.derecho() : null;
        int altIzq = (vIzq != null) ? vIzq.altura() : -1;
        int altDer = (vDer != null) ? vDer.altura() : -1;
        return altIzq - altDer;
    }

    /**
    * Grafica un montículo mínimo como un arreglo y como árbol binario completo.
    */
    private void graficaMonticulo() {
        double crecimientoCurva = 10;
        double largoContenedor = 25;
        double anchoContenedor = getAnchoContenedor(coleccion, 2, 25);
        double largoCurvas = (coleccion.getElementos() * crecimientoCurva) / 4;
        double largoMonticulo = (largoCurvas + radio) * 2 + largoContenedor;
        ancho = coleccion.getElementos() * anchoContenedor + 10;
        SVG.escribeCabecera(ancho, largoMonticulo + largo, true);
        double inicio = (ancho / 2) - (coleccion.getElementos()*anchoContenedor / 2);
        graficaArbol(arbolBinario.raiz(), 0, ancho, largoMonticulo, false);
        int y = (int) largoCurvas + 5;
        int i = 0;
        int y2 = y + 25;
        int yp1 = y - 15;
        int yp2 = y + 45;
        int color = 0;
        for (Integer entero : arbolBinario) {
            if (i % 3 == 0)
                color = 0;
            else if (i % 2 == 0)
                color = 200;
            else
                color = 75;
            double coordX = anchoContenedor*i + inicio;
            String cadena = String.valueOf(entero);
            SVG.dibujaRectangulo(coordX, y, anchoContenedor, largoContenedor,
                                "white", "black");
            SVG.dibujaTexto(coordX + anchoContenedor/2, y + 15, cadena, "black", 6);
            SVG.dibujaTexto(coordX + 4, y + 5, String.valueOf(i), "black", 5);
            if (i != 0)
                SVG.dibujaTexto(coordX + anchoContenedor/2, y + 23,
                                "p: " + (i - 1) / 2, "black", 5);
            coordX += anchoContenedor / 2;
            if (i * 2 + 1 < coleccion.getElementos()) {
                int nn = (i * 2 + 1) - i;
                double curvaX = ((coordX + nn*anchoContenedor) + coordX) / 2;
                double x1 = (coordX + 5);
                double x2 = ((coordX) + nn*anchoContenedor);
                SVG.dibujaCurva(x1, y, curvaX, yp1, x2, (y - 2), color);
            }
            if (i * 2 + 2 < coleccion.getElementos()) {
                int nn = (i * 2 + 2) - i;
                double curvaX = ((coordX + nn*anchoContenedor) + coordX) / 2;
                double x1 = (coordX + 5);
                double x2 = ((coordX) + nn*anchoContenedor);
                SVG.dibujaCurva(x1, y2, curvaX, yp2, x2, (y2 + 2), color);
            }
            yp1 -= crecimientoCurva;
            yp2 += crecimientoCurva;
            i++;
        }
    }

    /**
    * Crea un montículo mínimo con los elementos de la colección, y
    * posteriormente crea un árbol binario completo con el montículo.
    */
    private void pueblaMonticuloMinimo() {
        MonticuloMinimo<Indexable<Integer>> monticulo;
        arbolBinario = new ArbolBinarioCompleto<>();
        Lista<Indexable<Integer>> lista = new Lista<>();
        for (Integer numero : coleccion)
            lista.agrega(new Indexable<>(numero, numero));
        monticulo = new MonticuloMinimo<>(lista);
        for (Indexable<Integer> idx : monticulo)
            arbolBinario.agrega((int) idx.getValor());
    }

}
