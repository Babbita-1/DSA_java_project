package question2;

public class SewingMachines {

    public static void main(String[] args) {
        int[] input = {2, 1, 3, 0, 2};
        System.out.println("Minimum number of moves: " + findMinMoves(input));
    }

    private static int findMinMoves(int[] machines) {
        int totalDresses = 0;
        int maxDifference = 0;
        for (int dresses : machines) {
            totalDresses += dresses;
        }

        if (totalDresses % machines.length != 0) {
            return -1;
        }

        int targetDressesPerMachine = totalDresses / machines.length;
        for (int i = 0; i < machines.length; i++) {
            int currentDresses = machines[i];
            maxDifference = Math.max(maxDifference, Math.abs(currentDresses - targetDressesPerMachine));
            if (i < machines.length - 1) {
                currentDresses++;
                maxDifference = Math.max(maxDifference, Math.abs(currentDresses - targetDressesPerMachine));
            }
        }

        return maxDifference;
    }
}