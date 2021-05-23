import java.util.Comparator;
import java.util.Objects;

public class Vertex implements Comparator<Vertex> {
    private int id;
    private double xAxis;
    private double yAxis;
    private double cost;

    public Vertex(){}

    public Vertex(int id, double cost) {
        this.id = id;
        this.cost = cost;
    }

    public Vertex(int id, double xAxis, double yAxis) {
        this.id = id;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    /**
     * Copy Constructor used to generate adjacency list since
     * cost per edge is vertex specific
     *
     * @param vertex - old vertex
     * @param cost   - cost to reach
     */
    public Vertex(Vertex vertex, double cost) {
        this.id = vertex.getId();
        this.xAxis = vertex.getxAxis();
        this.yAxis = vertex.getyAxis();
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getxAxis() {
        return xAxis;
    }

    public void setxAxis(double xAxis) {
        this.xAxis = xAxis;
    }

    public double getyAxis() {
        return yAxis;
    }

    public void setyAxis(double yAxis) {
        this.yAxis = yAxis;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "id=" + id +
                ", xAxis=" + xAxis +
                ", yAxis=" + yAxis +
                '}';
    }

    @Override
    public int compare(Vertex o1, Vertex o2) {
        return Double.compare(o1.cost, o2.cost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex)) return false;
        Vertex vertex = (Vertex) o;
        return getId() == vertex.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


}
