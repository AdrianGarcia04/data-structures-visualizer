package mx.unam.ciencias.edd;

public class GraficaGraficable<T> extends Grafica<T> {

    /* Vértices para gráficas; implementan la interfaz VerticeGrafica */
    private class Vertice implements VerticeGrafica<T> {
        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La lista de vecinos del vértice. */
        public Lista<Vertice> vecinos;

        public int coordenadaX;
        public int coordenadaY;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            color = Color.NINGUNO;
            vecinos = new Lista<Vertice>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos;
        }

        public void setCoordenadas(int x, int y){
            this.coordenadaX = x;
            this.coordenadaY = x;
        }

        public Integer[] getCoordenadas(){
            Integer[] coordenadas = {coordenadaX, coordenadaY};
            return coordenadas;
        }

    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public GraficaGraficable() {
        vertices = new Lista<Vertice>();
    }

}
