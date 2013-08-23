import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.*;
import javax.imageio.ImageIO;

public class GraphFrame extends JFrame
{
    private int width = 1100;   // frame size
    private int height = 720;
    private int paintWidth = width;
    private int paintHeight = 6*height/7;
    private int menuWidth = width;
    private int menuHeight = height - paintHeight;
    private Graph G = new Graph(); 
    private int radius = 10;    // how big is each node
    private Random gen = new Random();  // random number generator
    private PaintSurfacePanel paintSurfacePanel;    // where things are drawn

    // background image
    private BufferedImage img = null;
    
    // components:
    private JPanel GraphPanel;
    private JPanel settingsPanel;
    private JButton NodeButton;
    private JButton EdgeButton;
    private JTextField WField;
    private JLabel WLabel;
    private JLabel FromLabel;
    private JLabel ToLabel;
    private JComboBox FromBox;
    private JComboBox ToBox;
    private JLabel NameLabel;
    private JTextField NameField;
    private JLabel XLabel;
    private JLabel YLabel;
    private JTextField XField;
    private JTextField YField;
    private JComboBox DeleteBox;
    private JLabel DeleteLabel;
    private JButton DeleteButton;
    private JButton InfoButton;
    private JLabel MapLabel;
    private JComboBox MapBox;
    private JButton MapButton;
    private JButton addNodeButton;

    private boolean inMouseMode = false; // are we adding a node where you click?
    
    ///////////////////////////////////////////////////////////////////////////
    public static void main(String[] args)
    {
        GraphFrame g = new GraphFrame();
    }
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    public GraphFrame()
    {   
        // basic settings
        this.setSize(width,height);
        this.setLocationRelativeTo(null);
        this.setTitle("Graphing Program");
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // initializing the layout and content pane
        JPanel contentPane = new JPanel();
        contentPane.setSize(width,height);
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Create the ActionListers c1 and m1:
        ClickListener c1 = new ClickListener();
        MouseButtonListener m1 = new MouseButtonListener();
        // initialize and add the paintSurfacePanel
        paintSurfacePanel = new PaintSurfacePanel();
        paintSurfacePanel.setPreferredSize(new Dimension(paintWidth, paintHeight));
        paintSurfacePanel.addMouseListener(m1);
        contentPane.add(paintSurfacePanel);
        // initialize and add the menubar
        GraphPanel = new JPanel();
        GraphPanel.setPreferredSize(new Dimension(menuWidth, menuHeight));
        contentPane.add(GraphPanel);
        /////////////////////////////////////////
        // the buttons and fields
        // node name
        NameLabel = new JLabel("Name: ");
        GraphPanel.add(NameLabel);
        NameField = new JTextField(7);
        GraphPanel.add(NameField);
        
        // x coordinate 
        XLabel = new JLabel("x:");
        GraphPanel.add(XLabel);
        XField = new JTextField(3);
        GraphPanel.add(XField);
        
        // y coordinate
        YLabel = new JLabel("y:");
        GraphPanel.add(YLabel);
        YField = new JTextField(3);
        GraphPanel.add(YField);
        
        // button for adding node
        NodeButton = new JButton("Node");
        GraphPanel.add(NodeButton);
        NodeButton.addActionListener(c1);

        // button to click to add a node
        addNodeButton = new JButton("add node by clicking");
        GraphPanel.add(addNodeButton);
        addNodeButton.addActionListener(c1);
         
        // new edge goes from:
        FromLabel = new JLabel("from: ");
        GraphPanel.add(FromLabel);  
        FromBox = new JComboBox();
        GraphPanel.add(FromBox);
        
        // new edge goes to:
        ToLabel = new JLabel("to: ");
        GraphPanel.add(ToLabel);
        ToBox = new JComboBox();
        GraphPanel.add(ToBox);
        
        // weight of edge
        WLabel = new JLabel("weight: ");
        GraphPanel.add(WLabel);
        WField = new JTextField(4);
        GraphPanel.add(WField);
        
        // button for adding edge
        EdgeButton = new JButton("Edge");
        GraphPanel.add(EdgeButton);
        EdgeButton.addActionListener(c1);
        
        // button for deleting a node
        DeleteBox = new JComboBox();
        GraphPanel.add(DeleteBox);
        DeleteButton = new JButton("delete");
        GraphPanel.add(DeleteButton);
        DeleteButton.addActionListener(c1);

        // background map choices
        MapLabel = new JLabel("Map: ");
        GraphPanel.add(MapLabel);
        MapBox = new JComboBox();
        GraphPanel.add(MapBox);
        MapButton = new JButton("Change Map");
        GraphPanel.add(MapButton);
        MapButton.addActionListener(c1);
        MapBox.addItem("USA");
        MapBox.addItem("Europe");
        MapBox.addItem("MiddleEast");
        MapBox.addItem("SouthAmerica");

        // button for displaying info
        InfoButton = new JButton("Info");
        GraphPanel.add(InfoButton);
        InfoButton.addActionListener(c1);
        /////////////////////////////////////////
        this.add(contentPane);
        // Getting the background image
        try {
            img = ImageIO.read(this.getClass().getResource("USA.png"));
        } catch (IOException e) {}

        this.setVisible(true);
    }
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    private class PaintSurfacePanel extends JPanel 
    {   
        ///////////////////////////////////////////////////////////////////////
        public void paint(Graphics g)
        {
            Graphics2D g2 = (Graphics2D)g;
            // draw on the background image
            g.drawImage(img,0,0,paintWidth,paintHeight,null);
            
            ///////////////////////////////////////////////////////////////////
            // draw in the grid
            g2.setStroke(new BasicStroke(1));
            for (int i = 0; i < width; i+=100){
                g2.setColor(Color.ORANGE);
                Shape line = new Line2D.Double(i,0,i,height);
                g2.draw(line);
                g2.setColor(Color.RED);
                g2.drawString(""+i,i+5, 20);
            }
            for (int j = 0; j < height; j += 100){
                g2.setColor(Color.ORANGE);
                Shape line = new Line2D.Double(0,j,width,j);
                g2.draw(line);
                g2.setColor(Color.RED);
                g2.drawString(""+j,20,j+10);
            }
            ///////////////////////////////////////////////////////////////////

            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.RED);
            ///////////////////////////////////////////////////////////////////
            //draw on all the edges
            for (Edge ed : G.getEdges()){
                Shape line = new Line2D.Double(ed.getStartX(), 
                                               ed.getStartY(), 
                                               ed.getEndX(), 
                                               ed.getEndY());

                g2.setStroke(new BasicStroke(ed.getWeight()));
                g2.draw(line);
            }
            ///////////////////////////////////////////////////////////////////
            
            ///////////////////////////////////////////////////////////////////
            //draw on all the Nodes
            g2.setColor(Color.RED);
            for(Node n : G.getNodes()){
                g2.setColor(Color.RED);
                Shape circle = new Ellipse2D.Double((n.getX()-radius), 
                                                    (n.getY()-radius), 
                                                    2*radius, 
                                                    2*radius);
                g2.fill(circle);
                g2.drawString(n.getName(), n.getX()-6, n.getY()-12);
            }
            ///////////////////////////////////////////////////////////////////
        }
        ///////////////////////////////////////////////////////////////////////
    }
    ///////////////////////////////////////////////////////////////////////////
    
    ///////////////////////////////////////////////////////////////////////////
    private class ClickListener implements ActionListener
    {   
        ///////////////////////////////////////////////////////////////////////
        public void actionPerformed(ActionEvent e)
        {   
            ///////////////////////////////////////////////////////////////////
            // add a node
            if(e.getSource() == NodeButton){
                if (!XField.getText().equals("") && !YField.getText().equals("")){
                    Node n = new Node(Integer.parseInt(XField.getText()),
                                      Integer.parseInt(YField.getText()), 
                                      NameField.getText());
                    G.addNode(n);
                    
                    /////////////////////////
                    // update GUI
                    FromBox.addItem(n);
                    ToBox.addItem(n);
                    DeleteBox.addItem(n);
                    XField.setText("");
                    YField.setText("");
                    NameField.setText("");
                    /////////////////////////
                }
            ///////////////////////////////////////////////////////////////////
            } else
            ///////////////////////////////////////////////////////////////////
            if (e.getSource() == addNodeButton){
                if(!inMouseMode){
                    inMouseMode = true;
                } else {
                    inMouseMode = false;
                }
            ///////////////////////////////////////////////////////////////////
            } else 
            ///////////////////////////////////////////////////////////////////
            // add an edge 
            if(e.getSource() == EdgeButton){
                Edge ed = new  Edge(((Node)FromBox.getSelectedItem()).getX(),
                                    ((Node)FromBox.getSelectedItem()).getY(),
                                    ((Node)ToBox.getSelectedItem()).getX(), 
                                    ((Node)ToBox.getSelectedItem()).getY(),
                                    2);
                // check if the edge had weight specified:
                if(!WField.getText().equals("")){
                    ed.setWeight(Integer.parseInt(WField.getText()));
                    ((Node)FromBox.getSelectedItem()).addEdge(ed);
                    ((Node)ToBox.getSelectedItem()).addEdge(ed);
                    G.addEdge(ed);
                } else {
                    ((Node)FromBox.getSelectedItem()).addEdge(ed);
                    ((Node)ToBox.getSelectedItem()).addEdge(ed);
                    G.addEdge(ed);
                }
            ///////////////////////////////////////////////////////////////////
            } else 
            ///////////////////////////////////////////////////////////////////
            // delete a node
            if(e.getSource() == DeleteButton){
                Node n = ((Node)DeleteBox.getSelectedItem());
                for (Edge l : n.getEdges()){
                    G.removeEdge(l);
                }
                G.removeNode(n);

                /////////////////////////
                // update GUI
                FromBox.removeItem(n);
                ToBox.removeItem(n);
                DeleteBox.removeItem(n);
                /////////////////////////

            ///////////////////////////////////////////////////////////////////
            } else 
            ///////////////////////////////////////////////////////////////////
            // display information
            if (e.getSource() == InfoButton){
                JOptionPane.showMessageDialog(null, "Charting Program made by" +
                    " Benjamin Eskildsen\nBackgrounds from " +
                    "www.af.mil accessed 5/30/2013\n" + 
                    "http://www.youreuropemap.com/ accessed 5/31/2013\n" +
                    "http://geography.about.com/ accessed 5/31/2013\n" +
                    "http://countries.bridgat.com/ accessed 5/31/2013");
            ///////////////////////////////////////////////////////////////////
            } else 
            ///////////////////////////////////////////////////////////////////
            if (e.getSource() == MapButton){
                String imgStr = ((String)MapBox.getSelectedItem());
                try {
                    img = ImageIO.read(this.getClass().getResource(imgStr+".png"));
                } catch (IOException ex) {}
            }
            ///////////////////////////////////////////////////////////////////
            // update GUI
            repaint();
        }
        ///////////////////////////////////////////////////////////////////////
    }
    ///////////////////////////////////////////////////////////////////////////
    // Handling mouse clicks
    private class MouseButtonListener implements MouseListener 
    {
        public void mouseClicked(MouseEvent e){
            if (inMouseMode){
                String name = "";
                if (!NameField.getText().equals("")){
                    name = NameField.getText();
                } else {
                    name = JOptionPane.showInputDialog(null,"Name the node: ");
                }
                Node n = new Node(e.getX(), e.getY(), name);
                G.addNode(n);
                
                /////////////////////////
                // update GUI
                FromBox.addItem(n);
                ToBox.addItem(n);
                DeleteBox.addItem(n);
                XField.setText("");
                YField.setText("");
                NameField.setText("");
                /////////////////////////
                inMouseMode = false;  
            }     
            repaint();
        }
        public void mouseEntered(MouseEvent e){}
        public void mouseExited(MouseEvent e){}
        public void mousePressed(MouseEvent e){}
        public void mouseReleased(MouseEvent e){}
    }
    ///////////////////////////////////////////////////////////////////////////
}