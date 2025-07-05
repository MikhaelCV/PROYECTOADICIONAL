package tree;

import exceptions.IsEmpty;
import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;
import list.ArrayList;

public class BPlusTree<T extends Comparable<T>> {
    private static final int DEFAULT_ORDER = 4;// arbol orden 4
    private final int order;
    private Node root;

    private abstract class Node {
        ArrayList<T> keys = new ArrayList<>();
        int keyCount() { return keys.size(); }
        abstract boolean isLeaf();
    }

    private class InternalNode extends Node {
        ArrayList<Node> children = new ArrayList<>();
        @Override boolean isLeaf() { return false; }
    }
    private class LeafNode extends Node {
        ArrayList<T> values = new ArrayList<>();
        LeafNode next;
        @Override boolean isLeaf() { return true; }
    }
    public BPlusTree() { this(DEFAULT_ORDER); }
    public BPlusTree(int order) {
        if (order < 3) throw new IllegalArgumentException("Order must be >= 3");
        this.order = order;
        root = new LeafNode();
    }
    public void insert(T key) throws IsEmpty, ItemNotFound, ItemDuplicated { //se insertan las claves
        LeafNode leaf = findLeaf(root, key); //encuentra la hoja correcta
        insertIntoLeaf(leaf, key);
        if (leaf.keyCount() > order - 1) splitLeaf(leaf);// evita que se exceda el orden del arbol
    }
    private LeafNode findLeaf(Node node, T key) throws IsEmpty, ItemNotFound {
        if (node.isLeaf()) return (LeafNode) node;
        InternalNode in = (InternalNode) node;
        int idx = 0;
        while (idx < in.keyCount() && key.compareTo(in.keys.get(idx)) >= 0) idx++;
        return findLeaf(in.children.get(idx), key);
    }//insertIntoLeaf inserta la clave key en la posicion correcta manteniendo orden
    private void insertIntoLeaf(LeafNode leaf, T key) throws IsEmpty, ItemNotFound, ItemDuplicated {
        int pos = 0;
        while (pos < leaf.keyCount() && key.compareTo(leaf.keys.get(pos)) > 0) pos++;
        leaf.keys.add(pos, key);
        leaf.values.add(pos, key);
    }
    private void splitLeaf(LeafNode leaf) throws IsEmpty, ItemNotFound, ItemDuplicated {
        int mid = order / 2;//Calcula la mitad de la cantidad max de claves permitidas por nodo
        LeafNode newLeaf = new LeafNode(); //Se crea una nueva hoja donde se moveran los elementos de la mitad derecha de la hoja actual
        for (int i = mid; i < leaf.keyCount(); i++) {
            newLeaf.keys.add(leaf.keys.get(i));
            newLeaf.values.add(leaf.values.get(i));
        }
        // borrar movidos de hoja original
        while (leaf.keyCount() > mid) {
            leaf.keys.remove(mid);
            leaf.values.remove(mid);//Se eliminan esas mismas claves de la hoja original 
        }//cada que elimina un elemento en mid, los sig bajan de posición automáticamente, por eso no se incrementa i
        // enlazar hojas
        newLeaf.next = leaf.next;
        leaf.next = newLeaf;
        // se promueve la primera clave del newLeaf al padre para que el arbol se reorganice
        insertIntoParent(leaf, newLeaf.keys.get(0), newLeaf);
    }//Si el nodo es la raíz, se crea una nueva raíz con ambos hijos
    private void insertIntoParent(Node left, T key, Node right) throws ItemDuplicated, ItemNotFound, IsEmpty {
        if (left == root) {//el nodo dividido era la raíz
            InternalNode newRoot = new InternalNode();//e crea una nueva raíz (newRoot
            newRoot.keys.add(key);//Se actualiza el puntero 
            newRoot.children.add(left);
            newRoot.children.add(right);
            root = newRoot;
            return;
        }
        InternalNode parent = findParent(root, left);//buscar el padre del nodo izquierdo 
        int idx = 0;//Determinar la posición donde insertar la nueva clave
        while (idx < parent.keyCount() && key.compareTo(parent.keys.get(idx)) >= 0) idx++;
        parent.keys.add(idx, key);//Insertar clave y nuevo hijo en el padre
        parent.children.add(idx+1, right);
        if (parent.children.size() > order) splitInternal(parent);
    }
    private InternalNode findParent(Node current, Node child) throws IsEmpty, ItemNotFound {
        if (!current.isLeaf()) {
            InternalNode in = (InternalNode) current;//1r recorrido si es hijo directo
            for (int i = 0; i < in.children.size(); i++) {//Recorre la lista de hijos de este nodo interno 
                if (in.children.get(i) == child) return in;//Si uno de sus hijos es igual al nodo buscado (child) 
            }//entonces este nodo es el padre y lo retorna
            for (int i = 0; i < in.children.size(); i++) {//repite el proceso recursivamente con cada hijo de este nodo
                InternalNode parent = findParent(in.children.get(i), child);
                if (parent != null) return parent;
            }//Si alguno de esos hijos retorna un padre no nulo, significa que lo encontró y se devuelve
        }
        return null;//caso base si el nodo actual es una hoja o no encuentra al padre
    }
//Si el padre se llena, se divide con splitInternal
    private void splitInternal(InternalNode node) throws IsEmpty, ItemNotFound, ItemDuplicated {
        int mid = order / 2;
        T upKey = node.keys.get(mid);
        InternalNode rightNode = new InternalNode();
        // mover claves
        for (int i = mid+1; i < node.keyCount(); i++) {
            rightNode.keys.add(node.keys.get(i));
        }
        // mover hijos
        for (int i = mid+1; i < node.children.size(); i++) {
            rightNode.children.add(node.children.get(i));
        }
        // recortar nodo original
        while (node.keyCount() > mid) node.keys.remove(mid);
        while (node.children.size() > mid+1) node.children.remove(node.children.size()-1);
        insertIntoParent(node, upKey, rightNode);
    }
    public boolean contains(T key) throws IsEmpty, ItemNotFound {
        LeafNode leaf = findLeaf(root, key);
        for (int i = 0; i < leaf.keyCount(); i++) {
            if (leaf.keys.get(i).equals(key)) return true;
        }
        return false;
    }
    
    public void delete(T key) throws IsEmpty, ItemNotFound {//elimina una clave específica
        LeafNode leaf = findLeaf(root, key);//Usa el método findLeaf para navegar desde la raíz hasta la hoja donde debería estar la clave key
        for (int i = 0; i < leaf.keyCount(); i++) {//Se recorre la lista de claves de la hoja
            if (leaf.keys.get(i).equals(key)) {//buscando una coincidencia exacta con key, si se encuentra
                leaf.keys.remove(i);//Se elimina del arreglo keys
                leaf.values.remove(i);//Se elimina del arreglo values
                return;
            }//keys se usan para navegar y busca
        }//values pueden contener los objetos completos, como Producto, Item, Registro
    }
//Imprime una representación jerárquica tipo árbol ASCII con indentaciones para cada nivel
    public void display() throws IsEmpty, ItemNotFound {
        display(root, "");
    }
    private void display(Node node, String indent) throws IsEmpty, ItemNotFound {
        if (node.isLeaf()) {
            System.out.println(indent + "Leaf: " + node.keys);
        } else {
            InternalNode in = (InternalNode) node;
            System.out.println(indent + "Internal: " + in.keys);
            for (int i = 0; i < in.children.size(); i++) {
                display(in.children.get(i), indent + "    ");
            }
        }
    }
}
