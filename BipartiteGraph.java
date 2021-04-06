package algo2.The_Hungarian_Method;

import java.util.*;

public class BipartiteGraph {
   private final HashMap<Integer, Node> _graphNodes;
   private final HashMap<Integer, HashMap<Integer, Edge>> _graphEdges;
   private int modeCount = 0;
   private int edgeSize = 0;
   public boolean run = true;

   public BipartiteGraph() {
      _graphNodes = new HashMap<>();
      _graphEdges = new HashMap<>();
   }

   public Node getNode(int key) {
      return _graphNodes.get(key);
   }

   public void addNode(Node n) {
//      if the node exist don't do nothing
      if (!hasNode(n.getKey())) {
//         adding new node to the graph
         _graphNodes.put(n.getKey(), n);
         _graphEdges.put(n.getKey(), new HashMap<>());
         modeCount++;
      }
   }

   public void connect(int n1, int n2) {
      if (hasNode(n1) && hasNode(n2) && !hasEdge(n1, n2) && !hasEdge(n2, n1)
              && getNode(n1).getGroup() != getNode(n2).getGroup()) {
//          if there is an edge don't increase the edge size
         edgeSize++;
         modeCount++;
         Edge edge = new Edge(getNode(n1), getNode(n2));
         _graphEdges.get(n1).put(n2, edge);
         _graphEdges.get(n2).put(n1, edge);
      }
   }

   public Collection<Node> getV() {
      return _graphNodes.values();
   }

   /* get all nodes id's from specific group */
   public Set<Integer> getGroup(Node.GroupEnum groupEnum) {
      Set<Integer> group = new HashSet<>();
      for (Node node : getV()) {
         if (node.getGroup() == groupEnum) group.add(node.getKey());
      }
      return group;
   }

   public Collection<Edge> getE(int key) {
      if (hasNode(key)) {
         return _graphEdges.get(key).values();
      }
      return new ArrayList<>();
   }

   public Node removeNode(int key) {
      Node node = getNode(key);
      if (node != null) {
//         if the node exist in the graph and removing all his edges
//         first, removing all of the edges that the node is the dest node
         edgeSize -= _graphEdges.get(key).size();
         for (int ni : _graphEdges.get(key).keySet()) {
            removeEdge(key, ni);
         }
//         after he isn't the dest of any node removing him from the graph
         _graphEdges.remove(key);
         _graphNodes.remove(key);
         modeCount++;
      }
      return node;
   }

   public void removeEdge(int n1, int n2) {
//      disconnect to nodes
      if (hasEdge(n1, n2)) {
         edgeSize--;
         modeCount++;
         _graphEdges.get(n1).remove(n2);
         _graphEdges.get(n2).remove(n1);
      }
   }

   public int nodeSize() {
      return _graphNodes.size();
   }

   public int edgeSize() {
      return edgeSize;
   }

   public int getMC() {
      return modeCount;
   }

   private boolean hasNode(int key) {
      return _graphNodes.containsKey(key);
   }

   private boolean hasEdge(int src, int dest) {
      return hasNode(src) && _graphEdges.get(src).containsKey(dest);
   }

   @Override
   public String toString() {
      return "BipartiteGraph{" +
              "_graphNodes=" + _graphNodes +
              ", _graphEdges=" + _graphEdges +
              '}';
   }
}

