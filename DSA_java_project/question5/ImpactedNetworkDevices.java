package question5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class device{

    public static List<Integer> nodes(int[][] edges, int target) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        Map<Integer, Integer> inDegree = new HashMap<>();

        // Build the graph and calculate in-degree of each node
        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
        }

        // Perform DFS starting from the target node
        List<Integer> result = new ArrayList<>();
        dfs(graph, inDegree, target, target, result);

        return result;
    }

    private static void dfs(Map<Integer, List<Integer>> graph, Map<Integer, Integer> inDegree, int node, int target,
                            List<Integer> result) {
        // If the current node has no incoming edges other than from the target node,
        // add it to the result
        if (inDegree.getOrDefault(node, 0) == 1 && graph.get(target).contains(node)) {
            result.add(node);
            // Add child nodes recursively
            addChildren(graph, node, result);
        }

        // Recursively explore the children of the current node
        if (graph.containsKey(node)) {
            for (int child : graph.get(node)) {
                dfs(graph, inDegree, child, target, result);
            }
        }
    }
    private static void addChildren(Map<Integer, List<Integer>> graph, int node, List<Integer> result) {
        if (graph.containsKey(node)) {
            for (int child : graph.get(node)) {
                result.add(child);
                addChildren(graph, child, result); // Recursively add children of children
            }
        }
    }
    public static void main(String[] args) {
        int[][] edges = { { 0, 1 }, { 0, 2 }, { 1, 3 }, { 1, 6 }, { 2, 4 }, { 4, 6 }, { 4, 5 }, { 5, 7 } };
        int target = 4;

        List<Integer> uniqueParents = nodes(edges, target);

        System.out.println("Nodes whose only parent is " + target + ":");
        for (int node : uniqueParents) {
            System.out.println(node);
        }
    }
}