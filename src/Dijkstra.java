import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dijkstra {
    private List<List<Node>> adj;
    private List<Node> nodes;
    private DijkstraGraph graph;
    private List<List<Integer>> paths;
    private int source, dest;

    public Dijkstra(List<List<Node>> adj, List<Node> nodes) {
        this.adj = adj;
        this.nodes = nodes;
        this.paths = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < adj.size(); i++) {
            for (Node n : adj.get(i)) {
                edges.add(new Edge(i, n.getId(), n.getCost()));
            }
        }

        this.graph = new DijkstraGraph(edges, nodes.size());
    }

    private void getRoute(int[] prev, int i, List<Integer> route) {
        if (i >= 0) {
            getRoute(prev, prev[i], route);
            route.add(i);
        }
    }

    public void runDijkstra(int source, int dest) {
        this.source = source;
        this.dest = dest;
        int N = nodes.size();

        PQ minHeap = new PQ();
        minHeap.add(nodes.get(source));

        List<Integer> dist = new ArrayList<>(Collections.nCopies(N, Integer.MAX_VALUE));
        dist.set(source, 0);

        boolean[] done = new boolean[N];
        done[source] = true;

        int[] prev = new int[N];
        prev[source] = -1;

        List<Integer> route = new ArrayList<>();

        while (!minHeap.isEmpty()) {
            Node node = minHeap.remove();

            int u = node.getId();

            for (Edge edge : graph.adjList.get(u)) {
                int v = edge.getEndPoint();
                long weight = edge.getWeight();

                if (!done[v] && (dist.get(u) + weight) < dist.get(v)) {
                    dist.set(v, (int) (dist.get(u) + weight));
                    prev[v] = u;
                    minHeap.add(new Node(v, dist.get(v)));
                }
            }
            done[u] = true;
        }

        for (int i = 0; i < N; i++) {
            getRoute(prev, i, route);
            paths.add(new ArrayList<>(route));
            route.clear();
        }
    }

    public List<Node> getPath() {
        List<Integer> path = paths.get(dest);
        List<Node> pathUsingNodes = new ArrayList<>();
        pathUsingNodes.add(nodes.get(source));

        for (int i = 0; i < path.size() - 1; i++) {
            for (Node n : adj.get(path.get(i))) {
                if (n.getId() == path.get(i + 1)) {
                    pathUsingNodes.add(n);
                    break;
                }
            }
        }

        return pathUsingNodes;
    }

    private static class DijkstraGraph {
        public List<List<Edge>> adjList;

        DijkstraGraph(List<Edge> edges, int N) {
            this.adjList = new ArrayList<>();

            for (int i = 0; i < N; i++) {
                adjList.add(new ArrayList<>());
            }

            for (Edge edge : edges) {
                adjList.get(edge.getStartPoint()).add(edge);
            }
        }
    }
}



