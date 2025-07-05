package sistema;

import exceptions.*;
import graph.GraphLink;
import list.*;
import modelo.Item;
import tree.*;

/**
 * InventorySystem para el Sistema de Gestión y Optimización de Inventarios en Almacenes.
 * Agrupa estructuras:
 * - B+ Tree para categorías de ítems,
 * - Grafo para ubicaciones,
 * - HashTable para búsqueda rápida.
 */
public class InventorySystem {
    private final BPlusTree<Item> bPlusTree;
    private final GraphLink<String> graph;
    private final HashTable<String, Item> hashTable;
    private final AVLTree<Item> avl;

    /**
     * Inicializa el sistema con estructuras vacías.
     * @throws ItemDuplicated si algún componente interno lo requiere
     */
    public InventorySystem() throws ItemDuplicated {
        bPlusTree = new BPlusTree<>();
        graph     = new GraphLink<>();
        hashTable = new HashTable<>();
        avl       = new AVLTree<>();
    }

    /**
     * Añade un ítem al sistema:
     * - lo inserta en el B+ Tree,
     * - en la tabla hash,
     * - registra su ubicación en el grafo si aún no está,
     * - lo inserta en el árbol AVL.
     * @throws ItemDuplicated si el ítem ya existe en B+ Tree o AVL
     * @throws ItemNotFound  
     * @throws IsEmpty       
     */
    public void addItem(Item item) throws IsEmpty, ItemNotFound, ItemDuplicated {
        // 1) Árbol B+
        bPlusTree.insert(item);
        // 2) HashTable
        hashTable.put(item.getCode(), item);
        // 3) Grafo de ubicaciones
        try {
            graph.addVertex(item.getLocation());
        } catch (ItemDuplicated ignored) {
            // la ubicación ya estaba presente, seguimos
        }
        // 4) Árbol AVL
        try {
            avl.insert(item);
        } catch (ItemDuplicated ignored) {
            // si ya existía en AVL, seguimos
        }
    }

    /**
     * Elimina un ítem por su código.
     * @param code código del ítem a borrar
     * @return true si el ítem existía y fue eliminado
     * @throws ItemNotFound si no se encuentra en B+ Tree o AVL
     * @throws IsEmpty      
     */
    public boolean removeItem(String code) throws IsEmpty, ItemNotFound {
        Item it = hashTable.get(code);
        if (it == null) {
            return false;
        }
        bPlusTree.delete(it);
        hashTable.remove(code);
        // Eliminar del AVL
        try {
            avl.delete(it);
        } catch (ItemNotFound ignored) {
            // no debería pasar si hashTable lo devolvió
        }
        return true;
    }

    /**
     * Recupera un ítem por su código.
     * @param code código del ítem
     * @throws ItemNotFound si no existe
     * @throws IsEmpty      
     */
    public Item getItem(String code) throws ItemNotFound, IsEmpty {
        Item it = hashTable.get(code);
        if (it == null) {
            throw new ItemNotFound("Ítem no encontrado: " + code);
        }
        return it;
    }

    /** Muestra por consola la estructura del B+ Tree. */
    public void displayTree() throws IsEmpty, ItemNotFound {
        bPlusTree.display();
    }

    /** Muestra por consola la representación del grafo de ubicaciones. */
    public void displayGraph() {
        System.out.println(graph);
    }
    
    /**
     * Muestra recorrido In-Order del AVL.
     */
    public void displayAVL() {
        System.out.println("Recorrido In-Order del AVL:");
        avl.inOrder();
    }

    /**
     * Abre o cierra una ruta (arista) entre dos ubicaciones.
     * @param edge  en formato "NodoA-NodoB"
     * @param enable true para crear la arista, false para eliminarla
     */
    public void toggleEdge(String edge, boolean enable) {
        String[] parts = edge.split("-");
        if (parts.length != 2) return;

        try {
            if (enable) {
                graph.addEdge(parts[0], parts[1], 1.0);
            } else {
                graph.removeEdge(parts[0], parts[1]);
            }
        } catch (Exception e) {
            System.out.println("Error en toggleEdge: " + e.getMessage());
        }
    }

    /**
     * Simula un escenario de optimización de rutas entre "Entrada" y "Salida"
     * usando el método shortestPath de GraphLink.
     */
    public void simulate() {
        try {
            LinkedList<String> path = graph.shortestPath("Entrada", "Salida");
            System.out.println("Ruta óptima: " + path);
        } catch (Exception e) {
            System.out.println("Error al simular ruta: " + e.getMessage());
        }
    }
}