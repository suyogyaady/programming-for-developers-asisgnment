/*
Question 3 
a) Suppose you are provided an array of n targets that are placed in a row from 0 to n-1. Each target is assigned 
with certain integer such that a [0] represent the value associated with target zero. You are asked to shoot all 
the targets. If you shoot I th target then you will get a[i-1]*a[i]*a[i+1] points. 
Note that if i-1 and i+1 position hits index out of bound, then you can assume that two target with value 1 are 
padded before start target and after end target.  
Return maximum point one can gain by hitting each target. 
 
Input: a = [3,1,5,8] 
Output: 167 
Explanation: 
a = [3,1,5,8] 
[3,1,5,8]  points 3*1*5 (“hitting target with value 1”) 
[3,5,8] points 3*5*8 (“hitting target with value 5”) 
[3,8]  points 1*3*8 (“hitting target with value 3”) note that there is padded target with value 1 at beginning and end 
of the provided target list 
,[8]  points 1*8*1 same as above 
points =      3*1*5+   3*5*8   +  1*3*8  + 1*8*1 = 167 
 */

package Q3;

public class MaximumPointFromTarget {
    public static int getMaxPoints(int[] a) {
        int n = a.length;
        int[] paddedTargets = new int[n + 2]; // Padded array with two additional targets with value 1
        paddedTargets[0] = paddedTargets[n + 1] = 1;
        for (int i = 0; i < n; i++) {
            paddedTargets[i + 1] = a[i];
        }

        int[][] dp = new int[n + 2][n + 2];
        for (int len = 1; len <= n; len++) {
            for (int left = 1; left <= n - len + 1; left++) {
                int right = left + len - 1;
                for (int k = left; k <= right; k++) {
                    dp[left][right] = Math.max(dp[left][right],
                            dp[left][k - 1] + paddedTargets[left - 1] * paddedTargets[k] * paddedTargets[right + 1] + dp[k + 1][right]);
                }
            }
        }

        return dp[1][n];
    }

    public static void main(String[] args) {
        int[] a = {3, 1, 5, 8};
        int output = getMaxPoints(a);
        System.out.println(output); // Output: 167
    }
}
