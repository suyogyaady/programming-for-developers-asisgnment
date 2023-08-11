package Q4;

import java.util.*;

public class MinimumStepsToCompleteTasks {

    public int minimumSteps(int n, int[][] prerequisites) {
        // Create the graph using adjacency lists
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        // Create an array to store the in-degree of each task
        int[] inDegree = new int[n];

        // Build the graph and calculate the in-degree of each task
        for (int[] prerequisite : prerequisites) {
            int x = prerequisite[0] - 1; // Adjusting 1-based indexing to 0-based indexing
            int y = prerequisite[1] - 1; // Adjusting 1-based indexing to 0-based indexing
            graph.get(x).add(y);
            inDegree[y]++;
        }

        // Perform topological sorting using a queue
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        int steps = 0;

        // Perform BFS to find the minimum steps needed to complete all tasks
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size > 0) {
                int current = queue.poll();
                size--;

                // Process the current task and reduce in-degree of its neighbors
                for (int neighbor : graph.get(current)) {
                    inDegree[neighbor]--;
                    if (inDegree[neighbor] == 0) {
                        queue.add(neighbor);
                    }
                }
            }
            steps++;
        }

        // Check if all tasks have been completed
        for (int degree : inDegree) {
            if (degree > 0) {
                return -1; // There is a cycle, so it's not possible to complete all tasks
            }
        }

        return steps - 1; // We subtract 1 because the last step doesn't count as completing any new task
    }

    public static void main(String[] args) {
        MinimumStepsToCompleteTasks mstct = new MinimumStepsToCompleteTasks();
        int n = 3;
        int[][] prerequisites = {{1, 3}, {2, 3}};
        int result = mstct.minimumSteps(n, prerequisites);
        System.out.println("Minimum number of steps needed to complete all tasks: " + result);
    }
}
