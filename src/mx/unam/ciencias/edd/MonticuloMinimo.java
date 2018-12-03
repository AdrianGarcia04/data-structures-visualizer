package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase privada para iteradores de montículos. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return (indice < elementos);
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return get(indice++);
        }
    }

    /* Clase estática privada para poder implementar HeapSort. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            this.elemento = elemento;
            indice = -1;
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            return elemento.compareTo(adaptador.elemento);
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        arbol = nuevoArreglo(100);
        elementos = 0;
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
     public MonticuloMinimo(Iterable<T> iterable, int n) {
         arbol = nuevoArreglo(n);
         elementos = n;
         int i = 0;
         for (T elemento : iterable) {
             arbol[i] = elemento;
             elemento.setIndice(i++);
         }
         for (int j = (int) Math.floor(elementos / 2) - 1; j >= 0; j--)
             acomodaAbajo(j);
     }

     private void acomodaAbajo(int i) {
         if (2*i + 1 >= elementos) // El i-ésimo no tiene hijos
             return;
         int min = getHijoMenor(i);
         T vertice = arbol[i];
         T menor = arbol[min];
         if (vertice.compareTo(menor) > 0) {
             intercambia(i, min);
             acomodaAbajo(min);
         }
     }

     private int getHijoMenor(int i) {
         T izq = arbol[2*i + 1];
         T der = null;
         if (2*i + 2 < elementos)
             der = arbol[2*i + 2];
         if (der == null)
             return 2*i + 1;
         if (izq.compareTo(der) < 0)
             return 2*i + 1;
         else if (izq.compareTo(der) > 0)
             return 2*i + 2;
         else
             return 2*i + 1;
     }

     private void intercambia(int i, int j) {
         T iesimo = arbol[i];
         arbol[i] = arbol[j];
         arbol[i].setIndice(i);
         arbol[j] = iesimo;
         arbol[j].setIndice(j);
     }

     /**
      * Agrega un nuevo elemento en el montículo.
      * @param elemento el elemento a agregar en el montículo.
      */
     @Override public void agrega(T elemento) {
         if (elementos == arbol.length) {
             T[] nuevo = nuevoArreglo(2*elementos);
             for (int i = 0; i < elementos; i++)
                 nuevo[i] = arbol[i];
             arbol = nuevo;
         }
         arbol[elementos] = elemento;
         elemento.setIndice(elementos);
         acomodaArriba(elementos++);
     }

     private void acomodaArriba(int i) {
         if (i < 0 || i >= elementos)
             return;
         int indPadre = (i - 1) / 2;
         if (indPadre < 0)
             return;
         T vertice = arbol[i];
         T padre = arbol[indPadre];
         if (padre.compareTo(vertice) > 0) {
             intercambia(i, indPadre);
             acomodaArriba(indPadre);
         }
     }

     /**
      * Elimina el elemento mínimo del montículo.
      * @return el elemento mínimo del montículo.
      * @throws IllegalStateException si el montículo es vacío.
      */
     @Override public T elimina() {
         if (elementos == 0)
             throw new IllegalStateException();
         T raiz = arbol[0];
         intercambia(0, --elementos);
         acomodaAbajo(0);
         raiz.setIndice(-1);
         return raiz;
     }

     /**
      * Elimina un elemento del montículo.
      * @param elemento a eliminar del montículo.
      */
     @Override public void elimina(T elemento) {
         if (elemento.getIndice() < 0 || elemento.getIndice() >= elementos)
             return;
        int i = elemento.getIndice();
        T aux = arbol[i];
         intercambia(i, --elementos);
         arbol[elementos] = null;
         reordena(arbol[i]);
         aux.setIndice(-1);
     }

     /**
      * Nos dice si un elemento está contenido en el montículo.
      * @param elemento el elemento que queremos saber si está contenido.
      * @return <code>true</code> si el elemento está contenido,
      *         <code>false</code> en otro caso.
      */
     @Override public boolean contiene(T elemento) {
         if (elemento.getIndice() < 0 || elemento.getIndice() >= elementos)
             return false;
         return arbol[elemento.getIndice()].equals(elemento);
     }

     /**
      * Nos dice si el montículo es vacío.
      * @return <tt>true</tt> si ya no hay elementos en el montículo,
      *         <tt>false</tt> en otro caso.
      */
     @Override public boolean esVacia() {
         return elementos == 0;
     }

     /**
      * Limpia el montículo de elementos, dejándolo vacío.
      */
     @Override public void limpia() {
         for (int i = 0; i < elementos; i++)
             arbol[i] = null;
         elementos = 0;
     }

    /**
      * Reordena un elemento en el árbol.
      * @param elemento el elemento que hay que reordenar.
      */
     @Override public void reordena(T elemento) {
         if (elemento == null)
             return;
         acomodaAbajo(elemento.getIndice());
         acomodaArriba(elemento.getIndice());
     }

     /**
      * Regresa el número de elementos en el montículo mínimo.
      * @return el número de elementos en el montículo mínimo.
      */
     @Override public int getElementos() {
         return elementos;
     }

     /**
      * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
      * @param i el índice del elemento que queremos, en <em>in-order</em>.
      * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
      * @throws NoSuchElementException si i es menor que cero, o mayor o igual
      *         que el número de elementos.
      */
     @Override public T get(int i) {
         if (i < 0 || i >= elementos)
             throw new NoSuchElementException();
         return arbol[i];
     }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        String s = "";
        for (int i = 0; i < elementos; i++)
            s += String.format("%s, ", arbol[i]);
        return s;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param o el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)o;
        if (elementos != monticulo.elementos)
            return false;
        if (esVacia() && monticulo.esVacia())
            return true;
        int i = 0;
        for (T elemento : arbol)
            if (elemento.compareTo(monticulo.get(i++)) != 0)
                return false;
        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
        Lista<Adaptador<T>> l1 = new Lista<>();
        Lista<T> l2 = new Lista<>();
        for (T elemento : coleccion)
            l1.agrega(new Adaptador<>(elemento));
        MonticuloMinimo<Adaptador<T>> mm = new MonticuloMinimo<>(l1);
        while (mm.getElementos() > 0) {
            Adaptador<T> adp = mm.elimina();
            l2.agrega(adp.elemento);
        }
        return l2;
    }
}
