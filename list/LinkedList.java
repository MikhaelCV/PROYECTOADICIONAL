
package list;

import exceptions.*;
import graph.Vertex;

public class LinkedList<T> implements TDAList<T> {
    private LinkedNode<T> head;
    private int size;

    public LinkedList() {
        head = null;
        size = 0;
    }

    public void add(T data) {
    LinkedNode<T> newNode = new LinkedNode<>(data);
    if (head == null) {
        head = newNode;
    } else {
        LinkedNode<T> curr = head;
        while (curr.getNext() != null) {
            curr = curr.getNext();
        }
        curr.setNext(newNode);
    }
    size++;
}


    public void add(int index, T data) throws ItemDuplicated {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        LinkedNode<T> newNode = new LinkedNode<>(data);
        if (index == 0) {
            newNode.setNext(head);
            head = newNode;
        } else {
            LinkedNode<T> curr = head;
            for (int i = 0; i < index - 1; i++) {
                curr = curr.getNext();
            }
            newNode.setNext(curr.getNext());
            curr.setNext(newNode);
        }
        size++;
    }

    public void clear() throws IsEmpty {
        if (size == 0) {
            throw new IsEmpty("La lista ya está vacía");
        }
        head = null;
        size = 0;
    }

    public int indexOf(T data) {
        LinkedNode<T> curr = head;
        int idx = 0;
        while (curr != null) {
            if (curr.getData().equals(data)) {
                return idx;
            }
            curr = curr.getNext();
            idx++;
        }
        return -1;
    }

    public int lastIndexOf(T data) {
        LinkedNode<T> curr = head;
        int idx = 0, last = -1;
        while (curr != null) {
            if (curr.getData().equals(data)) {
                last = idx;
            }
            curr = curr.getNext();
            idx++;
        }
        return last;
    }

    public T get(int index) throws IsEmpty, ItemNotFound {
        if (size == 0) {
            throw new IsEmpty("La lista está vacía");
        }
        if (index < 0 || index >= size) {
            throw new ItemNotFound("Índice inválido: " + index);
        }
        LinkedNode<T> curr = head;
        for (int i = 0; i < index; i++) {
            curr = curr.getNext();
        }
        return curr.getData();
    }

    public T set(int index, T element) throws ItemNotFound, IsEmpty {
        if (size == 0) {
            throw new IsEmpty("La lista está vacía");
        }
        if (index < 0 || index >= size) {
            throw new ItemNotFound("Índice inválido: " + index);
        }
        if (indexOf(element) < 0) {
            throw new ItemNotFound("Elemento no encontrado: " + element);
        }
        LinkedNode<T> curr = head;
        for (int i = 0; i < index; i++) {
            curr = curr.getNext();
        }
        T old = curr.getData();
        curr.setData(element);
        return old;
    }

    public T remove(int index) throws IsEmpty, ItemNotFound {
        if (size == 0) {
            throw new IsEmpty("La lista está vacía");
        }
        if (index < 0 || index >= size) {
            throw new ItemNotFound("Índice inválido: " + index);
        }
        if (index == 0) {
            T old = head.getData();
            head = head.getNext();
            size--;
            return old;
        }
        LinkedNode<T> curr = head;
        for (int i = 0; i < index - 1; i++) {
            curr = curr.getNext();
        }
        T old = curr.getNext().getData();
        curr.setNext(curr.getNext().getNext());
        size--;
        return old;
    }

    public boolean remove(T data) throws IsEmpty, ItemNotFound {
        if (size == 0) {
            throw new IsEmpty("La lista está vacía");
        }
        int idx = indexOf(data);
        if (idx < 0) {
            throw new ItemNotFound("Elemento no encontrado: " + data);
        }
        remove(idx);
        return true;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(T data) {
        return indexOf(data) >= 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        LinkedNode<T> curr = head;
        while (curr != null) {
            sb.append(curr.getData());
            if (curr.getNext() != null) sb.append(", ");
            curr = curr.getNext();
        }
        sb.append("]");
        return sb.toString();
    }

    // MÉTODOS ADICIONALES

    public T removeFirst() throws IsEmpty, ItemNotFound {
        return remove(0);
    }

    public void addLast(T data) throws ItemDuplicated {
        add(data);
    }
}
