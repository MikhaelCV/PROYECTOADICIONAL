package tree;
import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;

public class AVLTree<E extends Comparable<E>> {
    private AVLNode<E> root;

    public void insert(E element) throws ItemDuplicated {
        root = insert(root, element);
    }
//para insertar nuevos keys
    private AVLNode<E> insert(AVLNode<E> node, E element) throws ItemDuplicated {
        if (node == null) return new AVLNode<>(element);//colocamos al root

        int cmp = element.compareTo(node.element);//Compara el nuevo elemento con el del nodo actual
        if (cmp < 0) {
            node.left = insert(node.left, element);
        } else if (cmp > 0) {
            node.right = insert(node.right, element);
        } else {
            throw new ItemDuplicated("Elemento duplicado: " + element);
        }

        updateHeight(node);
        return balance(node);
    }
//para eliminar los keys///////////////////////////
    public void delete(E element) throws ItemNotFound {
        root = delete(root, element);//comenzamos desde la raiz
    }

    private AVLNode<E> delete(AVLNode<E> node, E element) throws ItemNotFound {
        if (node == null) throw new ItemNotFound("Elemento no encontrado: " + element);//vacio?

        int cmp = element.compareTo(node.element);//compara los elementos con el nodo corredor(el que eliminaremos)
        if (cmp < 0) {
            node.left = delete(node.left, element);//si es menor izq
        } else if (cmp > 0) {
            node.right = delete(node.right, element);//si es mayor derecha
        } else {
            if (node.left == null || node.right == null) {//Esto elimina el nodo directamente.
                node = (node.left != null) ? node.left : node.right;
            } else {
                AVLNode<E> min = findMin(node.right);//Busca el sucesor inorden
                node.element = min.element;//Copia su valor en el nodo actual
                node.right = delete(node.right, min.element);//Luego elimina ese sucesor del subárbol derecho
            }
        }

        if (node != null) {//actualizmos la altyura
            updateHeight(node);
            node = balance(node);
        }

        return node;
    }
//encontradores
    private AVLNode<E> findMin(AVLNode<E> node) {
        while (node.left != null) node = node.left;
        return node;
    }
//econtrador de altura
    private int height(AVLNode<E> node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(AVLNode<E> node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }
//si hay splits vamos a balancear
    private int balanceFactor(AVLNode<E> node) {
        return height(node.left) - height(node.right);
    }

    private AVLNode<E> balance(AVLNode<E> node) {
        int balance = balanceFactor(node);
        if (balance > 1) {
            if (balanceFactor(node.left) < 0) node.left = rotateLeft(node.left);
            return rotateRight(node);//rotamos si es el nodo izq 
        }
        if (balance < -1) {
            if (balanceFactor(node.right) > 0) node.right = rotateRight(node.right);
            return rotateLeft(node);//al revez pero por la derecha
        }
        return node;
    }
///////////////////////////////////////////////////////////
    //ROTACIONES
        //derecha
    private AVLNode<E> rotateRight(AVLNode<E> y) {
        AVLNode<E> x = y.left;
        y.left = x.right;//x se convierte en el nuevo padre
        x.right = y;//y se convierte en el hijo derecho de x
        updateHeight(y);
        updateHeight(x);
        return x;
    }
        //izquierda
    private AVLNode<E> rotateLeft(AVLNode<E> x) {
        AVLNode<E> y = x.right;
        x.right = y.left;//y se convierte en el nuevo padre
        y.left = x;//x se convierte en el hijo izquierdo de y
        updateHeight(x);
        updateHeight(y);
        return y;
    }
//Para listar los elementos ordenados
    //
    public void inOrder() {
        inOrder(root);
        System.out.println();
    }

    private void inOrder(AVLNode<E> node) {
        if (node != null) {
            inOrder(node.left);
            System.out.print(node.element + " ");
            inOrder(node.right);
        }
    }
    
    ///////////////////////////
    public void printTree() {
        printTree(root, 0);
    }

    private void printTree(AVLNode<E> node, int level) {
        if (node != null) {
            printTree(node.right, level + 1);
            System.out.println("    ".repeat(level) + node.element);
            printTree(node.left, level + 1);
        }
    }
    ////////////////////////////////////
}

