import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter amount of nodes:");
        int totalNodes = s.nextInt();

        System.out.println("Enter width of the graph:");
        int width = s.nextInt();

        System.out.println("Enter height of the graph:");
        int height = s.nextInt();

        Graph g = new Graph(totalNodes, width, height);
        g.printAdjacencyList();
        g.drawGraph();


    }
}
