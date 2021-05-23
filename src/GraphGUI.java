import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphGUI extends JPanel implements Runnable {
    private final DecimalFormat df = new DecimalFormat("0.00");
    private static final long serialVersionUID = 1L;
    private Set<Vertex> vertices;
    private double graphWidth;
    private double graphHeight;
    private final int scaling = 25;
    private final int ovalSize = 6;
    private final int gridCount = 10;
    private Map<Integer, Set<Vertex>> adjList;


    public void setAdjList(Map<Integer, Set<Vertex>> adjList) {
        this.adjList = adjList;
    }

    public void setVertices(Set<Vertex> vertices) {
        this.vertices = vertices;
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
        for (Vertex key : vertices) {
            double x1 = (key.getxAxis() * (xScale) + (2 * scaling));
            double y1 = ((graphHeight - key.getyAxis()) * yScale + scaling);
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
            if (vertices.size() > 0) {
                g2.setColor(Color.black);
                g2.drawLine((2 * scaling) + 1 + ovalSize, y0, getWidth() - scaling, y0);
                String yLabel = ((int) ((getGraphHeight() * ((i * 1.0) / gridCount)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y0);
        }

        for (int i = 0; i < gridCount + 1; i++) {
            int x0 = i * (getWidth() - (scaling * 3)) / gridCount + (2 * scaling);
            int x1 = x0;
            int y0 = getHeight() - (2 * scaling);
            int y1 = y0 - ovalSize;
            if ((i % ((int) ((vertices.size() / 20.0)) + 1)) == 0) {
                if (vertices.size() > 0) {
                    g2.setColor(Color.black);
                    g2.drawLine(x0, getHeight() - (2 * scaling) - 1 - ovalSize, x1, scaling);
                    String xLabel = ((int) ((getGraphWidth() * ((i * 1.0) / gridCount)) * 100)) / 100.0 + "";//i + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);

                }
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        //Draw the edges
        Stroke stroke = g2.getStroke();
        g2.setColor(Color.gray);
        g2.setStroke(new BasicStroke(2f));

        for (int id : adjList.keySet()) {
            if ((adjList.get(id) != null) && (!adjList.get(id).isEmpty())) {
                for (Vertex vertex : adjList.get(id)) {
                    if (adjList.get(id).contains(vertex)) {
                        int x1 = graphPoints.get(id - 1).x;
                        int y1 = graphPoints.get(id - 1).y;
                        int x2 = graphPoints.get(vertex.getId() - 1).x;
                        int y2 = graphPoints.get(vertex.getId() - 1).y;
                        g2.drawLine(x1, y1, x2, y2);


                        int p = (x1 + x2) / 2;
                        int q = (y1 + y2) / 2;

                        g2.setColor(Color.black);
                        g2.drawString(getDistance(id, vertex.getId()), p, q);
                        g2.setColor(Color.gray);
                    }
                }
            }
        }

        //Draw the oval
        g2.setStroke(stroke);
        g2.setColor(Color.red);
        for (Point graphPoint : graphPoints) {
            double x = graphPoint.x - ovalSize / 2.00;
            double y = graphPoint.y - ovalSize / 2.00;
            Ellipse2D.Double shape = new Ellipse2D.Double(x, y, ovalSize, ovalSize);
            g2.draw(shape);
        }

        //Label the nodes
        g2.setColor(Color.blue);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - ovalSize / 2;
            int y = graphPoints.get(i).y - ovalSize / 2;
            g2.drawString("" + (i + 1), x, y);
        }
    }

    private String getDistance(int src, int dest) {
        Set<Vertex> tmp = adjList.get(src);

        for (Vertex v : tmp)
            if (v.getId() == dest)
                return df.format(v.getCost());

        return "MISSING";
    }

    public void run() {
        String graphName = "Graph GUI";
        JFrame frame = new JFrame(graphName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}