package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<tt>null</tt>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles rojinegros. La única
     * diferencia con los vértices de árbol binario, es que tienen un campo para
     * el color del vértice.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            if (color == Color.NEGRO)
                return "N{" + elemento + "}";
            return "R{" + elemento + "}";
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)o;
            return (color == vertice.color && super.equals(o));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        return verticeRN(vertice).color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        rebalanceoAgrega(verticeRN(ultimoAgregado));
    }

    private void rebalanceoAgrega(VerticeRojinegro vertice) {
        coloreaRojo(vertice);
        if (vertice.padre == null) {
            coloreaNegro(vertice);
            return;
        }
        VerticeRojinegro padre = verticeRN(vertice.padre);
        if (esNegro(padre))
            return;
        VerticeRojinegro abuelo = verticeRN(padre.padre);
        VerticeRojinegro tio;
        if (esHijoIzquierdo(padre))
            tio = verticeRN(abuelo.derecho);
        else
            tio = verticeRN(abuelo.izquierdo);
        if (esRojo(tio)) {
            coloreaNegro(tio);
            coloreaNegro(padre);
            rebalanceoAgrega(abuelo);
            return;
        }
        if (cruzados(vertice, padre)) {
            if (esHijoIzquierdo(padre))
                super.giraIzquierda(padre);
            else
                super.giraDerecha(padre);
            VerticeRojinegro aux = vertice;
            vertice = padre;
            padre = aux;
        }
        coloreaNegro(padre);
        coloreaRojo(abuelo);
        if (esHijoDerecho(vertice))
            super.giraIzquierda(abuelo);
        else
            super.giraDerecha(abuelo);
    }

    private boolean cruzados(Vertice v, Vertice p) {
        return ((esHijoIzquierdo(p) && esHijoDerecho(v))
                || (esHijoDerecho(p) && esHijoIzquierdo(v)));
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        Vertice v = vertice(busca(elemento));
        if (v != null) {
            elementos--;
            if (v.izquierdo != null && v.derecho != null)
                v = intercambiaEliminable(v);
            VerticeRojinegro h;
            if (v.izquierdo == null && v.derecho == null) {
                h = verticeRN(nuevoVertice(null));
                coloreaNegro(h);
                h.padre = v;
                v.izquierdo = h;
            }
            h = (soloTieneIzquierdo(v)) ? verticeRN(v.izquierdo) : verticeRN(v.derecho);
            eliminaVertice(v);
            VerticeRojinegro vaux = verticeRN(v);
            if (esRojo(h)) {
                coloreaNegro(h);
                return;
            }
            if (esNegro(vaux) && esNegro(h))
                rebalanceaElimina(h);
            eliminaVertice(h);
        }
    }


    private void rebalanceaElimina(VerticeRojinegro v) {
        // Caso 1
        if (v.padre == null)
            return;
        VerticeRojinegro padre = verticeRN(v.padre);
        VerticeRojinegro hermano = (esHijoIzquierdo(v)) ? verticeRN(padre.derecho) : verticeRN(padre.izquierdo);
        // Caso 2
        if (esRojo(hermano)) {
            coloreaRojo(padre);
            coloreaNegro(hermano);
            if (esHijoIzquierdo(v))
                super.giraIzquierda(padre);
            else
                super.giraDerecha(padre);
        }
        hermano = (esHijoIzquierdo(v)) ? verticeRN(padre.derecho) : verticeRN(padre.izquierdo);
        // Caso 3
        VerticeRojinegro hi = verticeRN(hermano.izquierdo);
        VerticeRojinegro hd = verticeRN(hermano.derecho);
        if (esNegro(padre) && esNegro(hermano) && esNegro(hi) && esNegro(hd)) {
            coloreaRojo(hermano);
            rebalanceaElimina(padre);
        }
        // Caso 4
        if (esNegro(hermano) && esNegro(hi) && esNegro(hd)) {
                coloreaRojo(hermano);
                coloreaNegro(padre);
                return;
        }
        // Caso 5
        if ((esHijoIzquierdo(v) && esRojo(hi) && esNegro(hd)) ||
            (esHijoDerecho(v) && esNegro(hi) && esRojo(hd))) {
            coloreaRojo(hermano);
            if (esRojo(hi))
                coloreaNegro(hi);
            else
                coloreaNegro(hd);
            if (esHijoIzquierdo(v))
                super.giraDerecha(hermano);
            else
                super.giraIzquierda(hermano);
        }
        hermano = (esHijoIzquierdo(v)) ? verticeRN(padre.derecho) : verticeRN(padre.izquierdo);
        hi = verticeRN(hermano.izquierdo);
        hd = verticeRN(hermano.derecho);
        //Caso 6
        if ((esHijoIzquierdo(v) && esRojo(hd)) || (esHijoDerecho(v) && esRojo(hi))) {
            if (esNegro(padre))
                coloreaNegro(hermano);
            else
                coloreaRojo(hermano);
            coloreaNegro(padre);
            if (esHijoIzquierdo(v))
                coloreaNegro(hd);
            else
                coloreaNegro(hi);
            if (esHijoIzquierdo(v))
                super.giraIzquierda(padre);
            else
                super.giraDerecha(padre);
        }
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }

    private VerticeRojinegro verticeRN(VerticeArbolBinario<T> vertice) {
        return (VerticeRojinegro) vertice;
    }

    private boolean esNegro(VerticeRojinegro v) {
        return (v == null || v.color == Color.NEGRO);
    }

    private boolean esRojo(VerticeRojinegro v) {
        return (v != null && v.color == Color.ROJO);
    }

    private void coloreaRojo(VerticeRojinegro v) {
        v.color = Color.ROJO;
    }

    private void coloreaNegro(VerticeRojinegro v) {
        v.color = Color.NEGRO;
    }
}
