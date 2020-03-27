import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

import java.util.ArrayList;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;


public class GUI extends JPanel
{
    private static final long serialVersionUID = 1L;
    private ArrayList<JTextField> edgesTF;
    JTabbedPane tabbedPane;

    public GUI()
    {
        super(new GridLayout(1, 1));
        this.edgesTF = new ArrayList<>();

        tabbedPane = new JTabbedPane();

        JComponent setup = createSetupPanel();
        tabbedPane.addTab("Setup", setup);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        add(tabbedPane);

        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    private JPanel createSetupPanel()
    {
        JPanel panel = new JPanel(false);
        JPanel form = new JPanel(new GridLayout(3, 3));
        JPanel edges = new JPanel(false);
        edges.setAutoscrolls(true);
        JScrollPane edgesMain = new JScrollPane(edges);
        edgesMain.setPreferredSize(new Dimension(600, 450));


        int MAX_NODES = 100;
        Integer[] numbers = new Integer[MAX_NODES];
        for (int i = 0; i < MAX_NODES; ++i)
        {
            numbers[i] = i + 1;
        }
        JLabel nVerticesLabel = new JLabel("Select number of vertices");
        JComboBox nVertices = new JComboBox<>(numbers);
        JLabel nEdgesLabel = new JLabel("Select number of edges");
        JComboBox nEdges = new JComboBox<>(numbers);
        JLabel directedLabel = new JLabel("Is directed?");
        JCheckBox directed = new JCheckBox();
        JButton generate = new JButton("Generate!");
        generate.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                ArrayList<Edge> edgesArr = new ArrayList<>();
                Integer nV = (Integer) nVertices.getSelectedItem();
                int error = 0;
                // Loop over inputs
                for (int i = 0; i < edgesTF.size(); i += 3)
                {
                    JTextField u = edgesTF.get(i);
                    JTextField v = edgesTF.get(i + 1);
                    JTextField cost = edgesTF.get(i + 2);
                    try
                    {
                        Edge e = new Edge(Integer.valueOf(u.getText()), Integer.valueOf(v.getText()), Integer.valueOf(cost.getText()));
                        // Check that there is no out of bounds vertices
                        if (e.getU() >= nV || e.getV() >= nV || e.getU() < 0 || e.getV() < 0)
                        {
                            error = 1;
                            break;
                        }
                        edgesArr.add(e);
                    } catch (NumberFormatException e)
                    {
                        error = 2;
                        break;
                    }
                }
                if (error == 0)
                    generateGraph(nV, edgesArr, 0, directed.isSelected());
                else if (error == 1)
                    JOptionPane.showMessageDialog(null, "Node Numbers must be between [0, n - 1] inclusive");
                else if (error == 2)
                    JOptionPane.showMessageDialog(null, "Please enter all fields in numeric format (All entires must be numbers)");

            }
        });
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(generate);

        nEdges.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                edges.removeAll();
                edges.add(createEdgesInput((Integer) nEdges.getSelectedItem()));
                edges.revalidate();
                edges.repaint();
            }
        });

        form.add(nVerticesLabel);
        form.add(nVertices);
        form.add(nEdgesLabel);
        form.add(nEdges);
        form.add(directedLabel);
        form.add(directed);

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(form);
        panel.add(edgesMain);
        panel.add(bottomPanel);
        return panel;
    }

    private JPanel createEdgesInput(int nEdges)
    {
        edgesTF.clear();
        JPanel panel = new JPanel(new GridLayout(nEdges + 1, 2));
        panel.add(new JLabel("From"));
        panel.add(new JLabel("To"));
        panel.add(new JLabel("Cost"));
        Dimension size = new Dimension(50, 20);
        for (int i = 0; i < nEdges; ++i)
        {
            JTextField u = new JTextField();
            JTextField v = new JTextField();
            JTextField cost = new JTextField();
            u.setPreferredSize(size);
            v.setPreferredSize(size);
            cost.setPreferredSize(size);
            panel.add(u);
            panel.add(v);
            panel.add(cost);
            edgesTF.add(u);
            edgesTF.add(v);
            edgesTF.add(cost);
        }
        return panel;
    }


    private void generateGraph(Integer nVertices, ArrayList<Edge> edges, int start, boolean isDirected)
    {

        GraphDrawer originalDrawer = new GraphDrawer(nVertices, edges, "Original Graph", isDirected, false);
        originalDrawer.draw();

        HamiltonCircuitSolver solver = new HamiltonCircuitSolver(nVertices, edges, start, isDirected);
        ArrayList<Edge> answer = solver.getAnswer();
        if (answer == null)
        {
            JOptionPane.showMessageDialog(null, "There does not exist a hamilton circuit for this graph starting from node " + start);
            return;
        }
        GraphDrawer hamiltonDrawer = new GraphDrawer(nVertices, answer, "Minimum Hamilton Circuit", isDirected, true);
        hamiltonDrawer.draw();
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    public static void createAndShowGUI()
    {
        JFrame frame = new JFrame("Graph helper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new GUI(), BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }
}
