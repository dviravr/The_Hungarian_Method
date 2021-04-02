package algo2.The_Hungarian_Method;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class GraphPanel extends JPanel {

   Hungarian _hungarian;
   BipartiteGraph _graph;
   JButton _start;
   GraphPanel _gp;

   private final HashMap<Node, int[]> _pos = new HashMap<>();

   public GraphPanel(BipartiteGraph graph) {
      _gp = this;
      _hungarian = new Hungarian(graph, _gp);
      _graph = graph;
      _start = new JButton("Start Algorithm");
      _start.setFont(new Font("Ariel", Font.PLAIN, 20));
      this.add(_start);
      _start.addActionListener(new ListenForButton());
   }

   public void paint(Graphics g) {
      Image buffer_image;
      Graphics buffer_graphics;
      int w = this.getWidth();
      int h = this.getHeight();
      // Create a new "canvas"
      buffer_image = createImage(w, h);
      buffer_graphics = buffer_image.getGraphics();

      // Draw on the new "canvas"
      paintComponents(buffer_graphics);

      // "Switch" the old "canvas" for the new one
      g.drawImage(buffer_image, 0, 0, this);
   }

   public void paintComponents(Graphics g) {
      super.paint(g);

      int w = this.getWidth();
      int h = this.getHeight();
      g.clearRect(0, 0, w, h);
      setPosition(w, h);
      drawGraph(g);
   }

   private void setPosition(int w, int h) {
      int aIndex = 1, bIndex = 1;
      int flowXA = w / (_graph.getGroupA().size() + 1);
      int flowXB = w / (_graph.getGroupB().size() + 1);
      int flowYA = h / 3;
      int flowYB = h / 3 * 2;

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

   private void drawGraph(Graphics g) {
      for (Node n : _graph.getV()) {
         drawNode(n, g);
         for (Edge e : _graph.getE(n.getKey())) {
            g.setColor(Color.gray);
            if (_hungarian.getM().contains(e)) {
               g.setColor(Color.red);
            }
            drawEdge(e, g);
         }
      }
   }

   private void drawNode(Node n, Graphics g) {
      int r = 10, stringFlow;
      if (n.getGroup() == Node.GroupEnum.GROUP_A) {
         g.setColor(Color.BLUE);
         stringFlow = 3;
      } else {
         g.setColor(Color.GREEN);
         stringFlow = -3;
      }
      int x = _pos.get(n)[0];
      int y = _pos.get(n)[1];
      g.fillOval(x - r, y - r, 2 * r, 2 * r);
      g.drawString("" + n.getKey(), x, y - stringFlow * r);
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

   private class ListenForButton implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         if (e.getSource() == _start) {
            _hungarian = new Hungarian(_graph, _gp);
            _hungarian.theHungarianMethod();
         }
      }
   }
}
