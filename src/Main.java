import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        Graph g = new Graph();
        g.printAdjacencyList();

        Dijkstra d = new Dijkstra(g.getAdjacencyList(), g.getNumberOfVertices());
        d.run(1);
        d.printDistances();

        g.drawGraph();
    }
}
