package question3;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

class Edge implements Comparable<Edge> {
    int u, v, weight;

    public Edge(int u, int v, int weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }
}

public class KruskalMST {

    private int find(int[] parent, int node) {
        if (parent[node] != node) {
            parent[node] = find(parent, parent[node]);
        }
        return parent[node];
    }

    private void union(int[] parent, int rank[], int x, int y) {
        int xRoot = find(parent, x);
        int yRoot = find(parent, y);

        if (rank[xRoot] < rank[yRoot]) {
            parent[xRoot] = yRoot;
            rank[yRoot] += rank[xRoot];
        } else {
            parent[yRoot] = xRoot;
            rank[xRoot] += rank[yRoot];
        }
    }

    public List<Edge> getMinimumSpanningTree(List<Edge> edges, int vertices) {
        int[] parent = new int[vertices];
        int[] rank = new int[vertices];

        for (int i = 0; i < vertices; i++) {
            parent[i] = i;
            rank[i] = 1;
        }

        PriorityQueue<Edge> pq = new PriorityQueue<>();
        for (Edge edge : edges) {
            pq.add(edge);
        }

        List<Edge> mst = new ArrayList<>();
        while (!pq.isEmpty() && mst.size() < vertices - 1) {
            Edge edge = pq.poll();
            int xRoot = find(parent, edge.u);
            int yRoot = find(parent, edge.v);

            if (xRoot != yRoot) {
                mst.add(edge);
                union(parent, rank, xRoot, yRoot);
            }
        }

        return mst;
    }

    public static void main(String[] args) {
        List<Edge> edges = new ArrayList<>();
        addEdge(edges, 0, 1, 4);
        addEdge(edges, 0, 7, 8);
        addEdge(edges, 1, 2, 8);
        addEdge(edges, 1, 7, 11);
        addEdge(edges, 2, 3, 7);
        addEdge(edges, 2, 8, 2);
        addEdge(edges, 2, 5, 4);
        addEdge(edges, 3, 4, 9);
        addEdge(edges, 3, 5, 14);
        addEdge(edges, 4, 5, 10);
        addEdge(edges, 5, 6, 2);
        addEdge(edges, 6, 7, 1);
        addEdge(edges, 6, 8, 6);
        addEdge(edges, 7, 8, 7);

        int vertices = 9;
        KruskalMST kruskalMST = new KruskalMST();
        List<Edge> mst = kruskalMST.getMinimumSpanningTree(edges, vertices);

        System.out.println("Minimum Spanning Tree:");
        for (Edge edge : mst) {
            System.out.printf("(%d, %d) -> %d\n", edge.u, edge.v, edge.weight);
        }
    }

    private static void addEdge(List<Edge> edges, int u, int v, int weight) {
        edges.add(new Edge(u, v, weight));
    }
}