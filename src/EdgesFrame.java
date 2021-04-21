import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

public class EdgesFrame extends JFrame {

   private final JButton _createGraph;
   private final JButton _connect;
   ButtonGroup _buttonGroupA = new ButtonGroup();
   ButtonGroup _buttonGroupB = new ButtonGroup();
   private final BipartiteGraph _graph;

   EdgesFrame(BipartiteGraph graph) {
      _graph = graph;

      this.setSize(1000, 600);
      this.setLocationRelativeTo(null);
      this.setTitle("Connect Nodes");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      JPanel header = new JPanel();
      Box groupABox = Box.createHorizontalBox();
      Box groupBBox = Box.createHorizontalBox();
      JPanel connectPanel = new JPanel();
      JPanel createPanel = new JPanel();

      JLabel groupALabel = new JLabel("Group A: ");
      JLabel groupBLabel = new JLabel("Group B: ");
      JLabel headerLabel = new JLabel("Connect Graph's Nodes.");
      JLabel subHeaderLabel = new JLabel("connect one edge each time");

      Font f = new Font("Ariel", Font.BOLD, 22);
      headerLabel.setFont(f);
      subHeaderLabel.setFont(f);
      groupALabel.setFont(f);
      groupBLabel.setFont(f);

      _connect = new JButton("Connect");
      _connect.setFont(new Font("Ariel", Font.BOLD, 18));
      _createGraph = new JButton("Create Graph");
      _createGraph.setFont(f);

      header.add(headerLabel);
      header.add(subHeaderLabel);
      groupABox.add(groupALabel);
      groupBBox.add(groupBLabel);

      for (Integer n : graph.getGroup(Node.GroupEnum.GROUP_A)) {
         JRadioButton rb = new JRadioButton(String.valueOf(n));
         _buttonGroupA.add(rb);
         groupABox.add(rb);
      }

      for (Integer n : graph.getGroup(Node.GroupEnum.GROUP_A)) {
         JRadioButton rb = new JRadioButton(String.valueOf(n));
         _buttonGroupB.add(rb);
         groupBBox.add(rb);
      }
      connectPanel.add(_connect);
      createPanel.add(_createGraph);

      Box box = Box.createVerticalBox();
      box.add(header);

      box.add(groupALabel);
      box.add(groupABox);
      box.add(groupBLabel);
      box.add(groupBBox);

      box.add(connectPanel);
      box.add(createPanel);
      this.add(box);

      _connect.addActionListener(new ListenForButton());
      _createGraph.addActionListener(new ListenForButton());
      this.setVisible(true);
   }

   private class ListenForButton implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         if (e.getSource() == _connect) {
            try {
               int n1 = getNodesToConnect(_buttonGroupA);
               int n2 = getNodesToConnect(_buttonGroupB);
               if (n1 < 0 || n2 < 0) {
                  throw new NumberFormatException();
               }
               _graph.connect(n1, n2);
               resetButtons(_buttonGroupA);
               resetButtons(_buttonGroupB);
            } catch (NumberFormatException err) {
               JOptionPane.showMessageDialog(null, "You need to pick two Nodes!");
            }
         }
         if (e.getSource() == _createGraph) {
            try {
               drawGraph();
            } catch (NumberFormatException err) {
            }
         }
      }
   }

   private void drawGraph() {
      new GraphFrame(_graph);
      this.dispose();
   }

   private int getNodesToConnect(ButtonGroup buttonGroup) {
      for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
         AbstractButton button = buttons.nextElement();
         if (button.isSelected()) {
            return Integer.parseInt(button.getText());
         }
      }
      return -1;
   }

   private void resetButtons(ButtonGroup buttonGroup) {
      for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
         AbstractButton button = buttons.nextElement();
         button.setSelected(false);
      }
   }
}
