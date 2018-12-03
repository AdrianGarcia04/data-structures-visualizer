package mx.unam.ciencias.edd.dsv;

/**
 * <p> Clase abstracta para generar cadenas de formato SVG. </p>
 *
 * <p> La clase SVG permite imprimir cadenas de formato SVG en la salida
 *  estándar, con métodos para cabeceras, pieceras, líneas, círculos,
    rectángulos, texto y curvas cuadráticas de Bézier. </p>
 */
public abstract class SVG {

    /**
    * Escribe en la salida estándar las etiquetas SVG correspondientes
    * al inicio del archivo.
    * @param anchoSVG el ancho de la imagen SVG.
    * @param largoSVG el largo de la imagen SVG.
    * @param defineFlechas si es <tt>true</tt>, escribe un bloque de definición
    */
    public static void escribeCabecera(double anchoSVG, double largoSVG,
                                    boolean defineFlechas) {
        int anchoi = (int) Math.round(anchoSVG);
        int largoi = (int) Math.round(largoSVG);
        System.out.println("<?xml version='1.0' encoding='UTF-8' ?>\n" +
                            escribeTabuladores(1) + "<svg width='" + anchoi
                            + "' height='" + largoi + "'>");
        if (defineFlechas)
            System.out.println(escribeTabuladores(2) + "<defs>\n" +
                                escribeTabuladores(3)
                                + "<marker id='startarrow' markerWidth='5' "
                                + "markerHeight='6' refX='2' refY='3' "
                                + "orient='auto'>\n"
                                + escribeTabuladores(4) + "<polygon points='9 0, 9 6, 0 2.8' "
                                + "fill='black'/>\n" +
                                escribeTabuladores(3) + "</marker>\n" +
                                escribeTabuladores(3) + "<marker id='endarrow' "
                                + "markerWidth='5' markerHeight='6' "
                                + "refX='2' refY='1.7' orient='auto'>\n"
                                + escribeTabuladores(4) + "<polygon points='0 0, 5 1.5, 0 3.2' "
                                + "fill='black'/>\n" +
                                escribeTabuladores(3) + "</marker>\n" +
                            escribeTabuladores(2) + "</defs>");
        System.out.println(escribeTabuladores(2) + "<g>");
    }

    /**
    * Crea y regresa una cadena con la cantidad de tabuladores especificada.
    * @param tabs el número de tabuladores requeridos.
    * @return una cadena con la cantidad de tabuladores especificada.
    */
    private static String escribeTabuladores(int tabs) {
        String s = "";
        for (int i = 1; i <= tabs; i++)
            s += "   ";
        return s;
    }

    /**
    * Escribe en la salida estándar la etiqueta SVG correspondiente a un círculo.
    * @param centroX coordenada en x del centro del círculo.
    * @param centroY coordenada en y del centro del círculo.
    * @param radio el radio del círculo.
    * @param colorFondo una cadena con el nombre del color de fondo.
    * @param colorBorde una cadena con el nombre del color del borde.
    */
    public static void dibujaCirculo(double centroX, double centroY, double radio,
                                String colorFondo, String colorBorde) {
        int xi = (int) Math.round(centroX);
        int yi = (int) Math.round(centroY);
        int ri = (int) Math.round(radio);
        System.out.println(escribeTabuladores(2) + "<circle fill='" + colorFondo + "' " +
                        "stroke='" + colorBorde + "' cx='" + xi + "' " +
                        "cy='" + yi + "' r='" + ri + "' />");
    }

    /**
    * Escribe en la salida estándar la etiqueta SVG correspondiente a un rectángulo.
    * @param x coordenada en x de la esquina superior izquierda del rectángulo.
    * @param y coordenada en y de la esquina superior izquierda del rectángulo.
    * @param ancho el ancho del rectángulo.
    * @param largo el largo del rectángulo.
    * @param colorFondo una cadena con el nombre del color de fondo.
    * @param colorBorde una cadena con el nombre del color del borde.
    */
    public static void dibujaRectangulo(double x, double y, double ancho,
                                        double largo, String colorFondo,
                                        String colorBorde) {
        int xi = (int) Math.round(x);
        int yi = (int) Math.round(y);
        int anchoi = (int) Math.round(ancho);
        int largoi = (int) Math.round(largo);
        System.out.println(escribeTabuladores(2) + "<rect x='" + xi + "' y='" + yi + "' "
                            + " width='" + anchoi + "' height='" + largoi + "' "
                            + "fill='" + colorFondo + "' stroke='" + colorBorde + "'/>");
    }

    /**
    * Escribe en la salida estándar la etiqueta SVG correspondiente a una
    * etiqueta de texto.
    * @param x coordenada en x del centro de la etiqueta de texto.
    * @param y coordenada en y del centro de la etiqueta de texto.
    * @param cadena cadena de texto a dibujar.
    * @param colorTexto una cadena con el nombre del color del texto.
    * @param size un entero que determina el tamaño en pixeles del texto.
    */
    public static void dibujaTexto(double x, double y, String cadena,
                                    String colorTexto, int size) {
        int xi = (int) Math.round(x);
        int yi = (int) Math.round(y);
        System.out.println(escribeTabuladores(2) + "<text font-family='sans-serif' " +
                        "font-size='" + size + "' x='" + xi + "' y='" + yi +"' "
                        + "fill='" + colorTexto + "' "
                        + "text-anchor='middle'>"+cadena+"</text>");
    }

    /**
    * Escribe en la salida estándar la etiqueta SVG correspondiente a una línea.
    * @param xInicio coordenada en x del inicio de la flecha.
    * @param yInicio coordenada en y del inicio de la flecha.
    * @param xFinal coordenada en x del final de la flecha.
    * @param yFinal coordenada en y del final de la flecha.
    * @param flechaInicio si es <tt>true</tt>, pone una flecha al inicio de la línea.
    * @param flechaFinal si es <tt>true</tt>, pone una flecha al final de la línea.
    */
    public static void dibujaLinea(double xInicio, double yInicio, double xFinal,
                                    double yFinal, boolean flechaInicio,
                                    boolean flechaFinal) {
        int xi1 = (int) Math.round(xInicio);
        int yi1 = (int) Math.round(yInicio);
        int xi2 = (int) Math.round(xFinal);
        int yi2 = (int) Math.round(yFinal);
        System.out.print(escribeTabuladores(2) + "<line x1='" + xi1 + "' "
                        + "y1='" + yi1 + "' x2='" + xi2 + "' "
                        + "y2='" + yi2 + "' stroke='#000' stroke-width='1' ");
        if (flechaInicio)
            System.out.print("marker-start='url(#startarrow)' ");
        if (flechaFinal)
            System.out.print("marker-end='url(#endarrow)'");
        System.out.println("/>");
    }

    /**
    * Escribe en la salida estándar la etiqueta SVG correspondiente a una curva
    * cuadrática de Bézier.
    * @param x1 coordenada en x del inicio de la curva.
    * @param y1 coordenada en y del inicio de la curva.
    * @param curvaX coordenada en x del punto externo de referencia para pintar
    *               la curva.
    * @param curvaY coordenada en y del punto externo de referencia para pintar
    *               la curva.
    * @param x2 coordenada en x del final de la curva.
    * @param y2 coordenada en y del final de la curva.
    * @param color entero entre 0 y 255 que determina un color de tres valores
    *               iguales en formato rgb.
    */
    public static void dibujaCurva(double x1, double y1, double curvaX,
                                    double curvaY, double x2, double y2,
                                    int color) {
        int xi1 = (int) Math.round(x1);
        int yi1 = (int) Math.round(y1);
        int xci = (int) Math.round(curvaX);
        int yci = (int) Math.round(curvaY);
        int xi2 = (int) Math.round(x2);
        int yi2 = (int) Math.round(y2);
        System.out.println(escribeTabuladores(2) + "<path d='M" + xi1 + " " + yi1
                            + " Q " + xci +" " + yci + " " + xi2 +" " + yi2
                            + "' " + "marker-end='url(#endarrow)' "
                            + "stroke='rgb(" + color + "," + color + "," + color + ")'"
                            + " fill='none'/>");

    }

    /**
    * Escribe en la salida estándar las etiquetas SVG correspondientes
    * al final del archivo.
    */
    public static void escribePiecera() {
        System.out.println(escribeTabuladores(2) + "</g>\n " + escribeTabuladores(1) + "</svg>");
    }

}
