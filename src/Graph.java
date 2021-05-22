import java.awt.*;
import java.util.*;

public class Graph {
    private int numberOfVertices;
    private int width;
    private int height;
    private Set<Vertex> vertices;
    private Map<Integer, Set<Vertex>> adjacencyList;

    public Graph(int numberOfNodes, int width, int height) {
        this.numberOfVertices = numberOfNodes;
        this.width = width;
        this.height = height;

        vertices = new LinkedHashSet<>();
        populateVertices();

        adjacencyList = new LinkedHashMap<>();
        populateAdjacencyList();
    }

    private void populateVertices() {
        Random r = new Random();
        int id = 1;
        while (vertices.size() != numberOfVertices) {
            int scale = (int) Math.pow(10, 1);
            double x = r.nextDouble() * width;
            double y = r.nextDouble() * height;
            x = Math.floor(x * scale) / scale;
            y = Math.floor(y * scale) / scale;

            vertices.add(new Vertex(id++, x, y));
        }
    }

    private void populateAdjacencyList() {
        for (int i = 1; i <= numberOfVertices; i++) {
            adjacencyList.put(i, new HashSet<>());
        }

        Object[] temp = vertices.toArray();
        for (int i = 1; i <= numberOfVertices; i++) {
            Vertex v1 = (Vertex) temp[i - 1];

            for (Vertex v2 : vertices) {
                if (v1.equals(v2))
                    continue;

                double cost = calculateEdgeCost(v1, v2);
                if (new Random().nextBoolean()) {
                    Set<Vertex> tmp1 = adjacencyList.get(v1.getId());
                    Set<Vertex> tmp2 = adjacencyList.get(v2.getId());

                    if (!tmp1.contains(v2))
                        tmp1.add(new Vertex(v2, cost));

                    if (!tmp2.contains(v1))
                        tmp2.add(new Vertex(v1, cost));
                }
            }
        }
    }

    private double calculateEdgeCost(Vertex v1, Vertex v2) {
        double xAxis1 = v1.getxAxis();
        double yAxis1 = v1.getyAxis();
        double xAxis2 = v2.getxAxis();
        double yAxis2 = v2.getyAxis();
        return Math.sqrt(((xAxis1 - xAxis2) * (xAxis1 - xAxis2)) + ((yAxis1 - yAxis2) * (yAxis1 - yAxis2)));
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    public void setNumberOfVertices(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Set<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(Set<Vertex> vertices) {
        this.vertices = vertices;
    }

    public Map<Integer, Set<Vertex>> getAdjacencyList() {
        return adjacencyList;
    }

    public void setAdjacencyList(Map<Integer, Set<Vertex>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public void printAdjacencyList() {
        System.out.println("\n==========Adjacency List==========");
        for (Integer i : adjacencyList.keySet()) {
            System.out.print(i + ": ");
            for (Vertex v : adjacencyList.get(i)) {
                System.out.print(v.getId() + ", ");
            }
            System.out.println();
        }
    }

    public void drawGraph() {
        GraphGUI graph = new GraphGUI();
        graph.setGraphWidth(width);
        graph.setGraphHeight(height);
        graph.setVertices(vertices);
        graph.setAdjList(adjacencyList);
        graph.setPreferredSize(new Dimension(960, 800));
        Thread graphThread = new Thread(graph);
        graphThread.start();
    }
}
