//August Carlsson auca4478
//Adan Anwar adan9862

import java.util.*;

public class MyUndirectedGraph<T> implements UndirectedGraph<T> {
    private Map<T, Node<T>> map = new HashMap<>();
    private T firstNode;
    private int noOfEdges;

    private static class Node<T>{
        public T data;
        public HashMap<T, Integer> connectedNodes = new HashMap<>();
        public Node (T data){
            this.data = data;
        }

    }
    private static class Edge<T> implements Comparable<Edge>{
        public T destination;
        public int weight;
        public Edge(T destination,  int weight){
            this.destination=destination;
            this.weight=weight;
        }

        @Override
        public int compareTo(Edge edge) {
            if(this.weight == edge.weight)
                return 0;
            return (this.weight > edge.weight) ? 1 : -1;
        }
    }





    @Override
    public int getNumberOfNodes() {
        return map.size();
    }

    @Override
    public int getNumberOfEdges() {
        return noOfEdges;
    }

    @Override
    public boolean add(T newNode) {
        if(!map.containsKey(newNode)){
            Node<T> node = new Node<>(newNode);
            firstNode = (firstNode == null) ? newNode: firstNode;
            map.put(newNode,node);
            return true;
        }
        return false;
    }

    @Override
    public boolean connect(T node1, T node2, int cost) {
        if(map.containsKey(node1) && map.containsKey(node2) && cost > 0 ) {
            map.get(node1).connectedNodes.put(node2, cost);
            map.get(node2).connectedNodes.put(node1, cost);
            noOfEdges++;
            return true;
        }

        return false;
    }


    @Override
    public boolean isConnected(T node1, T node2) {
        return map.get(node1).connectedNodes.containsKey(node2);
    }

    @Override
    public int getCost(T node1, T node2) {
        Node<T> n = map.get(node1);
        Integer cost = null;
        if(n!=null)
            cost = n.connectedNodes.get(node2);
        return (cost != null) ? cost : -1;
    }

    @Override
    public List<T> depthFirstSearch(T start, T end) {
        HashSet<T> tried = new HashSet<>();
        List<T> path = new Stack<>();
        depthFirst(start, end, (Stack) path, tried);
        return path;
    }

    private void depthFirst(T start, T end, Stack<T> path, HashSet<T> tried){
        tried.add(start);
        path.push(start);
        for (Map.Entry<T, Integer> currentNode : map.get(start).connectedNodes.entrySet()) {
            if (tried.contains(end))
                return;
            if (!tried.contains(currentNode.getKey()))
                depthFirst(currentNode.getKey(), end, path, tried);
            if (tried.contains(end))
                return;
        }
        path.pop();

    }

    @Override
    public List<T> breadthFirstSearch(T start, T end) {
        HashMap<T, T> parentData = new HashMap<>();
        LinkedList<T> currentNodeData = new LinkedList<>();
        parentData.put(start, null);
        currentNodeData.addLast(start);
        T currentData;

        while(!currentNodeData.isEmpty()){
            T tempChildData;
            currentData = currentNodeData.removeFirst();
            currentNodeData.addAll(currentNodeData.size(), map.get(currentData).connectedNodes.keySet());
            LinkedList<T> children = new LinkedList<>();
            children.addAll(map.get(currentData).connectedNodes.keySet());
            while(!children.isEmpty()){
                tempChildData = children.removeFirst();
                if(!parentData.containsKey(tempChildData))
                    parentData.put(tempChildData, currentData);
                if(tempChildData.equals(end))
                    return shortestPath(parentData, start, end);
            }
        }
        return shortestPath(parentData, start, end);
    }

    private List<T> shortestPath(HashMap<T, T> parentData, T start, T end){
        List<T> shortestPath = new LinkedList<>();
        shortestPath.add(0, end);
        T next = end;
        while((next = parentData.get(next)) != null)
            shortestPath.add(0, next);
        return shortestPath;

    }

    @Override
    public UndirectedGraph<T> minimumSpanningTree() {

        Set<T> tried = new HashSet<T>();
        Map<T, Integer> distances = new HashMap<T, Integer>();
        Map<T, Boolean> determined = new HashMap<T, Boolean>();

        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>();
        UndirectedGraph graph = new MyUndirectedGraph();

        depthSearch(firstNode, tried);
        for(T n : tried){
            graph.add(n);
            determined.put(n, false);
        }
        distances.put(firstNode, 0);
        determined.put(firstNode,true);

        return makeGraph(firstNode, distances, determined, priorityQueue, graph);
    }

    private UndirectedGraph<T> makeGraph(T from, Map<T, Integer> distances, Map<T, Boolean> determined, PriorityQueue<Edge> priorityQueue, UndirectedGraph<T> graph) {
        Edge<T> edge;
        for (Edge<T> e : getEdges(from)){
            if (!determined.get(e.destination)){
                int p = e.weight;
                Integer weight = distances.get(e.destination);
                if (weight == null || p < distances.get(e.destination)) {
                    priorityQueue.add(e);
                    distances.put(e.destination, p);
                }
            }
        }
        edge = priorityQueue.poll();
        if (edge == null)
            return graph;
        if(!determined.get(edge.destination)){
            determined.put(edge.destination, true);
            graph.connect(from, edge.destination, edge.weight);
        }
        return makeGraph(edge.destination, distances, determined, priorityQueue, graph);

    }



    private void depthSearch( T from, Set<T> tried){
        tried.add(from);
        for(Edge edge : getEdges(from)){
            if(!tried.contains(edge.destination))
                depthSearch((T) edge.destination, tried);
        }
    }

    private ArrayList<Edge> getEdges(T from){
        ArrayList<Edge> edges = new ArrayList<>();
        for(Map.Entry<T, Integer> entry : map.get(from).connectedNodes.entrySet()){
            edges.add(new Edge(entry.getKey(),entry.getValue()));
        }
        return edges;
    }




}
