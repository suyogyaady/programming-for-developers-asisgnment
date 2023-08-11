package Q3;

import java.util.*;

class MaxHeap {
    private List<Integer> heap;

    public MaxHeap() {
        heap = new ArrayList<>();
    }

    public void push(int val) {
        heap.add(val);
        siftUp(heap.size() - 1);
    }

    public int pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }

        int maxValue = heap.get(0);
        int lastValue = heap.remove(heap.size() - 1);

        if (!isEmpty()) {
            heap.set(0, lastValue);
            siftDown(0);
        }

        return maxValue;
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    private void siftUp(int index) {
        int parent = (index - 1) / 2;

        while (index > 0 && heap.get(index) > heap.get(parent)) {
            swap(index, parent);
            index = parent;
            parent = (index - 1) / 2;
        }
    }

    private void siftDown(int index) {
        int leftChild = index * 2 + 1;
        int rightChild = index * 2 + 2;
        int largest = index;

        if (leftChild < heap.size() && heap.get(leftChild) > heap.get(largest)) {
            largest = leftChild;
        }

        if (rightChild < heap.size() && heap.get(rightChild) > heap.get(largest)) {
            largest = rightChild;
        }

        if (largest != index) {
            swap(index, largest);
            siftDown(largest);
        }
    }

    private void swap(int i, int j) {
        int temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}

class Edge {
    int src, dest, weight;

    public Edge(int src, int dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }
}

public class BellmanFord {
    private static final int INF = Integer.MAX_VALUE;

    public static int[] bellmanFord(List<Edge> graph, int vertices, int source) {
        int[] distances = new int[vertices];
        Arrays.fill(distances, INF);
        distances[source] = 0;

        for (int i = 0; i < vertices - 1; i++) {
            for (Edge edge : graph) {
                int u = edge.src;
                int v = edge.dest;
                int weight = edge.weight;
                if (distances[u] != INF && distances[u] + weight < distances[v]) {
                    distances[v] = distances[u] + weight;
                }
            }
        }

        // Check for negative weight cycles
        for (Edge edge : graph) {
            int u = edge.src;
            int v = edge.dest;
            int weight = edge.weight;
            if (distances[u] != INF && distances[u] + weight < distances[v]) {
                throw new IllegalArgumentException("Graph contains a negative weight cycle");
            }
        }

        return distances;
    }

    public static void main(String[] args) {
        // Example graph representation as an edge list
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 4));
        edges.add(new Edge(0, 2, 3));
        edges.add(new Edge(1, 3, 2));
        edges.add(new Edge(1, 2, 5));
        edges.add(new Edge(2, 3, 7));
        edges.add(new Edge(3, 4, 2));
        edges.add(new Edge(4, 0, 4));
        edges.add(new Edge(4, 1, 4));
        edges.add(new Edge(4, 5, 6));

        int vertices = 6;
        int sourceVertex = 0;

        try {
            int[] distances = bellmanFord(edges, vertices, sourceVertex);
            System.out.println("Shortest distances from source vertex " + sourceVertex + ": ");
            for (int i = 0; i < vertices; i++) {
                System.out.println("Vertex " + i + ": " + distances[i]);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
