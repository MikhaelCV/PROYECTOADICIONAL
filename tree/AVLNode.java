package tree;


public class AVLNode<E> {
    public E element;
    public AVLNode<E> left;
    public AVLNode<E> right;
    public int height;

    public AVLNode(E element) {
        this.element = element;
        this.height = 1;
    }

    
}
