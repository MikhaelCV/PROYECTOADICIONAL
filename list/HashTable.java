package list;
import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;
/**
 * HashTable personalizado para el Sistema de Gestión y Optimización de Inventarios en Almacenes.
 * Colisiones resueltas por encadenamiento usando nodos HashNode<K,V>.
 * No utiliza librerías de Java Collections.
 *
 * @param <K> tipo de clave
 * @param <V> tipo de valor
 */
public class HashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 37;// modulo 37
    private static final double DEFAULT_LOAD_FACTOR = 0.75;//llenado como max 75%(27.8)
    private HashNode<K, V>[] table;//array de listas enlazadas
    private int capacity;
    private int size;
    /**
     * Construye una HashTable con capacidad inicial por defecto.
     */
    @SuppressWarnings("unchecked")
    public HashTable() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = (HashNode<K, V>[]) new HashNode[capacity];
        this.size = 0;
    }
    /**
     * Función hash basada en hashCode(), acotada por la capacidad.
     */
    private int hash(K key) {
        return (key == null ? 0 : Math.abs(key.hashCode())) % capacity;
    }//Calcula el índice del bucket donde irá una clave

    /**
     * Inserta un par (key,value). Lanza ItemDuplicated si la clave ya existe.
     */
    public void put(K key, V value) throws ItemDuplicated {
        int idx = hash(key);//pocicion dentro del ht
        HashNode<K, V> node = table[idx];//toma el primer nodo
        while (node != null) {
            if ((key == null && node.getKey() == null) || (key != null && key.equals(node.getKey()))) {//Compara la clave que se quiere insertar con la de los nodos existentes.
                throw new ItemDuplicated("Clave duplicada: " + key);
            }
            node = node.getNext();//Continúa al siguiente nodo de la lista en caso no haya coincidencia aún.
        }
        HashNode<K, V> newNode = new HashNode<>(key, value);
        newNode.setNext(table[idx]);//El nuevo nodo se coloca al inicio de la lista enlazada.
        table[idx] = newNode;//La cabeza de la lista en la posición idx ahora es el nuevo nodo.
        size++;
        if ((double) size / capacity >= DEFAULT_LOAD_FACTOR) { //si excede la capacidad de 0.75
            rehash();// rehash, duplica el tamanio
        }
    }

    /**
     * Obtiene el valor asociado a la clave. Lanza ItemNotFound si no existe.
     */
    public V get(K key) throws ItemNotFound {
        int idx = hash(key);//calcula la pocicion
        HashNode<K, V> node = table[idx];//Toma el primer nodo de la lista enlazada que está en esa posición del arreglo.
        while (node != null) {
            if ((key == null && node.getKey() == null) || (key != null && key.equals(node.getKey()))) {//Compara la clave que se busca con la del nodo actual:
                return node.getValue();
            }
            node = node.getNext();//Si no encontró aún la clave, avanza al siguiente nodo en la lista enlazada.
        }
        throw new ItemNotFound("Clave no encontrada: " + key);
    }

    /**
     * Remueve el par con la clave dada y retorna su valor. Lanza ItemNotFound si no existe.
     */
    public V remove(K key) throws ItemNotFound {
        int idx = hash(key);//pisicion
        HashNode<K, V> node = table[idx];//primer node de la lista enlazada 
        HashNode<K, V> prev = null;//como es el primer el anteriior es null
        while (node != null) {
            if ((key == null && node.getKey() == null) || (key != null && key.equals(node.getKey()))) {//Si no son null pero son iguales usando .equals()
                V val = node.getValue();//Guarda el valor del nodo que se va a eliminar
                if (prev == null) {
                    table[idx] = node.getNext();
                } else {
                    prev.setNext(node.getNext());//Si está más adelante, el nodo anterior (prev) apunta al siguiente del actual (node.getNext()), eliminando así el nodo actual de la cadena.
                }
                size--;
                return val;
            }
            prev = node;
            node = node.getNext();
        }
        throw new ItemNotFound("Clave no encontrada: " + key);
    }

    /**
     * Verifica si existe la clave.
     */
    public boolean containsKey(K key) {
        try {
            get(key);
            return true;
        } catch (ItemNotFound e) {
            return false;
        }
    }

    /** @return número de pares almacenados */
    public int size() {
        return size;
    }

    /** @return true si la tabla está vacía */
    public boolean isEmpty() {
        return size == 0;
    }
 //Esta anotación le dice al compilador que ignore la advertencia de tipo genérico
    @SuppressWarnings("unchecked")
    public void clear() {
        this.table = (HashNode<K, V>[]) new HashNode[capacity];//Crea un nuevo arreglo vacío de nodos hash del mismo tamaño 
        this.size = 0;//einicia el contador de elementos en la tabla
    }

    //Suprime la advertencia de casting al crear un arreglo genéric
    //REHASHEO
    @SuppressWarnings("unchecked")
    private void rehash() {// duplica cuando se ingresa el modulo 9
        HashNode<K, V>[] oldTable = table;
        int oldCap = capacity;
        capacity *= 2;
        table = (HashNode<K, V>[]) new HashNode[capacity];
        size = 0;
        for (int i = 0; i < oldCap; i++) {
            HashNode<K, V> node = oldTable[i];
            while (node != null) {
                try {
                    put(node.getKey(), node.getValue());
                } catch (ItemDuplicated ignored) {
                    // no puede ocurrir aquí
                }
                node = node.getNext();
            }
        }
    }
}
