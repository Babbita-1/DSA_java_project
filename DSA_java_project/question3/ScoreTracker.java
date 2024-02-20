package question3;
import java.util.Collections;
import java.util.PriorityQueue;

// Class to track scores and calculate the median dynamically
public class ScoreTracker {
    private PriorityQueue<Double> minHeap; // Priority queue to store scores greater than median
    private PriorityQueue<Double> maxHeap; // Priority queue to store scores less than or equal to median

    // Constructor to initialize the minHeap and maxHeap
    public ScoreTracker() {
        minHeap = new PriorityQueue<>();
        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    }

    // Method to add a score to the tracker
    public void addScore(double score) {
        // Inserting the score into the appropriate heap based on its value
        if (maxHeap.isEmpty() || score <= maxHeap.peek()) {
            maxHeap.offer(score);
        } else {
            minHeap.offer(score);
        }

        // Balancing the heaps to maintain the median property
        balanceHeaps();
    }

    // Method to calculate and return the median score
    public double getMedianScore() {
        if (maxHeap.size() == minHeap.size()) {
            // If both heaps have equal size, average the top elements
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        } else {
            // Otherwise, return the top element of maxHeap
            return maxHeap.peek();
        }
    }

    // Method to balance the heaps to maintain the median property
    private void balanceHeaps() {
        while (maxHeap.size() > minHeap.size() + 1) {
            // Transfer elements from maxHeap to minHeap until their sizes differ by at most 1
            minHeap.offer(maxHeap.poll());
        }

        while (minHeap.size() > maxHeap.size()) {
            // Transfer elements from minHeap to maxHeap until their sizes are equal
            maxHeap.offer(minHeap.poll());
        }
    }

    // Main method to test the implementation
    public static void main(String[] args) {
        ScoreTracker scoreTracker = new ScoreTracker(); // Creating a new ScoreTracker instance

        // Adding scores to the tracker
        scoreTracker.addScore(85.5);
        scoreTracker.addScore(92.3);
        scoreTracker.addScore(77.8);
        scoreTracker.addScore(90.1);
        double median1 = scoreTracker.getMedianScore(); // Calculating the median after the first set of scores
        System.out.println("Median 1: " + median1); // Printing the median

        scoreTracker.addScore(81.2);
        scoreTracker.addScore(88.7);
        double median2 = scoreTracker.getMedianScore(); // Calculating the median after adding more scores
        System.out.println("Median 2: " + median2); // Printing the median
    }
}
