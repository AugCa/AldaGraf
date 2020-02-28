package alda.graph;

import java.util.*;

public class MyUndirectedGraph<T> implements alda.graph.UndirectedGraph<T> {
    private Map<T, Node<T>> map = new HashMap<>();
    private T from = null;
    private HashMap<T, ArrayList<Edge<T>>> edges = new HashMap<T, ArrayList<Edge<T>>>();

    private static class Node<T>{
        public T data;
        public HashMap<T, Integer> neighbour = new HashMap<>();
        public Node (T data){
            this.data = data;
        }

    }
    private static class Edge<T> implements Comparable<Edge<T>>{

        private T destination;
        private int weight;
        public Edge(T destination,  int weight){
            if(weight<0)
                throw new IllegalArgumentException("negative weight");
            this.destination=destination;
            this.weight=weight;
        }

        public int getWeight(){return weight;}
        public T getDestination(){return destination;}
        public int compareTo(Edge<T> other) {
            return this.getWeight() - other.getWeight();}
    }





    @Override
    public int getNumberOfNodes() {
        return map.size();
    }

    @Override
    public int getNumberOfEdges() {
        return edges.size();
    }

    @Override
    public boolean add(T newNode) {
        if(map.containsKey(newNode))
            return false;
        Node<T> node = new Node<>(newNode);
        edges.put(newNode, new ArrayList<Edge<T>>());
        from = (from == null) ? newNode:from;
        map.put(newNode,node);
        return true;


    }

    @Override
    public boolean connect(T node1, T node2, int cost) {
        if(cost <= 0)
            return false;
        if(map.containsKey(node1) && map.containsKey(node2) ) {
            map.get(node1).neighbour.put(node2, cost);
            map.get(node2).neighbour.put(node1, cost);
            ArrayList<Edge<T>> fromList = edges.get(node1);
            ArrayList<Edge<T>> toList = edges.get(node2);
            Edge<T> edge1 = new Edge<T>(node2, cost);
            Edge<T> edge2 = new Edge<T>(node1, cost);
            fromList.add(edge1);
            toList.add(edge2);
            return true;
        }

        return false;
    }


    @Override
    public boolean isConnected(T node1, T node2) {
        Node n1 = map.get(node1);
        return (n1 != null) && n1.neighbour.containsKey(node2);
    }

    @Override
    public int getCost(T node1, T node2) {
        Integer value = null;
        if(map.get(node1) != null)
            value = map.get(node1).neighbour.get(node2);
        return (value != null) ? value : -1;

    }

    @Override
    public List<T> depthFirstSearch(T start, T end) {
        HashSet<T> tried = new HashSet<>();
        List<T> path = new Stack<>();
        depthFirst(start, end, (Stack) path, tried);
        return path;
    }

    private void depthFirst(T start, T end, Stack<T> path, HashSet<T> tried){
        Node<T> current = map.get(start);
        tried.add(start);
        path.push(start);
        Iterator<Map.Entry<T, Integer>> children = current.neighbour.entrySet().iterator();
        if(tried.contains(end))
            return;
        while(children.hasNext()){
            Map.Entry<T, Integer> currentNode = children.next();
            if(tried.contains(end))
                return;
            if(!tried.contains(currentNode.getKey()))
                depthFirst(currentNode.getKey(), end, path, tried);
            if(tried.contains(end))
                return;
        }
        path.pop();

    }

    @Override
    public List<T> breadthFirstSearch(T start, T end) {
        HashMap<T, T> parentData = new HashMap<>();
        LinkedList<T> currentNodeId = new LinkedList<>();
        parentData.put(start, null);
        currentNodeId.addLast(start);

        T currentId;
        while(!currentNodeId.isEmpty()){
            T tempChildId;
            currentId = currentNodeId.removeFirst();
            currentNodeId.addAll(currentNodeId.size(), map.get(currentId).neighbour.keySet());
            LinkedList<T> children = new LinkedList<>();
            children.addAll(map.get(currentId).neighbour.keySet());
            while(!children.isEmpty()){
                tempChildId = children.removeFirst();
                if(!parentData.containsKey(tempChildId))
                    parentData.put(tempChildId, currentId);

                if(tempChildId.equals(end))
                    return shortestPath(parentData, start, end);
            }
        }
        return shortestPath(parentData, start, end);
    }

    private List<T> shortestPath(HashMap<T, T> parentData, T start, T end){
        List<T> shortestPath = new LinkedList<>();
        shortestPath.add(0, end);
        T next = end;
        if(parentData.get(end) != null)
            while((next = parentData.get(next)) != null)
                shortestPath.add(0, next);
        return shortestPath;

    }

    @Override
    public alda.graph.UndirectedGraph<T> minimumSpanningTree() {

        Set<T> tried = new HashSet<T>();
        Map<T, T> pre = new HashMap<T, T>();
        Map<T, Integer> distances = new HashMap<T, Integer>();
        Map<T, Boolean> determined = new HashMap<T, Boolean>();

        PriorityQueue<Edge<T>> priorityQueue = new PriorityQueue<Edge<T>>();
        alda.graph.UndirectedGraph graph = new MyUndirectedGraph();

        depthSearch(this, from, tried);
        for(T n : tried){
            distances.put(n, Integer.MAX_VALUE);
            determined.put(n, false);
            pre.put(n, null);
        }
        distances.put(from, 0);
        determined.put(from,true);
        for(T data : tried){
            graph.add(data);
        }
        return make(from, tried, pre, distances, determined, priorityQueue, graph);
    }

    private alda.graph.UndirectedGraph<T> make(T from, Set<T> visited, Map<T, T> pre, Map<T, Integer> distance, Map<T, Boolean> determined, PriorityQueue<Edge<T>> priorityQueue, alda.graph.UndirectedGraph<T> graph) {
        Edge<T> edge;
        for (Edge<T> e : this.getEdges(from)) {
            if (!determined.get(e.getDestination())) {
                int p = e.getWeight();
                if (p < distance.get(e.getDestination())) {
                    priorityQueue.add(e);
                    distance.put(e.getDestination(), p);
                }

            }
        }
        edge = priorityQueue.poll();
        if (edge == null)
            return graph;
        if(!determined.get(edge.getDestination())){
            determined.put(edge.getDestination(), true);
            pre.put(edge.getDestination(), from);
            graph.connect(from, edge.getDestination(), edge.getWeight());
        }
        return make(edge.getDestination(), visited, pre, distance, determined, priorityQueue, graph);

    }

    private void depthSearch(MyUndirectedGraph<T> map, T from, Set<T> visited){
        visited.add(from);
        for(Edge<T> e : edges.get(from)){
            T destination = e.getDestination();
            if(!visited.contains(destination))
                depthSearch(map, destination, visited);
        }
    }

    private ArrayList<Edge<T>> getEdges(T from){
        return new ArrayList<Edge<T>>(edges.get(from));
    }




}
