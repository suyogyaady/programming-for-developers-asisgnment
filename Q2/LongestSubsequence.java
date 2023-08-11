/* 
Given an integer array nums and another integer k, the goal is to find the longest subsequence of nums that 
satisfies the following two conditions: 
The subsequence is strictly decreasing. 
The difference between adjacent elements in the subsequence is at most k. 
The output should be the length of the longest subsequence that meets these requirements. 
For example, consider the following input: 
nums = [8,5,4, 2, 1, 4, 3, 4, 3, 1, 15] k = 3 
output=[8,5,4,2,1] or [8,5,4,3,1] 
Output: 5 
Explanation: 
The longest subsequence that meets the requirements is [8,5,4,2,1] or [8,5,4,3,1]. 
The subsequence has a length of 5, so we return 5. 
Note that the subsequence [1,3,4,5,8,15] does not meet the requirements because 15 - 8 = 7 is larger than 3. 
*/

package Q2;

public class LongestSubsequence {
    public static int longestSequence(int[] nums, int k) {

        // Initialize a dynamic programming array
        int[] dp = new int[nums.length];
        for (int i = 0; i < dp.length; i++) {
            dp[i] = 1;
        }

        // Iterate over the array
        for (int i = 1; i < nums.length; i++) {
            // Iterate over the previous elements
            for (int j = 0; j < i; j++) {
                // Check if the difference between the current element and the previous element
                // is less than or equal to k and nums[i] < nums[j]
                if (nums[j] - nums[i] <= k && nums[j] > nums[i]) {
                    // Check if the current element can be added to the previous element
                    if (dp[i] < dp[j] + 1) {
                        dp[i] = dp[j] + 1;
                    }

                }
            }
        }

        // Return the maximum value in the dp array
        int max = 0;
        for (int i = 0; i < dp.length; i++) {
            if (dp[i] > max) {
                max = dp[i];

            }
        }
        return max;

    }

    public static void sortDesc(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (nums[j] < nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {

        int[] nums = { 8, 5, 4, 2, 1, 4, 3, 4, 3, 1, 15 };
        int k = 3;
        int result = longestSequence(nums, k);

        System.out.println(result);
    }
}
