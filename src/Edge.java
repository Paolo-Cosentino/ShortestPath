public class Edge implements Comparable {
    private int startPoint;
    private int endPoint;
    private long weight;

    public Edge(int startPoint, int endPoint, long weight) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.weight = weight;
    }

    public int getStartPoint() {
        return startPoint;
    }

    public int getEndPoint() {
        return endPoint;
    }

    public long getWeight() {
        return weight;
    }

    public int compareTo(Object o) {
        Edge e = (Edge) o;
        return Long.compare(this.weight, e.weight);
    }

    public String toString() {
        return startPoint + "-" + endPoint + " (" + weight + ")";
    }
}
