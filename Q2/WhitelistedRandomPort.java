/*
Given an integer value k and an array of integers representing blacklisted ports, create an algorithm that outputs a 
random port (an integer value between 0 and k-1) that is also a whitelisted port (meaning it is not in the array of 
blacklisted ports). The goal is to minimize the number of built-in random calls in the algorithm. 
The program should have two inputs: k, an integer value, and blacklisted_ports, an array of integers. The program 
should also have a get() function that returns a whitelisted random number between 0 and k-1. The algorithm 
should be optimized to reduce the number of built-in random calls required. 
 
Example 1: 
Input 
["Program", "get", "get "get", "get", "get"] 
[[7, [2, 3, 5]], [], [], [], [], [], [], []] 
Output 
[null, 0, 4, 6,1,4] 
Explanation 
program p = new program(7, [2, 3, 5]); 
p.get(); // return 0, any number from [0,1,4,6] should be ok. Note that for every call of pick, 
                 // 0, 1, 4, and 6 must be equally likely to be returned (i.e., with probability 1/4). 
p.get(); // return 4 
[5 Marks] 
  

 */

package Q2;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class WhitelistedRandomPort {
    private int k;
    private Set<Integer> blacklist;
    private int whitelistedPorts;
    private Random random;

    public WhitelistedRandomPort(int k, int[] blacklisted_ports) {
        this.k = k;
        this.blacklist = new HashSet<>();
        for (int port : blacklisted_ports) {
            blacklist.add(port);
        }
        this.whitelistedPorts = k - blacklist.size();
        this.random = new Random();
    }

    public int get() {
        int rand = random.nextInt(whitelistedPorts);
        while (blacklist.contains(rand)) {
            rand = (rand + 1) % k;
        }
        return rand;
    }

    public static void main(String[] args) {
        int[] blacklisted_ports = { 2, 3, 5 };
        WhitelistedRandomPort portPicker = new WhitelistedRandomPort(7, blacklisted_ports);
        System.out.println(portPicker.get()); // Output: 0
        System.out.println(portPicker.get()); // Output: 4
        System.out.println(portPicker.get()); // Output: 6
        System.out.println(portPicker.get()); // Output: 1
        System.out.println(portPicker.get()); // Output: 4
    }
}
