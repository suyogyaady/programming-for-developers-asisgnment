/*
a)  
A trio of friends planned to purchase clothing from a particular store for an upcoming party, intending to wear 
matching outfits in varying colors - black, blue, and pink. The store had three different clothing sets on display, 
each in a different color. The shopkeeper assisted the three friends by selecting a clothing set in the appropriate 
color for each person based on their body shape and size. Given a 2D array, price[][3], where price[i][0], 
price[i][1], and price[i][2] represent the price of each clothing set for a different colored outfit for person i, your 
objective is to determine the minimum cost required to purchase clothing such that each person wears  have 
different color clothes if they stand in a row. 
 
It should be noted that any two people can wear the same color cloth, but the third person must wear various 
color cloths, and all three can wear different colored garments. 
Input: N = 3, price[][3] = [{14, 4, 11}, {11, 14, 3}, {14, 2, 10}] 
Output: 9 
Explanation:  
Person 1 chooses blue clothing cost=4. Person 2 chooses pink clothing. Cost = 3. Person 3 chooses blue 
clothing. Cost = 2. 
As a result, the total cost = 2 + 5 + 3 = 9. 
Note: algorithm must take 
Time Complexity: O(N) 
Auxiliary Space: O(1) 
*/


package Q1;

import java.util.Scanner;

public class MinimumCostClothing {

    // Method to calculate the minimum cost of clothing for a group of people
    public static int minimumCost(int N, int[][] price) {
        // Create a 2D array to store the minimum cost for each person and clothing type
        int[][] dp = new int[N][3];

        // Initialize the base cases for the first person
        dp[0][0] = price[0][0];
        dp[0][1] = price[0][1];
        dp[0][2] = price[0][2];

        // Iterate from the second person to the last person
        for (int i = 1; i < N; i++) {
            // Calculate the minimum cost for each clothing type for the current person
            dp[i][0] = price[i][0] + Math.min(dp[i - 1][1], dp[i - 1][2]);
            dp[i][1] = price[i][1] + Math.min(dp[i - 1][0], dp[i - 1][2]);
            dp[i][2] = price[i][2] + Math.min(dp[i - 1][0], dp[i - 1][1]);
        }

        // Return the minimum cost for the last person, considering all clothing types
        return Math.min(dp[N - 1][0], Math.min(dp[N - 1][1], dp[N - 1][2]));
    }

    // Main method to handle user input and display output
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter the number of people
        System.out.print("Enter the value of N: ");
        int N = scanner.nextInt();

        // Create a 2D array to store the price of clothing for each person
        int[][] price = new int[N][3];

        // Prompt the user to enter the clothing prices for each person
        System.out.println("Enter the price array:");
        for (int i = 0; i < N; i++) {
            // Prompt for each person's clothing prices
            System.out.print("Enter the price for person " + (i + 1) + ": ");
            for (int j = 0; j < 3; j++) {
                // Read and store the clothing prices
                price[i][j] = scanner.nextInt();
            }
        }

        // Calculate the minimum cost using the minimumCost method
        int minimumCost = minimumCost(N, price);

        // Display the calculated minimum cost to the user
        System.out.println("Minimum cost: " + minimumCost);
    }
}