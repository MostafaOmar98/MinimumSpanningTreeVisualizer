import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.AbstractGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.*;
import javafx.scene.shape.Circle;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class GraphDrawer
{
    AbstractGraph mst;
    String windowName;
    boolean right;

    public GraphDrawer(int nodes, ArrayList<Edge> edgeList, String windowName, boolean isDirected, boolean right)
    {
        mst = new SparseMultigraph<Integer, String>();
        for (int i = 0; i < nodes; ++i)
            mst.addVertex(i);

        String extra = new String();
        for (Edge e : edgeList)
        {
            extra += " ";
            String eCost = Integer.toString(e.getCost());
            Integer u = e.getU(), v = e.getV();
            eCost += extra;
            if (isDirected)
                mst.addEdge(eCost, u, v, EdgeType.DIRECTED);
            else
                mst.addEdge(eCost, u, v);
        }
        this.windowName = windowName;
        this.right = right;
    }

    public void draw()
    {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();

        Layout<Integer, String> layout = new CircleLayout(mst);
        layout.setSize(new Dimension((int) rect.getMaxX() / 2, (int) rect.getMaxY() / 2));
        BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<Integer, String>(layout);
        vv.setPreferredSize(new Dimension((int) rect.getMaxX() / 2, (int) rect.getMaxY() / 2));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        Transformer<String, Font> edgeFontTransfomer = new Transformer<String, Font>()
        {
            @Override
            public Font transform(String s)
            {
                return new Font(s, 0, 20);
            }
        };

        Transformer<Integer, Font> NodeFontTransformer = new Transformer<Integer, Font>()
        {
            @Override
            public Font transform(Integer s)
            {
                return new Font(s.toString(), 0, 20);
            }
        };
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeFontTransformer(edgeFontTransfomer);
        vv.getRenderContext().setVertexFontTransformer(NodeFontTransformer);
        vv.getRenderContext().setVertexShapeTransformer(new Transformer<Integer, Shape>()
        {
            @Override
            public Shape transform(Integer u)
            {
                return new Ellipse2D.Double(-15, -15, 30, 30);
            }
        });
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        JFrame frame = new JFrame(windowName);
        //        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        if (right)
            frame.setLocation((int) rect.getMaxX() - (int) rect.getMaxX() / 2, 0);
        frame.setVisible(true);
    }
}
