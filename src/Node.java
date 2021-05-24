class Node {
    private final int id;
    private long cost;
    private Axis axis;
    private boolean dataNode;

    public Node(int id, Axis axis) {
        this.id = id;
        this.cost = 0;
        this.axis = axis;
        this.dataNode = false;
    }

    public Node(Node node) {
        this.id = node.id;
        this.cost = node.cost;
        this.axis = node.axis;
        this.dataNode = node.dataNode;
    }

    public Node(int id, int cost) {
        this.id = id;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return (this.dataNode ? "Data\t" : "Storage\t")
                + ": " + this.id
                + ", xAxis:" + this.axis.getxAxis()
                + ", yAxis:" + this.axis.getyAxis();
    }

    public int getId() {
        return id;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public Axis getAxis() {
        return axis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return this.getId() == node.getId();
    }

    public int compareTo(Node node) {
        return Double.compare(this.cost, node.cost);
    }
}
