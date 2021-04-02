package algo2.The_Hungarian_Method;

import javax.swing.*;
import java.awt.*;
public class GraphFrame extends JFrame {

   public GraphFrame(BipartiteGraph graph) {
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      this.setSize(1200, 700);
      this.setBackground(Color.WHITE);
      this.setTitle("The Hungarian Method");

      GraphPanel panel = new GraphPanel(graph);
      panel.setBounds(0, 150, this.getWidth(), getHeight() - 150);

      this.add(panel);
      this.setVisible(true);
   }
}
