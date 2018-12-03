package mx.unam.ciencias.edd.dsv;

import mx.unam.ciencias.edd.Lista;

/**
 * Proyecto 1: Graficador de estructuras de datos
 */
public class DSV {

    public static void main(String[] args) {
        Lista<Integer> lista = LectorArgumentos.lee(args);
        Estructura estructura = LectorArgumentos.getEstructura();
        if (estructura.equals(Estructura.LISTA) || estructura.equals(Estructura.PILA)
            || estructura.equals(Estructura.COLA))
                new GraficadorEstructuraLineal(estructura, lista);
        else if (estructura.equals(Estructura.GRAFICA))
            new GraficadorGrafica(lista);
        else
            new GraficadorArbol(estructura, lista);
    }
}
