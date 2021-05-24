import java.awt.*;
import java.util.List;
import java.util.*;

public class Graph {
    private final double height = 100;
    private final double width = 100;
    private int numberOfNodes;
    private int startNode;
    private int endNode;

    private List<Node> nodes;
    private List<List<Node>> adjacencyList;
    private List<Set<Integer>> connectedNodes;

    private final Scanner SCAN = new Scanner(System.in);
    private final Random RANDOM = new Random();

    public static void main(String[] args) {
        Graph g = new Graph();
        g.prompt();

        Dijkstra dijkstra = new Dijkstra(g.getAdjacencyList(), g.getNodes());
        dijkstra.runDijkstra(g.getStartNode(), g.getEndNode());
        List<Node> path = dijkstra.getPath();

        System.out.println("\nResults:");
        System.out.println("Path from Src(" + g.getStartNode() + ") to Dest(" + g.getEndNode() + "): ");
        printPath(path);
        System.out.print("\nTotal Cost: ");
        printCost(path);

        g.drawGraph(path);
    }

    public static void printPath(List<Node> p) {
        if (p == null || p.size() == 0)
            return;

        System.out.print("[");
        for (int i = 0; i < p.size(); i++) {
            if (i != p.size() - 1)
                System.out.print(p.get(i).getId() + ":" + p.get(i).getCost() + " -> ");
            else
                System.out.println(p.get(i).getId() + ":" + p.get(i).getCost() + "]");
        }
    }

    public static void printCost(List<Node> p) {
        long total = 0;
        for (Node n : p)
            total += n.getCost();
        System.out.println(total);
    }

    public void prompt() {
        System.out.print("Enter the number of nodes: ");
        setNumberOfNodes(SCAN.nextInt());

        populateNodes();
        populateAdjacencyList();

        System.out.print("Select your Source Node: ");
        setStartNode(SCAN.nextInt());

        System.out.print("Select your Destination Node: ");
        setEndNode(SCAN.nextInt());
    }


    private void setNumberOfNodes(int numberOfNodes) {
        while (numberOfNodes <= 0) {
            System.out.println("Number of nodes must be greater than 0:");
            numberOfNodes = SCAN.nextInt();
        }
        this.numberOfNodes = numberOfNodes;
    }

    private void setStartNode(int startNode) {
        while (startNode < 0 || startNode >= numberOfNodes) {
            System.out.print("Invalid source node, try again: ");
            startNode = SCAN.nextInt();
        }
        this.startNode = startNode;
    }

    private void setEndNode(int endNode) {
        while (endNode == startNode || endNode < 0 || endNode >= numberOfNodes) {
            System.out.print("Invalid destination node, try again: ");
            endNode = SCAN.nextInt();
        }
        this.endNode = endNode;
    }

    public int getStartNode() {
        return startNode;
    }

    public int getEndNode() {
        return endNode;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<List<Node>> getAdjacencyList() {
        return adjacencyList;
    }

    private void executeDFS() {
        connectedNodes = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        Set<Integer> visited = new HashSet<>();

        Set<Integer> temp;
        for (int nodeID = 0; nodeID < this.adjacencyList.size(); nodeID++) {
            temp = new LinkedHashSet<>();
            stack.add(nodeID);

            while (!stack.isEmpty()) {
                Integer currentlyVisiting = stack.pop();

                if (!visited.contains(currentlyVisiting)) {
                    temp.add(currentlyVisiting);
                    visited.add(currentlyVisiting);
                }

                List<Node> edges = this.adjacencyList.get(currentlyVisiting);
                for (Node e : edges) {
                    if (e != null && !visited.contains(e.getId()))
                        stack.add(e.getId());
                }
            }
            if (!temp.isEmpty())
                connectedNodes.add(new LinkedHashSet<>(temp));
        }
    }

    private boolean isConnected() {
        if (connectedNodes.size() != 1)
            return false;
        printNodeList();
        printAdjList();
        return true;
    }

    private void drawGraph(List<Node> path) {
        GraphGUI graph = new GraphGUI(path);
        graph.setGraphWidth(width);
        graph.setGraphHeight(height);
        graph.setNodes(nodes);
        graph.setAdjList(adjacencyList);
        graph.setPreferredSize(new Dimension(960, 800));
        Thread graphThread = new Thread(graph);
        graphThread.start();
    }

    private void populateNodes() {
        this.nodes = new ArrayList<>();
        for (int i = 0; i < this.numberOfNodes; i++) {
            Axis axis = new Axis();
            int scale = (int) Math.pow(10, 1);
            double xAxis = 0 + RANDOM.nextDouble() * (this.width - 0);
            double yAxis = 0 + RANDOM.nextDouble() * (this.height - 0);

            xAxis = Math.floor(xAxis * scale) / scale;
            yAxis = Math.floor(yAxis * scale) / scale;

            axis.setxAxis(xAxis);
            axis.setyAxis(yAxis);

            this.nodes.add(new Node(i, axis));
        }
    }

    private void printNodeList() {
        System.out.println("\nNode List:");
        for (Node n : this.nodes) {
            System.out.println(n);
        }
        System.out.println();
    }

    private void populateAdjacencyList() {
        adjacencyList = new ArrayList<>();

        for (int i = 0; i < this.numberOfNodes; i++) {
            this.adjacencyList.add(new ArrayList<>());
        }

        for (Node node1 : this.nodes) {
            Axis axis1 = node1.getAxis();
            for (Node node2 : this.nodes) {

                // skip comparing the same node
                if (node1.equals(node2)) continue;

                Axis axis2 = node2.getAxis();
                double xAxis1 = axis1.getxAxis();
                double yAxis1 = axis1.getyAxis();
                double xAxis2 = axis2.getxAxis();
                double yAxis2 = axis2.getyAxis();
                double distance = Math.sqrt(((xAxis1 - xAxis2) * (xAxis1 - xAxis2)) + ((yAxis1 - yAxis2) * (yAxis1 - yAxis2)));

                if ((RANDOM.nextInt(8) + 1) == 1) {
                    List<Node> tmpList1 = adjacencyList.get(node1.getId());
                    List<Node> tmpList2 = adjacencyList.get(node2.getId());

                    Node tmp;
                    if (!tmpList1.contains(node2)) {
                        tmp = new Node(node2);
                        tmp.setCost(Math.round(distance));
                        tmpList1.add(tmp);
                    }

                    if (!tmpList2.contains(node1)) {
                        tmp = new Node(node1);
                        tmp.setCost(Math.round(distance));
                        tmpList2.add(tmp);
                    }
                }
            }
        }

        executeDFS();
        if (!isConnected())
            populateAdjacencyList();
    }

    private void printAdjList() {
        System.out.println("Adjacency List:");
        for (int i = 0; i < this.adjacencyList.size(); i++) {
            System.out.print(i + "->[");
            List<Node> neighbors = this.adjacencyList.get(i);
            if (!neighbors.isEmpty()) {
                Node last = neighbors.get(neighbors.size() - 1);
                for (Node n : neighbors) {
                    if (n != last)
                        System.out.print(n.getId() + ":" + n.getCost() + ", ");
                    else
                        System.out.println(n.getId() + ":" + n.getCost() + "]");
                }
            }
        }
        System.out.println();
    }
}