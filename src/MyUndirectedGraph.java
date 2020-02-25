import java.util.List;

public class MyUndirectedGraph<T> implements UndirectedGraph<T>{


    @Override
    public int getNumberOfNodes() {
        return 0;
    }

    @Override
    public int getNumberOfEdges() {
        return 0;
    }

    @Override
    public boolean add(T newNode) {
        return false;
    }

    @Override
    public boolean connect(T node1, T node2, int cost) {
        return false;
    }

    @Override
    public boolean isConnected(T node1, T node2) {
        return false;
    }

    @Override
    public int getCost(T node1, T node2) {
        return 0;
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
    public UndirectedGraph<T> minimumSpanningTree() {
        return null;
    }
}
