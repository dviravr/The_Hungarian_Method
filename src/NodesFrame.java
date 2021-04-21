import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NodesFrame extends JFrame {

   private final JButton _createNodes;
   JTextField _groupAText = new JTextField(3);
   JTextField _groupBText = new JTextField(3);
   BipartiteGraph _graph;
   private int _groupANodes;
   private int _groupBNodes;

   public NodesFrame() {
      this.setSize(500, 500);
      this.setLocationRelativeTo(null);
      this.setTitle("Nodes creator");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      JPanel header = new JPanel();
      JPanel groupAPanel = new JPanel();
      JPanel groupBPanel = new JPanel();
      JPanel createPanel = new JPanel();

      JLabel groupALabel = new JLabel("Group A: ");
      JLabel groupBLabel = new JLabel("Group B: ");
      JLabel headerLabel = new JLabel("Create Graph's Nodes");
      JLabel subHeaderLabel = new JLabel("Enter num of nodes in each group");

      Font f = new Font("Ariel", Font.BOLD, 20);
      headerLabel.setFont(f);
      subHeaderLabel.setFont(f);
      groupALabel.setFont(f);
      groupBLabel.setFont(f);
      _groupAText.setFont(f);
      _groupBText.setFont(f);

      _createNodes = new JButton("Create Nodes");
      _createNodes.setFont(f);

      header.add(headerLabel);
      header.add(subHeaderLabel);
      groupAPanel.add(groupALabel);
      groupAPanel.add(_groupAText);
      groupBPanel.add(groupBLabel);
      groupBPanel.add(_groupBText);
      createPanel.add(_createNodes);

      Box box = Box.createVerticalBox();
      box.add(header);

      Box insideBox = Box.createHorizontalBox();
      insideBox.add(groupAPanel);
      insideBox.add(groupBPanel);

      box.add(insideBox);
      box.add(createPanel);
      this.add(box);

      _createNodes.addActionListener(new ListenForButton());
      this.setVisible(true);

   }

   private class ListenForButton implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         if (e.getSource() == _createNodes) {
            try {
               _groupANodes = Integer.parseInt(_groupAText.getText());
               _groupBNodes = Integer.parseInt(_groupBText.getText());
               if (_groupANodes < 0 || _groupBNodes < 0) {
                  throw new NumberFormatException();
               }
               createNodes();
            } catch (NumberFormatException err) {
               JOptionPane.showMessageDialog(null, "Invalid input!");
            }
         }
      }
   }

   private void createNodes() {
      _graph = new BipartiteGraph();
      for (int i = 0; i < _groupANodes; i++) {
         _graph.addNode(new Node(Node.GroupEnum.GROUP_A));
      }
      for (int i = 0; i < _groupBNodes; i++) {
         _graph.addNode(new Node(Node.GroupEnum.GROUP_B));
      }
      new EdgesFrame(_graph);
      this.dispose();
   }
}
