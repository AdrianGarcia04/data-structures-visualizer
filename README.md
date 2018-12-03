# Data Structures Visualizer
Data Structures Visualizer using Java and SVG

## Requirements

* [Apache Ant](https://ant.apache.org/)
* [Java](https://www.java.com/en/download/)

## Compilation

```bash
$ ant
```

## Use

```bash
$ java -jar dsv.jar file
```

Where file defines the desired data structure to be graphed with its 
corresponding elements

## File structure

Available structures  | Command
------------ | -------------
Doubly-linked list  | Lista
Stack  | Pila
Queue  | Cola
Complete binary tree  | ArbolBinarioCompleto
Ordered binary tree  | ArbolBinarioOrdenado
AVL tree  | ArbolAVL
RB tree  | ArbolRojinegro
Minheap  | MonticuloMinimo
Graph  | Grafica

For instance:

> Lista 1 2 3 4 5

> ArbolAVL 1 2 3 4 5

> MonticuloMinimo 1 2 3 4 5

> Graph 1 1 2 3 10 20 15 25 30 30

are valid files.

### Note
Graphs can be defined by edges _(20, 40)_ or vertices _(10, 10)_. There always exists an even quantity of numbers.
