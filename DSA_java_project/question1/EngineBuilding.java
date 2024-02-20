package question1;

import java.util.Arrays;
public class EngineBuilding{
    public static int timeForEngine(int[] engine, int splitCost) {
        int Engine = engine.length; //number of engine
        int[] dp = new int[Engine + 1]; // creates an array to store minimun time to build i engines

        Arrays.fill(dp, Integer.MAX_VALUE); //fiiling integer with Max(infinity) 
        dp[0] = 0; //setting the minimum time to 1st engine to 0

        for (int i = 1; i <= Engine; i++) { //loop through each engine
            dp[i] = engine[i - 1] + splitCost; // time to build one engine and + split cost
            for (int j = 1; j < i; j++) {  // loop through each possible split point
                dp[i] = Math.min(dp[i], dp[j] + dp[i - j]); // updating minimum time by choosing minimum spilt 
            }
        }
        return dp[Engine]; //return the minimum time to build all engine
    }

    public static void main(String[] args) {
        int[] engine = {1,2,3};
        int splitCost = 1; //cost to split

        int minTime = timeForEngine(engine, splitCost);
        System.out.println("Minimum time to build all engine: " + minTime);
    }
}

