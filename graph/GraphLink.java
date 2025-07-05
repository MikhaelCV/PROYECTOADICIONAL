package graph;
import exceptions.ItemNotFound;
import exceptions.IsEmpty;
import exceptions.ItemDuplicated;
import list.LinkedList;
import graph.Vertex;
import graph.Edge;
public class GraphLink<E extends Comparable<E>> {//grafo no dirigido(ida y vuelta) y ponderado(peso) 
    private final LinkedList<Vertex<E>> vertices;//usando listas enlazadas
    public GraphLink() {
        vertices = new LinkedList<>();//inicia con una lista vacia
    }
    public void addVertex(E data) throws ItemDuplicated {//añade un vértice con el dato
        Vertex<E> v = new Vertex<>(data);
        if (vertices.indexOf(v) >= 0) {// si se ingresa un mismo vertice 
            throw new ItemDuplicated("Vértice ya existe: " + data);
        }
        vertices.add(v);// sino lo añade
    }
    public void removeVertex(E data) throws IsEmpty, ItemNotFound {//elimina el vertice
        if (vertices.isEmpty()) throw new IsEmpty("El grafo está vacío");
        Vertex<E> v = findVertex(data);
        for (int i = 0; i < vertices.size(); i++) {// recorre todo los vertices
            vertices.get(i).removeEdgeTo(v);// elimina sus aristas que apunten al vertice
        }
        vertices.remove(v);
    }
    public void addEdge(E src, E dest, double weight) throws ItemNotFound, ItemDuplicated, IsEmpty {
        Vertex<E> vSrc = findVertex(src);// encuentra el vertice origen
        Vertex<E> vDest = findVertex(dest);// encuentra el vertice destino
        Edge<E> edge = new Edge<>(vDest, weight);// a;ade la arista con un peso
        vSrc.addEdge(edge);
    }
    public void removeEdge(E src, E dest) throws ItemNotFound, IsEmpty {//elimina la arista
        Vertex<E> vSrc = findVertex(src);//busca el vértice origen usando el método auxiliar
        Vertex<E> vDest = findVertex(dest);//busca el vértice destino
        vSrc.removeEdgeTo(vDest);//lama al método removeEdgeTo del vértice origen (vSrc) 
    } //para eliminar la arista que apunta vDest
//encontrar el camino más corto (menor peso) entre dos vértices algoritmo Dijkstra
    public LinkedList<E> shortestPath(E origin, E destination) throws ItemNotFound, IsEmpty, ItemDuplicated { //busca el camino más corto desde origin hasta destination
        if (vertices.isEmpty()) throw new IsEmpty("El grafo está vacío");
        Vertex<E> src = findVertex(origin);//Busca los nodos origen y destino
        Vertex<E> dst = findVertex(destination);//Busca los nodos destino
        int n = vertices.size();//cantidad de vértices
        double[] dist = new double[n];//distancias mínimas desde el origen
        Vertex<E>[] prev = new Vertex[n];// predecesores para reconstruir el camino
        boolean[] visited = new boolean[n];//para saber qué vértices ya fueron visitados

        for (int i = 0; i < n; i++) {//Al inicio, solo el nodo origen tiene distancia 0
            dist[i] = vertices.get(i).equals(src) ? 0.0 : Double.POSITIVE_INFINITY;//luego dist inicia con infinito
//Si el vértice en la posición i es igual al vértice origen (src), entonces la distancia es 0.0. Si no, la distancia es infinita
            prev[i] = null;// nodo anterior es nulo,como inicia asi no ahy predecesor
            //de donde vienes para llegar al nodo
            visited[i] = false;// el nodo i es false porq no ha sido visitado aun
        }//true si ya fue visitado

        for (int k = 0; k < n; k++) {//se repite hasta haber visitado todos los vértices
            int u = -1;//Selecciona el vértice u no visitado con menor distanci
            double min = Double.POSITIVE_INFINITY; //variable minima q almacela distancia minima recorrida
            for (int i = 0; i < n; i++) {//y
                if (!visited[i] && dist[i] < min) {
                    min = dist[i];//la distancia mín conocida desde el nodo origen hasta el nodo i
                    u = i;// si sigue siendo -1 es porq no hay mas vertices
                }
            }
            if (u < 0) break;
            visited[u] = true;//Marca el vértice u como visitado
            Vertex<E> vU = vertices.get(u);//obj vertex al indice u
            if (vU.equals(dst)) break;
//Si el nodo actual vU es igual al nodo dest termina el bucle, porq ya se encontró el camino más corto hasta el destino
            LinkedList<Edge<E>> adj = vU.getAdjList();// se obtiene la lista de aristas del vertice actual
            for (int j = 0; j < adj.size(); j++) {// se recorre cada arista del nodo actual
                Edge<E> e = adj.get(j);
                Vertex<E> vV = e.getDestination();// se obtiene el nodo destino de la arista
                int vIdx = vertices.indexOf(vV);
                if (vIdx < 0) continue;// si el nodo no esta en la lista lo ignora
                double alt = dist[u] + e.getWeight();// calcula una ruta alternative tomando su distancia y peso
                if (alt < dist[vIdx]) { //si la distancia alternativa es menor que la conocida
                    dist[vIdx] = alt;// se actualiza la distancia
                    prev[vIdx] = vU;// se guarda el camino para reconstruirlo despues
                }
            }
        }

        LinkedList<E> path = new LinkedList<>();// se prepara una lista para guardar el camino mas corto
        Vertex<E> step = dst;// se empiza desde el nodo destino
        while (step != null) {// va reconstruyendo el camino
            path.add(0, step.getData());//se usa add 0 para que el camino quede en orden correcto
            int idx = vertices.indexOf(step);
            step = (idx >= 0) ? prev[idx] : null;// va a seguir retrocediendo hasta que prev quede null
        }
        return path; //se retorna los datos almacenados en la variable data
    }

    private Vertex<E> findVertex(E data) throws ItemNotFound, IsEmpty {
        for (int i = 0; i < vertices.size(); i++) {
            Vertex<E> v = vertices.get(i);
            if (v.getData().equals(data)) return v;
        }
        throw new ItemNotFound("Vértice no encontrado: " + data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vertices.size(); i++) {
            try { 
                sb.append(vertices.get(i).toString()).append("\n");
            } catch (IsEmpty | ItemNotFound e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
//busqueda de anchura,comenzando desde el nodo start
    public LinkedList<E> bfs(E start) throws ItemNotFound, IsEmpty {
        LinkedList<E> result = new LinkedList<>();// crea una lista
        LinkedList<Vertex<E>> queue = new LinkedList<>();// crea una cola donde almacena los datos
        boolean[] visited = new boolean[vertices.size()];//arreglo para marca si un vertice fue visitado o no
//Busca el objeto Vertex cuyo dato (getData()) sea igual a start
        Vertex<E> startVertex = findVertex(start);
        int idxStart = vertices.indexOf(startVertex);//btiene el índice del vértice de inicio 
        visited[idxStart] = true;//para marcarlo como visitado
        queue.add(startVertex);//lo añade a la cola como primer nodo por visitar

        while (!queue.isEmpty()) {//sigue hasta q haya vértices en la cola
            Vertex<E> current = queue.get(0);//e extrae el primer vértice de la cola (current) 
            queue.remove(0);//y se elimina
            result.add(current.getData());
//se agrega su dato a result, indicando que ya fue visitado
            LinkedList<Edge<E>> adj = current.getAdjList();//se obtiene la lista de aristas del nodo actual
            for (int i = 0; i < adj.size(); i++) {
                Vertex<E> neighbor = adj.get(i).getDestination();//se obtiene el vértice vecino (neighbor
                int idx = vertices.indexOf(neighbor);//se busca su índice (idx
                if (!visited[idx]) {//si idx no ha sido visitado
                    visited[idx] = true;//se marca como visitado
                    queue.add(neighbor);//se añade a la cola
                }
            }
        }
        return result;
    }//busqueda de profundidad en niveles
    public LinkedList<E> dfs(E start) throws ItemNotFound, IsEmpty {
        LinkedList<E> result = new LinkedList<>();//creacion de una lista
        boolean[] visited = new boolean[vertices.size()];//arreglo de booleanos que marca si cada nodo fue visitado o no
        Vertex<E> startVertex = findVertex(start);//busca en la lista de vértices el nodo cuyo dato (E) sea igual a start
        dfsRecursive(startVertex, visited, result);//llama función aux recursiva, para visitar cada nodo y vecinos
        return result;//al terminar devuelve la lista con el orden en que se visitaron los nodos
    }

    private void dfsRecursive(Vertex<E> v, boolean[] visited, LinkedList<E> result) {
        int idx = vertices.indexOf(v);//obtiene el índice del vértice actual
        if (visited[idx]) return;//si el nodo ya fue visitado,no se hace nada y se sale 
        visited[idx] = true;//marca el nodo actual como visitado
        result.add(v.getData());//Agrega el dato del vértice (E

        LinkedList<Edge<E>> adj = v.getAdjList();//obtiene la lista de aristas del nodo actual
        for (int i = 0; i < adj.size(); i++) {//se recorre cada arista saliente
            try {//para cada arista
                dfsRecursive(adj.get(i).getDestination(), visited, result);//se obtiene el nodo destino
            } catch (IsEmpty | ItemNotFound e) {
                e.printStackTrace();//imprime error al saltar las excepciones
            }

        }
    }

    public boolean hasCycle() {
    boolean[] visited = new boolean[vertices.size()];
    for (int i = 0; i < vertices.size(); i++) {
        try {
            if (!visited[i]) {
                if (hasCycleDFS(vertices.get(i), visited, null)) return true;
            }
        } catch (IsEmpty | ItemNotFound e) {
            e.printStackTrace();
        }
    }
    return false;
}

    private boolean hasCycleDFS(Vertex<E> current, boolean[] visited, Vertex<E> parent) {
        int idx = vertices.indexOf(current);
        visited[idx] = true;
        LinkedList<Edge<E>> adj = current.getAdjList();
        for (int i = 0; i < adj.size(); i++) {
            Vertex<E> neighbor = null;
            try {
                neighbor = adj.get(i).getDestination();
            } catch (IsEmpty | ItemNotFound e) {
                e.printStackTrace();
            }

            int nIdx = vertices.indexOf(neighbor);
            if (!visited[nIdx]) {
                if (hasCycleDFS(neighbor, visited, current)) return true;
            } else if (!neighbor.equals(parent)) {
                return true;
            }
        }
        return false;
    }

public int countConnectedComponents() {//try catch para el itemnofound p
    boolean[] visited = new boolean[vertices.size()];
    int count = 0;
    for (int i = 0; i < vertices.size(); i++) {
        try {
            if (!visited[i]) {
                LinkedList<E> temp = new LinkedList<>();
                dfsRecursive(vertices.get(i), visited, temp);
                count++;
            }
        } catch (IsEmpty | ItemNotFound e) {
            e.printStackTrace();
        }
    }
    return count;
}


    public LinkedList<E> getIsolatedZones() {
        LinkedList<E> isolated = new LinkedList<>();
        for (int i = 0; i < vertices.size(); i++) {
            try {
                if (vertices.get(i).getAdjList().isEmpty()) {
                    isolated.add(vertices.get(i).getData());
            }
        } catch (IsEmpty | ItemNotFound e) {
            e.printStackTrace();
        }
        }
        return isolated;
    }
}
