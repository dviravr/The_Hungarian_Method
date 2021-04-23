import javax.swing.*;
import java.awt.*;
public class GraphFrame extends JFrame {

   public GraphFrame() {
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setLocation(150, 75);
      this.setSize(1200, 700);
      this.setBackground(Color.WHITE);
      this.setTitle("The Hungarian Method");

      GraphPanel panel = new GraphPanel();
      panel.setBounds(0, 0, this.getWidth(), getHeight());

      this.add(panel);
      this.setVisible(true);
   }
}
