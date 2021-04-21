import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class BipartiteDiGraph {
   private final HashMap<Integer, Node> _graphNodes;
   private final HashMap<Integer, HashMap<Integer, Edge>> _graphEdges;
   private final HashMap<Integer, HashSet<Integer>> _destNi;
   private int modeCount = 0;
   private int edgeSize = 0;

   public BipartiteDiGraph() {
      _graphNodes = new HashMap<>();
      _graphEdges = new HashMap<>();
      _destNi = new HashMap<>();
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
         _destNi.put(n.getKey(), new HashSet<>());
         modeCount++;
      }
   }

   public void connect(int src, int dest) {
      if (hasNode(src) && hasNode(dest) && !hasEdge(src, dest) && getNode(src).getGroup() != getNode(dest).getGroup()) {
//          if there is an edge don't increase the edge size
         edgeSize++;
         modeCount++;
         _destNi.get(dest).add(src);
         _graphEdges.get(src).put(dest, new Edge(getNode(src), getNode(dest)));
      }
   }

   public Collection<Node> getV() {
      return _graphNodes.values();
   }

   public Collection<Edge> getE(int node_id) {
      if (hasNode(node_id)) {
         return _graphEdges.get(node_id).values();
      }
      return new ArrayList<>();
   }

   public Node removeNode(int key) {
      Node node = getNode(key);
      if (node != null) {
//         if the node exist in the graph and removing all his edges
//         first, removing all of the edges that the node is the dest node
         for (int ni : new HashSet<>(_destNi.get(key))) {
            removeEdge(ni, key);
         }
//         after he isn't the dest of any node removing him from the graph
         edgeSize -= _graphEdges.get(key).size();
         _destNi.remove(key);
         _graphEdges.remove(key);
         _graphNodes.remove(key);
         modeCount++;
      }
      return node;
   }

   public void removeEdge(int src, int dest) {
//      disconnect to nodes
      if (hasEdge(src, dest)) {
         edgeSize--;
         modeCount++;
         _destNi.get(dest).remove(src);
         _graphEdges.get(src).remove(dest);
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
}
