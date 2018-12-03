package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles AVL. La única diferencia
     * con los vértices de árbol binario, es que tienen una variable de clase
     * para la altura del vértice.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            super(elemento);
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return altura;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            return elemento + " " + altura + "/" + balance(this);
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)o;
            return (this.altura == vertice.altura && super.equals(o));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        rebalancea(ultimoAgregado.padre);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        Vertice v = vertice(busca(elemento));
        if (v != null) {
            elementos--;
            if (v.izquierdo != null && v.derecho != null)
                v = intercambiaEliminable(v);
            eliminaVertice(v);
            rebalancea(v.padre);
        }
    }

    private void rebalancea(Vertice vertice) {
        if (vertice == null)
            return;
        VerticeAVL v = verticeAVL(vertice);
        recalculaAltura(v);
        if (balance(v) == -2) {
            VerticeAVL q = verticeAVL(v.derecho);
            if (balance(q) == 1){
                VerticeAVL x = verticeAVL(q.izquierdo);
                super.giraDerecha(q);
                recalculaAltura(x);
                recalculaAltura(q);
            }
            q = verticeAVL(v.derecho);
            if (balance(q) == 0 || balance(q) == -1
                || balance(q) == -2) {
                    super.giraIzquierda(v);
                    recalculaAltura(q);
                    recalculaAltura(v);
                }

        }
        else if (balance(v) == 2) {
            VerticeAVL p = verticeAVL(v.izquierdo);
            if (balance(p) == -1){
                VerticeAVL x = verticeAVL(p.derecho);
                super.giraIzquierda(p);
                recalculaAltura(x);
                recalculaAltura(p);
            }
            p = verticeAVL(v.izquierdo);
            if (balance(p) == 0 || balance(p) == 1
                || balance(p) == 2) {
                    super.giraDerecha(v);
                    recalculaAltura(v);
                    recalculaAltura(p);
                }
        }
        if (v != null && v.padre != null)
            rebalancea(v.padre);
    }

    private void recalculaAltura(VerticeAVL vertice) {
        int izq = (vertice.izquierdo != null) ? verticeAVL(vertice.izquierdo).altura : -1;
        int der = (vertice.derecho != null) ? verticeAVL(vertice.derecho).altura : -1;
        vertice.altura = 1 + Math.max(izq, der);
    }


    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }

    private VerticeAVL verticeAVL(Vertice vertice) {
        return (VerticeAVL) vertice;
    }

    private int balance(VerticeAVL vertice) {
        if (vertice == null)
            return -1;
        VerticeAVL vIzq = verticeAVL(vertice.izquierdo);
        VerticeAVL vDer = verticeAVL(vertice.derecho);
        int altIzq = (vIzq != null) ? vIzq.altura : -1;
        int altDer = (vDer != null) ? vDer.altura : -1;
        return altIzq - altDer;
    }

}
