package mx.unam.ciencias.edd;

import java.util.Iterator;


/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios completos. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Constructor que recibe la raíz del árbol. */
        public Iterador() {
            cola = new Cola<Vertice>();
            if (raiz != null)
                cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !(cola.esVacia());
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            Vertice v = cola.saca();
            if (v.izquierdo != null)
                cola.mete(v.izquierdo);
            if (v.derecho != null)
                cola.mete(v.derecho);
            return v.elemento;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
     @Override public void agrega(T elemento) {
         if (elemento == null)
             throw new IllegalArgumentException();
         Vertice v = nuevoVertice(elemento);
         elementos++;
         if (raiz == null) {
             raiz = v;
             return;
         }
         double power = Math.pow(2, Math.floor(Math.log(elementos) / Math.log(2)));
         // Obtenemos la coordenada x
         int x = (int)(-(power) + elementos);
         // derechas nos permite saber si la trayectoria al nuevo vertice
         // nos dirige al hijo derecho de cada vertice en la trayectoria
         Boolean[] derechas = new Boolean[altura()];
         int i = 0;
         // O(log_2(n))
         while(i < altura()) {
             // Si la coordenada x anterior es impar, entonces va a la derecha
             derechas[i++] = (x % 2 != 0) ? true : false;
             x = (x % 2 != 0) ? ((x - 1) / 2) : (x/2);
         }
         Vertice vertice = raiz;
         // O(log_2(n))
         for (int j = altura() - 1; j > 0; j--)
             vertice = (derechas[j]) ? vertice.derecho : vertice.izquierdo;
         v.padre = vertice;
         if (derechas[0])
             vertice.derecho = v;
         else
             vertice.izquierdo = v;
     }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice v = vertice(busca(elemento));
        if (v == null)
            return;
        if (--elementos == 0) {
            raiz = null;
            return;
        }
        Cola<Vertice> cola = new Cola<>();
        cola.mete(raiz);
        Vertice n = raiz;
        while (!(cola.esVacia())) {
            if (n.izquierdo != null)
                cola.mete(n.izquierdo);
            if (n.derecho != null)
                cola.mete(n.derecho);
            n = cola.saca();
            if (cola.esVacia())
                break;
        }
        T e = n.elemento;
        n.elemento = v.elemento;
        v.elemento = e;
        if (n.padre.izquierdo != null && n.padre.izquierdo == n)
            n.padre.izquierdo = null;
        else // Entonces n es hijo derecho
            n.padre.derecho = null;
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        if (raiz == null)
            return -1;
        return (int)(Math.floor(Math.log(elementos) / Math.log(2)));
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        if (elementos == 0)
            return;
        Cola<Vertice> cola = new Cola<>();
        cola.mete(raiz);
        while(!(cola.esVacia())){
            Vertice v = cola.saca();
            accion.actua(v);
            if (v.izquierdo != null)
                cola.mete(v.izquierdo);
            if (v.derecho != null)
                cola.mete(v.derecho);
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
