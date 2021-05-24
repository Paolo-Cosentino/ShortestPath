import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphGUI extends JPanel implements Runnable {
    private static final long serialVersionUID = 1L;
    private double graphWidth;
    private double graphHeight;
    private int scaling = 25;
    private int ovalSize = 6;
    private int gridCount = 10;

    private List<Node> nodes, path;
    private List<List<Node>> adjList;

    public GraphGUI(List<Node> path) {
        this.path = path;
    }

    public void setAdjList(List<List<Node>> adjList) {
        this.adjList = adjList;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
        invalidate();
        this.repaint();
    }

    public double getGraphWidth() {
        return graphWidth;
    }

    public void setGraphWidth(double graphWidth) {
        this.graphWidth = graphWidth;
    }

    public double getGraphHeight() {
        return graphHeight;
    }

    public void setGraphHeight(double graphHeight) {
        this.graphHeight = graphHeight;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((getWidth() - 3 * scaling) / (graphWidth));
        double yScale = ((getHeight() - 3 * scaling) / (graphHeight));

        List<Point> graphPoints = new ArrayList<>();
        for (Node node : nodes) {
            double x1 = (node.getAxis().getxAxis() * (xScale) + (2 * scaling));
            double y1 = ((graphHeight - node.getAxis().getyAxis()) * yScale + scaling);
            Point point = new Point();
            point.setLocation(x1, y1);
            graphPoints.add(point);
        }

        g2.setColor(Color.white);
        g2.fillRect(2 * scaling, scaling, getWidth() - (3 * scaling), getHeight() - 3 * scaling);
        g2.setColor(Color.black);

        for (int i = 0; i < gridCount + 1; i++) {
            int x0 = 2 * scaling;
            int x1 = ovalSize + (2 * scaling);
            int y0 = getHeight() - ((i * (getHeight() - (3 * scaling))) / gridCount + (2 * scaling));
            int y1 = y0;
            if (nodes.size() > 0) {
//                g2.setColor(Color.black);
//                g2.drawLine((2 * scaling) + 1 + ovalSize, y0, getWidth() - scaling, y1);
                String yLabel = ((int) ((getGraphHeight() * ((i * 1.0) / gridCount)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        for (int i = 0; i < gridCount + 1; i++) {
            int x0 = i * (getWidth() - (scaling * 3)) / gridCount + (2 * scaling);
            int x1 = x0;
            int y0 = getHeight() - (2 * scaling);
            int y1 = y0 - ovalSize;
            if ((i % ((int) ((nodes.size() / 20.0)) + 1)) == 0) {
                if (nodes.size() > 0) {
//                    g2.setColor(Color.black);
//                    g2.drawLine(x0, getHeight() - (2 * scaling) - 1 - ovalSize, x1, scaling);
                    String xLabel = ((int) ((getGraphWidth() * ((i * 1.0) / gridCount)) * 100)) / 100.0 + "";//i + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
//
                }
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        //Draw the edges
        Stroke stroke = g2.getStroke();
        g2.setColor(Color.gray);
        g2.setStroke(new BasicStroke(2f));

        for (int i = 0; i < nodes.size(); i++) {
            if ((adjList.get(i) != null) && (!adjList.get(i).isEmpty())) {
                for (Node adj : adjList.get(i)) {
                    if (adjList.get(i).contains(adj)) {
                        int x1 = graphPoints.get(i).x;
                        int y1 = graphPoints.get(i).y;
                        int x2 = graphPoints.get(adj.getId()).x;
                        int y2 = graphPoints.get(adj.getId()).y;

                        //Labeling edge
                        int p = (x1 + x2) / 2;
                        int q = (y1 + y2) / 2;

                        g2.setColor(Color.black);
                        g2.drawString(getDistance(i, adj.getId()), p, q);
                        g2.setColor(Color.gray);
                        g2.drawLine(x1, y1, x2, y2);
                    }
                }
            }
        }

        //Draw path
//        g2.setStroke(stroke);
        g2.setStroke(new BasicStroke(2f));
        for (int i = 0; i < path.size() - 1; i++) {
            int x1 = graphPoints.get(path.get(i).getId()).x;
            int y1 = graphPoints.get(path.get(i).getId()).y;
            int x2 = graphPoints.get(path.get(i + 1).getId()).x;
            int y2 = graphPoints.get(path.get(i + 1).getId()).y;
            g2.setColor(Color.RED);
            g2.drawLine(x1, y1, x2, y2);
        }

        //Draw the oval
        g2.setStroke(stroke);
        g2.setColor(Color.red);
        for (Point graphPoint : graphPoints) {
            double x = graphPoint.x - ovalSize / 2.0;
            double y = graphPoint.y - ovalSize / 2.0;
            double ovalW = ovalSize;
            double ovalH = ovalSize;
            Ellipse2D.Double shape = new Ellipse2D.Double(x, y, ovalW, ovalH);
            g2.draw(shape);
        }

        //Label the nodes
        g2.setColor(Color.blue);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - ovalSize / 2;
            int y = graphPoints.get(i).y - ovalSize / 2;
            g2.drawString(Integer.toString(i), x, y);
        }
    }

    public String getDistance(int src, int dest) {
        List<Node> get = adjList.get(src);
        for (Node node : get) {
            if (node.getId() == dest) {
                int i = (int) Math.round(node.getCost());
                return Integer.toString(i);
            }
        }
        return "x";
    }

    public void run() {
        String graphName = "Sensor Network Graph";
        JFrame frame = new JFrame(graphName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}