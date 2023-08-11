package Q5;

import java.util.ArrayList;
import java.util.List;

public class ReorientConnections {

    private static int dfs(int node, boolean[] visited, List<List<Integer>> adjList) {
        visited[node] = true;
        int edgesToReverse = 0;

        for (int neighbor : adjList.get(node)) {
            if (!visited[neighbor]) {
                edgesToReverse += dfs(neighbor, visited, adjList) + 1;
            }
        }

        return edgesToReverse;
    }

    public static int minReorientConnections(int n, int[][] connections) {
        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }

        for (int[] connection : connections) {
            int from = connection[0];
            int to = connection[1];
            adjList.get(from).add(to);
        }

        boolean[] visited = new boolean[n];
        int[] edgesToReverse = new int[n];

        // Calculate the number of edges to be reversed for each server
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                edgesToReverse[i] = dfs(i, visited, adjList);
            }
        }

        // Find the server with the maximum count of edges to be reversed
        int maxEdgesToReverse = 0;
        int maxEdgesServer = 0;
        for (int i = 0; i < n; i++) {
            if (edgesToReverse[i] > maxEdgesToReverse) {
                maxEdgesToReverse = edgesToReverse[i];
                maxEdgesServer = i;
            }
        }

        return maxEdgesToReverse;
    }

    public static void main(String[] args) {
        int n = 6;
        int[][] connections = {{0, 1}, {1, 3}, {2, 3}, {4, 0}, {4, 5}};

        int result = minReorientConnections(n, connections);
        System.out.println("Minimum number of edges to reverse: " + result); // Output: 3
    }
}
