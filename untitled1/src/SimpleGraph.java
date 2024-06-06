import java.util.*;

public class SimpleGraph {
    private int V;
    private List<List<int[]>> adjList;

    public SimpleGraph(int V) {
        this.V = V;
        adjList = new ArrayList<>(V);
        for (int i = 0; i < V; ++i)
            adjList.add(new ArrayList<>());
    }

    public void addEdge(int u, int v, int weight) {
        adjList.get(u).add(new int[]{v, weight});
        adjList.get(v).add(new int[]{u, weight});
    }

    public List<int[]> kruskalMST() {
        List<int[]> mst = new ArrayList<>();
        PriorityQueue<int[]> edges = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
        for (int u = 0; u < V; ++u) {
            for (int[] neighbor : adjList.get(u)) {
                int v = neighbor[0];
                int weight = neighbor[1];
                edges.offer(new int[]{u, v, weight});
            }
        }

        DisjointSet ds = new DisjointSet(V);
        while (!edges.isEmpty() && mst.size() < V - 1) {
            int[] edge = edges.poll();
            int u = edge[0];
            int v = edge[1];
            if (ds.find(u) != ds.find(v)) {
                mst.add(edge);
                ds.union(u, v);
            }
        }

        return mst;
    }

    public List<int[]> primMST() {
        List<int[]> mst = new ArrayList<>();
        boolean[] visited = new boolean[V];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.offer(new int[]{0, 0});

        while (!pq.isEmpty() && mst.size() < V - 1) {
            int[] current = pq.poll();
            int u = current[0];
            if (visited[u]) continue;
            visited[u] = true;

            for (int[] neighbor : adjList.get(u)) {
                int v = neighbor[0];
                int weight = neighbor[1];
                if (!visited[v]) {
                    pq.offer(new int[]{v, weight});
                }
            }
            if (current[1] != 0) {
                mst.add(current);
            }
        }

        return mst;
    }

    public void printMST(List<int[]> mst, String method) {
        System.out.println("Minimalne drzewo rozpinające (MST) znalezione metodą " + method + ":");
        for (int[] edge : mst) {
            if (edge.length == 3) {
                System.out.println(edge[0] + " - " + edge[1] + ", waga: " + edge[2]);
            } else if (edge.length == 2) {
                System.out.println(edge[0] + " - " + edge[1]);
            }
        }
    }



    public static void main(String[] args) {
        int V = 4;
        SimpleGraph graph = new SimpleGraph(V);

        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 15);
        graph.addEdge(1, 2, 20);

        List<int[]> kruskalMST = graph.kruskalMST();
        graph.printMST(kruskalMST, "Kruskal");

        List<int[]> primMST = graph.primMST();
        graph.printMST(primMST, "Prima");
    }
}

class DisjointSet {
    int[] parent;
    int[] rank;

    public DisjointSet(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) return;
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }
}
