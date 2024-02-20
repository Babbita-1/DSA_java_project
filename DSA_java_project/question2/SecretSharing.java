package question2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecretSharing {

    public static void main(String[] args) {
        int n = 5;
        int[][] intervals = {{0, 2}, {1, 3}, {2, 4}};
        int firstPerson = 0;
        List<Integer> secretIndividuals = findIndividualsWithSecret(n, intervals, firstPerson);
        System.out.println("Individuals knowing the secret: " + secretIndividuals);
    }

    private static List<Integer> findIndividualsWithSecret(int n, int[][] intervals, int firstPerson) {
        boolean[] reached = new boolean[n];
        Arrays.fill(reached, false);

        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];

            for (int i = start; i <= end; i++) {
                if (i == firstPerson || !reached[i]) {
                    reached[i] = true;
                }
            }
        }

        return getIndividualsWithSecret(reached);
    }

    private static List<Integer> getIndividualsWithSecret(boolean[] reached) {
        List<Integer> individualsWithSecret = new ArrayList<>();

        for (int i = 0; i < reached.length; i++) {
            if (reached[i]) {
                individualsWithSecret.add(i);
            }
        }

        return individualsWithSecret;
    }
}