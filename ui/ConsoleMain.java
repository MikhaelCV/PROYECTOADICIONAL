package ui;
import exceptions.IsEmpty;
import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;
import java.util.Scanner;
import modelo.Item;
import sistema.InventorySystem;

public class ConsoleMain {
    public static void main(String[] args) throws ItemDuplicated, IsEmpty, ItemNotFound {
        InventorySystem system = new InventorySystem();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n====== MENU ALMACEN ======");
            System.out.println("1. Añadir ítem");
            System.out.println("2. Eliminar ítem");
            System.out.println("3. Buscar ítem");
            System.out.println("4. Mostrar B+ Tree");
            System.out.println("5. Mostrar Grafo");
            System.out.println("6. Simular escenario");
            System.out.println("7. Mostrar AVL");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");
            System.out.println("\n============================");
            String opt = scanner.nextLine();
            switch (opt) {
                case "1" -> {
                    System.out.print("Código: ");
                    String code = scanner.nextLine();
                    System.out.print("Nombre: ");
                    String name = scanner.nextLine();
                    System.out.print("Cantidad: ");
                    int qty = Integer.parseInt(scanner.nextLine());
                    System.out.print("Ubicación: ");
                    String loc = scanner.nextLine();
                    Item item = new Item(code, name, qty, loc);
                    system.addItem(item);
                    System.out.println("Ítem agregado.");
                }
                case "2" -> {
                    System.out.print("Código a eliminar: ");
                    String code = scanner.nextLine();
                    boolean removed = system.removeItem(code);
                    System.out.println(removed?"Ítem eliminado":"Ítem no encontrado");
                }
                case "3" -> {
                    System.out.print("Código a buscar: ");
                    String code = scanner.nextLine();
                    Item item = system.getItem(code);
                    System.out.println(item!=null?"Ítem: "+item:"No existe");
                }
                case "4" -> {
                    System.out.println("B+ Tree:");
                    system.displayTree();
                }
                case "5" -> {
                    System.out.println("Grafo de ubicaciones:");
                    system.displayGraph();
                }
                case "6" -> {
                    System.out.print("Cerrar ruta (A-B) o vacío: ");
                    String edge = scanner.nextLine();
                    if (!edge.isEmpty()) system.toggleEdge(edge,false);
                    system.simulate();
                }
                case "7" -> {
                    System.out.println("AVL:");
                    system.displayAVL();
                }
                case "8" -> {
                    System.out.println("Saliendo...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Opción inválida.");
            }
        }
    }
}