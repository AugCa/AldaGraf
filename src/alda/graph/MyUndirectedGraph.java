package alda.graph;

import java.util.*;

public class MyUndirectedGraph<T> implements alda.graph.UndirectedGraph<T> {
    private int noOfNodes;
    private int noOfEdges;
    private Map<T, Node<T>> map = new HashMap<>();
    private ArrayList<T> edges = new ArrayList<>();

    private static class Node<T>{
        public T data;
        public HashMap<T, Integer> neighbour = new HashMap<>();
        public Node (T data){
            this.data = data;
        }

    }



    @Override
    public int getNumberOfNodes() {
        return noOfNodes;
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
        map.put(newNode,node);
        noOfNodes++;
        return true;


    }

    @Override
    public boolean connect(T node1, T node2, int cost) {
        if(cost <= 0)
            return false;

        if(map.containsKey(node1) && map.containsKey(node2) ) {
            map.get(node1).neighbour.put(node2, cost);
            map.get(node2).neighbour.put(node1, cost);
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
        return null;
    }

    @Override
    public List<T> breadthFirstSearch(T start, T end) {
        return null;
    }

    @Override
    public alda.graph.UndirectedGraph<T> minimumSpanningTree() {
        return null;
    }
}
