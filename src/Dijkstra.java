import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Dijkstra {
    private double distance[];
    private Set<Integer> settled;
    private PriorityQueue<Vertex> pq;
    private int V;
    private Map<Integer, Set<Vertex>> adj;
    private final DecimalFormat df = new DecimalFormat("0.00");

    public Dijkstra(Map<Integer, Set<Vertex>> adj, int V) {
        this.V = V;
        distance = new double[V];
        settled = new HashSet<>();
        pq = new PriorityQueue<>(V, new Vertex());
        this.adj = adj;
    }

    public void run(int src) {
        for (int i = 0; i < V; i++)
            distance[i] = Integer.MAX_VALUE;

        pq.add(new Vertex(src, 0));
        distance[src - 1] = 0;

        while (settled.size() != V) {
            int u = pq.remove().getId();
            settled.add(u);
            e_Neighbours(u);
        }
    }

    private void e_Neighbours(int u) {
        double edgeDistance;
        double newDistance;

        for (Vertex v : adj.get(u)) {
            if (!settled.contains(v.getId())) {
                edgeDistance = v.getCost();
                newDistance = distance[u - 1] + edgeDistance;

                if (newDistance < distance[v.getId() - 1])
                    distance[v.getId() - 1] = newDistance;

                pq.add(new Vertex(v.getId(), distance[v.getId() - 1]));
            }
        }
    }

    public void printDistances() {
        System.out.println("\nShortest path from vertex: ");

        for (int i = 0; i < distance.length; i++)
            System.out.println("1 to " + (i + 1) + " is " + df.format(distance[i]));
    }
}
