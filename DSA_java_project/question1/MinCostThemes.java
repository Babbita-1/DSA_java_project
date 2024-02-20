package question1;

public class MinCostThemes {
    public static int minimunCostToDecorate(int[][] kharcha) {
        
        if (kharcha == null || kharcha[0].length == 0) //checking the validation
            return 0;

       
        int k = kharcha[0].length; //  number of themes
        int n = kharcha.length; // venues number

        int[][] dp = new int[n][k]; //initaililzing array to store minimum cost

        
        for (int i = 0; i < n; i++) { // checking  over venues
            for (int j = 0; j < k; j++) { // checking over themes
                dp[i][j] = kharcha[i][j]; //initialinzing cost to decorate current venue with current theme

                
                if (i > 0) { // If not the first venue
                    int previousMinKharcha = Integer.MAX_VALUE; // initializing max(infinity) 
                    for (int x = 0; x < k; x++) { // Iterate over themes of the previous venue
                        if (x != j) { // Exclude the current theme
                            previousMinKharcha = Math.min(previousMinKharcha, dp[i - 1][x]); // Update the minimum cost
                        }
                    }
                    dp[i][j] += previousMinKharcha; // Update the current cost by adding the minimum cost of the previous venue with a different theme
                }
            }
        }

        int minKharcha = Integer.MAX_VALUE; // Initialize the minimum cost
        for (int j = 0; j < k; j++) { // Iterate over themes of the last venue
            minKharcha = Math.min(minKharcha, dp[n - 1][j]); // Find the minimum cost among all themes
        }

        return minKharcha; // Return the minimum cost of decorating all venues
    }

    public static void main(String[] args) {
        int[][] kharcha = {{2, 3, 1}, {4, 6, 8}, {3, 2, 5}};
        System.out.println(minimunCostToDecorate(kharcha)); 
    }
}