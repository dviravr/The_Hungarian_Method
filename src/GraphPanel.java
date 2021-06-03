import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class GraphPanel extends JPanel {

   private final Hungarian _hungarian;
   private final BipartiteGraph _graph;
   private JButton _help, _start, _stepByStep;
   private Node _selectedNode = null;
   private final int _radius = 10;
   private final int[] _addNodesAPos = new int[2];
   private final int[] _addNodesBPos = new int[2];
   private final HashMap<Node, int[]> _pos = new HashMap<>();
   private int _msToSleep = 1500;
   private final Color AColor = new Color(100, 200, 170);
   private final Color BColor = new Color(119, 61, 142);

   public GraphPanel() {
      _graph = new BipartiteGraph();
      _hungarian = new Hungarian(_graph, this);
      addButtons();
      mouseClicked();
   }

   private void addButtons() {
      Font f = new Font("Ariel", Font.PLAIN, 20);
      _help = new JButton("?");
      _start = new JButton("Start Algorithm");
      _stepByStep = new JButton("Step By Step");
      _help.setFont(f);
      _start.setFont(f);
      _stepByStep.setFont(f);
      _help.addActionListener(new ListenForButton());
      _start.addActionListener(new ListenForButton());
      _stepByStep.addActionListener(new ListenForButton());
      this.add(_help);
      this.add(_start);
      this.add(_stepByStep);

      JSlider slider = new JSlider(JSlider.HORIZONTAL, 500, 3000, 500);
      slider.setMajorTickSpacing(500);
      slider.setMinorTickSpacing(100);
      slider.setPaintLabels(true);
      slider.setPaintTicks(true);
      slider.addChangeListener(e -> {
         JSlider source = (JSlider) e.getSource();
         if (!source.getValueIsAdjusting()) {
            _msToSleep = source.getValue();
         }
      });
      this.add(slider);
   }

   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      int w = this.getWidth();
      int h = this.getHeight();
      g.clearRect(0, 100, w, h);
      setPosition(w, h);
      drawAddNodesSign(g, Node.GroupEnum.GROUP_A);
      drawAddNodesSign(g, Node.GroupEnum.GROUP_B);
      drawGraph(g);
   }

   private void drawAddNodesSign(Graphics g, Node.GroupEnum group) {
      int[] pos = _addNodesAPos;
      g.setColor(AColor);
      if (group == Node.GroupEnum.GROUP_B) {
         pos = _addNodesBPos;
         g.setColor(BColor);
      }
      g.fillOval(pos[0] - 2 * _radius, pos[1] - 2 * _radius, 4 * _radius, 4 * _radius);
      Graphics2D g2 = (Graphics2D) g;
      g2.setStroke(new BasicStroke(2));
      g.setColor(Color.WHITE);
      g.drawLine(pos[0], pos[1] - _radius, pos[0], pos[1] + _radius);
      g.drawLine(pos[0] - _radius, pos[1], pos[0] + _radius, pos[1]);
   }

   private void drawGraph(Graphics g) {
      for (Node n : _graph.getV()) {
         drawNode(n, g);
         for (Edge e : _graph.getE(n.getKey())) {
            g.setColor(Color.gray);
            if (_hungarian.getM().contains(e)) {
               g.setColor(new Color(255, 76, 20));
            }
            drawEdge(e, g);
         }
      }
   }

   private void drawNode(Node n, Graphics g) {
      int stringFlow;
      if (n.getGroup() == Node.GroupEnum.GROUP_A) {
         g.setColor(AColor);
         stringFlow = 3;
      } else {
         g.setColor(BColor);
         stringFlow = -3;
      }
      if (n.equals(_selectedNode)) {
         g.setColor(new Color(215, 180, 50));
      }
      int x = _pos.get(n)[0];
      int y = _pos.get(n)[1];
      g.fillOval(x - _radius, y - _radius, 2 * _radius, 2 * _radius);
      g.drawString("" + n.getKey(), x, y - stringFlow * _radius);
   }

   private void drawEdge(Edge edge, Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      g2.setStroke(new BasicStroke(3));
      int x1 = _pos.get(edge.getNode1())[0];
      int y1 = _pos.get(edge.getNode1())[1];
      int x2 = _pos.get(edge.getNode2())[0];
      int y2 = _pos.get(edge.getNode2())[1];
      g2.drawLine(x1, y1, x2, y2);
   }

   //   setting a position for all nodes and add nodes signs
   private void setPosition(int w, int h) {
      _addNodesAPos[0] = w / 2;
      _addNodesAPos[1] = h / 7 * 2;
      _addNodesBPos[0] = w / 2;
      _addNodesBPos[1] = h / 7 * 6;
      int aIndex = 1, bIndex = 1;
      int flowXA = w / (_graph.getGroup(Node.GroupEnum.GROUP_A).size() + 1);
      int flowXB = w / (_graph.getGroup(Node.GroupEnum.GROUP_B).size() + 1);
      int flowYA = h / 7 * 3;
      int flowYB = h / 7 * 5;

      for (Node n : _graph.getV()) {
         int[] xy = new int[2];
         if (n.getGroup() == Node.GroupEnum.GROUP_A) {
            xy[0] = aIndex++ * flowXA;
            xy[1] = flowYA;
         } else {
            xy[0] = bIndex++ * flowXB;
            xy[1] = flowYB;
         }
         _pos.put(n, xy);
      }
   }

   private void mouseClicked() {
      addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
//            left-click: remove edge
            if (e.getButton() == 1) {
               if (isClickOnAddNode(e.getPoint(), Node.GroupEnum.GROUP_A)) {
                  _graph.addNode(new Node(Node.GroupEnum.GROUP_A));
                  _hungarian.resetAlgo();
               } else if (isClickOnAddNode(e.getPoint(), Node.GroupEnum.GROUP_B)) {
                  _graph.addNode(new Node(Node.GroupEnum.GROUP_B));
                  _hungarian.resetAlgo();
               } else {
                  for (Node node : _graph.getV()) {
                     if (isClickOnNode(node, e.getPoint())) {
                        if (_selectedNode == null) {
                           _selectedNode = node;
                        } else if (node.getGroup() == _selectedNode.getGroup()) {
                           _selectedNode = node;
                        } else {
                           _graph.connect(node.getKey(), _selectedNode.getKey());
                           _selectedNode = null;
                        }
                        _hungarian.resetAlgo();
                     }
                  }
               }
            }
//            right-click: remove edge
            if (e.getButton() == 3) {
               for (Node node : _graph.getV()) {
                  if (isClickOnNode(node, e.getPoint())) {
                     _graph.removeNode(node.getKey());
                     _hungarian.resetAlgo();
                     break;
                  } else if (node.getGroup() == Node.GroupEnum.GROUP_A) {
                     for (Edge edge : _graph.getE(node.getKey())) {
                        if (isClickOnEdge(edge, e.getPoint())) {
                           _graph.removeEdge(node.getKey(), edge.getNi(node).getKey());
                           _hungarian.resetAlgo();
                        }
                     }
                  }
               }
            }
            repaint();
         }

         private boolean isClickOnAddNode(Point point, Node.GroupEnum group) {
            int x = _addNodesAPos[0] - point.x;
            int y = _addNodesAPos[1] - point.y;
            if (group == Node.GroupEnum.GROUP_B) {
               x = _addNodesBPos[0] - point.x;
               y = _addNodesBPos[1] - point.y;
            }
            return Math.abs(x) < 2 * _radius && Math.abs(y) < 2 * _radius;
         }

         private boolean isClickOnNode(Node node, Point point) {
            int x = _pos.get(node)[0] - point.x;
            int y = _pos.get(node)[1] - point.y;
            return Math.abs(x) < _radius && Math.abs(y) < _radius;
         }

         private boolean isClickOnEdge(Edge edge, Point point) {
            double x1 = _pos.get(edge.getNode1())[0];
            double y1 = _pos.get(edge.getNode1())[1];
            double x2 = _pos.get(edge.getNode2())[0];
            double y2 = _pos.get(edge.getNode2())[1];
            if (x1 == x2) {
               return ((point.y < y2 && point.y > y1) ||
                     (point.y < y1 && point.y > y2)) &&
                     Math.abs(point.x - x1) < 3;
            }
            double m = (y1 - y2) / (x1 - x2);
            double n = y1 - m * x1;
            return Math.abs(point.y - n - (m * point.x)) < 3;
         }
      });
   }

   private class ListenForButton implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         if (e.getSource() == _start) {
            _hungarian.setStepByStep(false);
            _hungarian.theHungarianMethod();
         } else if (e.getSource() == _stepByStep) {
            Thread t = new Thread(_hungarian);
            _hungarian.setStepByStep(true);
            _hungarian.setMsToSleep(_msToSleep);
            t.start();
         } else if (e.getSource() == _help) {
            JOptionPane.showMessageDialog(null, """
                        To add nodes click on the add buttons.
                        To add edge just choose two nodes in a different groups.
                        Right click on an edge will remove the edge.
                        
                        'Start Algorithm' will color all the matches in the graph.
                        'Step By Step' will color the edges one at a time.
                        The slider is the time to sleep between every step (ms).

                        Notice that right click on a crossroads will remove all edges.""",
                  "information", JOptionPane.INFORMATION_MESSAGE);
         }
      }
   }
}
