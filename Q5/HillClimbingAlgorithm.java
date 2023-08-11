package Q5;

import java.util.Random;

public class HillClimbingAlgorithm {

    private static double evaluate(double x) {
        // Hypothetical function to optimize (you can replace this with your function)
        return -(x - 5) * (x - 5); // Minimize (maximize if you change the sign)
    }

    private static double getRandomNeighbor(double x, double stepSize) {
        Random random = new Random();
        double offset = random.nextDouble() * stepSize;
        if (random.nextBoolean()) {
            return x + offset;
        } else {
            return x - offset;
        }
    }

    public static double hillClimbing(double initialX, double stepSize, int maxIterations) {
        double currentX = initialX;
        double currentValue = evaluate(currentX);

        for (int i = 0; i < maxIterations; i++) {
            double neighborX = getRandomNeighbor(currentX, stepSize);
            double neighborValue = evaluate(neighborX);

            if (neighborValue > currentValue) {
                currentX = neighborX;
                currentValue = neighborValue;
            }
        }

        return currentX;
    }

    public static void main(String[] args) {
        double initialX = 0; // Initial solution
        double stepSize = 0.1; // Step size for random neighbors
        int maxIterations = 100; // Maximum number of iterations

        double solution = hillClimbing(initialX, stepSize, maxIterations);
        double optimumValue = evaluate(solution);

        System.out.println("Optimal Solution: " + solution);
        System.out.println("Optimal Value: " + optimumValue);
    }
}
