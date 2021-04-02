package algo2.The_Hungarian_Method;

import java.util.Objects;

public class Edge {

   private final Node _node1;
   private final Node _node2;

   Edge(Node node1, Node node2) {
      _node1 = node1;
      _node2 = node2;
   }

   public Node getNode1() {
      return _node1;
   }

   public Node getNode2() {
      return _node2;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Edge edge = (Edge) o;
//      not a directed edge, so the node1-node2 is the same as node2-node1
      return (Objects.equals(_node1, edge._node1) && Objects.equals(_node2, edge._node2)) ||
              (Objects.equals(_node1, edge._node2) && Objects.equals(_node2, edge._node1));
   }

   @Override
   public int hashCode() {
      return Objects.hash(_node1, _node2) + Objects.hash(_node2, _node1);
   }

   @Override
   public String toString() {
      return "Edge {" +
              " n1=" + _node1 +
              ", n2=" + _node2 +
              " }\n";
   }
}
