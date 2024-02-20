package question4;
import java.util.LinkedList;
import java.util.Queue;

public class MazeSolver {

    static class State {
        int x, y, keys, steps;

        public State(int x, int y, int keys, int steps) {
            this.x = x;
            this.y = y;
            this.keys = keys;
            this.steps = steps;
        }
    }

    public static int minMovesToCollectKeys(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int targetKeys = 0;
        int allKeysMask = 0;

        int startX = 0, startY = 0;

        // Find the initial state and count the number of keys in the maze
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                char cell = grid[i][j];
                if (cell == 'S') {
                    startX = i;
                    startY = j;
                } else if (cell == 'E') {
                    targetKeys = countKeys(grid);
                } else if (Character.isLowerCase(cell)) {
                    allKeysMask |= (1 << (cell - 'a'));
                }
            }
        }

        Queue<State> queue = new LinkedList<>();
        boolean[][][] visited = new boolean[m][n][64]; // 64 is 2^6, representing all possible key combinations

        // Initialize the queue with the starting state
        queue.offer(new State(startX, startY, 0, 0));
        visited[startX][startY][0] = true;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            State current = queue.poll();

            // Check if all keys are collected and reached the exit
            if (current.keys == targetKeys) {
                return current.steps;
            }

            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];

                // Check if the new position is within bounds and not a wall
                if (newX >= 0 && newX < m && newY >= 0 && newY < n && grid[newX][newY] != 'W') {
                    char nextCell = grid[newX][newY];

                    // If it's a key, update the keys bitmask
                    if (Character.isLowerCase(nextCell)) {
                        int keyMask = current.keys | (1 << (nextCell - 'a'));
                        if (!visited[newX][newY][keyMask]) {
                            queue.offer(new State(newX, newY, keyMask, current.steps + 1));
                            visited[newX][newY][keyMask] = true;
                        }
                    }
                    // If it's a door, check if the corresponding key is available
                    else if (Character.isUpperCase(nextCell)) {
                        int doorMask = 1 << (nextCell - 'A');
                        if ((current.keys & doorMask) != 0 && !visited[newX][newY][current.keys]) {
                            queue.offer(new State(newX, newY, current.keys, current.steps + 1));
                            visited[newX][newY][current.keys] = true;
                        }
                    }
                    // If it's an empty path, continue exploring
                    else if (nextCell == 'P' && !visited[newX][newY][current.keys]) {
                        queue.offer(new State(newX, newY, current.keys, current.steps + 1));
                        visited[newX][newY][current.keys] = true;
                    }
                }
            }
        }

        // If we reach here, it's not possible to collect all keys and reach the exit
        return -1;
    }

    // Helper function to count the number of keys in the maze
    private static int countKeys(char[][] grid) {
        int count = 0;
        for (char[] row : grid) {
            for (char cell : row) {
                if (Character.isLowerCase(cell)) {
                    count++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        char[][] grid = {
                {'S', 'P', 'P', 'P'},
                {'W', 'P', 'P', 'E'},
                {'P', 'b', 'W', 'P'},
                {'P', 'P', 'P', 'P'}
        };

        int result = minMovesToCollectKeys(grid);
        System.out.println("Minimum moves to collect all keys and reach exit: " + result);
    }
}
