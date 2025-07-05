package test;

import exceptions.IsEmpty;
import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;
import modelo.Item;
import sistema.InventorySystem;

/**
 * Prueba integral de todas las operaciones equivalentes a los botones de la interfaz:
 * Añadir, Eliminar, Buscar, Mostrar B+ Tree, Mostrar Grafo y Simular Escenario.
 */
public class LaBichotaTest {
    public static void main(String[] args) throws IsEmpty, ItemNotFound, ItemDuplicated {
        System.out.println("--- Iniciando LaBichotaTest ---\n");
        InventorySystem system = new InventorySystem();

        // 1. Añadir ítems
        System.out.println("[Añadir Ítem]");
        try {
            system.addItem(new Item("P1", "Product1", 15, "Entrada"));
            system.addItem(new Item("P2", "Product2", 30, "AlmacenA"));
            system.addItem(new Item("P3", "Product3",  5, "AlmacenB"));
            system.addItem(new Item("P4", "Product4", 20, "Salida"));
            system.addItem(new Item("P5", "Product5", 12, "AlmacenC"));
            system.addItem(new Item("P6", "Product6", 25, "ZonaX"));
            System.out.println("Ítems añadidos: P1, P2, P3, P4, P5, P6\n");
        } catch (ItemDuplicated e) {
            System.out.println("Error al añadir ítem duplicado: " + e.getMessage());
        }

        // 2. Buscar ítems
        System.out.println("[Buscar Ítem]");
        try {
            System.out.println("Buscando P2: " + system.getItem("P2"));
        } catch (ItemNotFound e) {
            System.out.println(e.getMessage());
        }
        try {
            System.out.println("Buscando P7: " + system.getItem("P7"));
        } catch (ItemNotFound e) {
            System.out.println(e.getMessage());
        }
        System.out.println();

        // 3. Eliminar ítem
        System.out.println("[Eliminar Ítem]");
        try {
            boolean removed = system.removeItem("P3");
            System.out.println("Eliminación de P3 exitosa: " + removed);
        } catch (Exception e) {
            System.out.println("Error al eliminar P3: " + e.getMessage());
        }
        // Intento de eliminar P3 de nuevo sin detenerse
        try {
            boolean removedAgain = system.removeItem("P3");
            System.out.println("Intento de eliminar P3 de nuevo: " + removedAgain);
        } catch (Exception e) {
            System.out.println("Error al eliminar nuevamente P3: " + e.getMessage());
        }
        System.out.println();

        // 4. Mostrar B+ Tree
        System.out.println("[Mostrar B+ Tree]");
        system.displayTree();
        System.out.println();

        // 5. Configurar y mostrar Grafo con rutas
        System.out.println("[Mostrar Grafo con rutas]");
        system.toggleEdge("Entrada-AlmacenA", true);
        system.toggleEdge("Entrada-AlmacenB", true);
        system.toggleEdge("Entrada-AlmacenC", true);
        system.toggleEdge("AlmacenA-AlmacenB", true);
        system.toggleEdge("AlmacenB-AlmacenC", true);
        system.toggleEdge("AlmacenC-Salida", true);
        system.toggleEdge("AlmacenA-Salida", true);
        system.toggleEdge("AlmacenB-Salida", true);
        system.displayGraph();
        System.out.println();

        // 6. Simular escenario sin cortes
        System.out.println("[Simular Escenario sin cortes]");
        system.simulate();
        System.out.println();

        // 7. Simular escenario con cortes de rutas
        System.out.println("[Simular Escenario con cortes]");
        system.toggleEdge("Entrada-AlmacenA", false);
        system.toggleEdge("AlmacenB-Salida", false);
        system.toggleEdge("AlmacenC-Salida", false);
        system.simulate();
        System.out.println("--- Fin de LaBichotaTest ---");
        
        System.out.println();
        System.out.println("AVL:");
        system.displayAVL();
    }
}