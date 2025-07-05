package test;
import sistema.InventorySystem;
import exceptions.IsEmpty;
import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;
import modelo.Item;
public class BPlusTreeTest {
    public static void main(String[] args) throws IsEmpty, ItemNotFound, ItemDuplicated {
        InventorySystem system = new InventorySystem();
        // Agregar varios ítems con códigos desordenados para probar balanceo
        system.addItem(new Item("C1", "ItemC1", 10, "Zona1"));
        system.addItem(new Item("A1", "ItemA1", 5, "Zona2"));
        system.addItem(new Item("B1", "ItemB1", 8, "Zona3"));
        system.addItem(new Item("D1", "ItemD1", 12, "Zona1"));
        system.addItem(new Item("E1", "ItemE1", 20, "Zona2"));
        system.addItem(new Item("F1", "ItemF1", 3, "Zona3"));
        // Mostrar la estructura del B+ Tree en consola
        System.out.println("--- B+ Tree ---");//imprime todo el objeto
        system.displayTree();// Ver si esta equilibrado
    }
}