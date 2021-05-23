import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter amount of nodes:");
        int totalNodes = s.nextInt();

        Graph g = new Graph(totalNodes);
        g.printAdjacencyList();
        g.drawGraph();
    }
}
