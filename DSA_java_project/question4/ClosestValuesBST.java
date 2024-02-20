package question4;

import java.util.PriorityQueue;

class TreeNode {
    double val;
    TreeNode left;
    TreeNode right;

    TreeNode(double val) {
        this.val = val;
        left = null;
        right = null;
    }
}

public class ClosestValuesBST {

    public static int[] closestValues(TreeNode root, double target, int k) {
        // Create a PriorityQueue to store the closest values
        PriorityQueue<Double> pq = new PriorityQueue<>();

        // Call the helper method to add the closest values to the PriorityQueue
        closestValuesHelper(root, pq, target, k);

        // Create an array to store the k closest values
        int[] result = new int[k];

        // Extract the k closest values from the PriorityQueue and store them in the array
        for (int i = 0; i < k; i++) {
            result[i] = (int) Math.floor(pq.poll());
        }

        return result;
    }

    // Helper method to traverse the binary search tree and add the closest values to the PriorityQueue
    private static void closestValuesHelper(TreeNode node, PriorityQueue<Double> pq, double target, int k) {
        if (node == null) {
            return;
        }

        // Traverse the left subtree
        closestValuesHelper(node.left, pq, target, k);

        // If the PriorityQueue has fewer than k elements, add the current node value
        if (pq.size() < k) {
            pq.add(node.val);
        }
        // If the current node value is closer to the target than the smallest value in the PriorityQueue, replace the smallest value
        else if (Math.abs(node.val - target) < Math.abs(pq.peek() - target)) {
            pq.poll();
            pq.add(node.val);
        }

        // Traverse the right subtree
        closestValuesHelper(node.right, pq, target, k);
    }

    public static void main(String[] args) {
        // Construct the binary search tree
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(3);
        root.right = new TreeNode(7);
        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(4);
        root.right.right = new TreeNode(8);

        double target = 3.8;
        int k = 2;

        int[] result = closestValues(root, target, k);
        System.out.println("Closest values to " + target + " are: ");
        for (int value : result) {
            System.out.print(value + " ");
        }
    }
}