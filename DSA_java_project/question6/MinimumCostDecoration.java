
public class MinimumCostDecoration {
	
	public static int minCostToDecorateVenues(int[][] costs) {
        if (costs == null || costs.length == 0 || costs[0].length == 0) {
            return 0;
        }

        int n = costs.length;
        int k = costs[0].length;

        // Initialize a DP table to store the minimum cost for each venue and theme combination
        int[][] dp = new int[n][k];

        // Copy the costs for the first row
        for (int i = 0; i < k; i++) {
            dp[0][i] = costs[0][i];
        }

        // Iterate through each venue and calculate the minimum cost for each theme
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < k; j++) {
                // For each theme, calculate the minimum cost by considering the minimum cost of the previous row
                // with a different theme
                for (int l = 0; l < k; l++) {
                    if (j != l) {
                        dp[i][j] = Math.min(dp[i][j], dp[i - 1][l] + costs[i][j]);
                    }
                }
            }
        }

        // The minimum cost will be the minimum value in the last row of the DP table
        int minCost = Integer.MAX_VALUE;
        for (int cost : dp[n - 1]) {
            minCost = Math.min(minCost, cost);
        }

        return minCost;
    }

	public static void main(String[] args) {
        int[][] costMatrix = {{1, 3, 2}, {4, 6, 8}, {3, 1, 5}};
        int result = minCostToDecorateVenues(costMatrix);
        System.out.println("Minimum cost: " + result);
    }

}
