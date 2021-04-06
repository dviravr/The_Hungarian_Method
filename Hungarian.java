package algo2.The_Hungarian_Method;

import javax.swing.*;
import java.util.*;

public class Hungarian {

   private final BipartiteGraph _graph;
   private final ArrayList<Edge> _M;
   private Set<Integer> _Am;
   private Set<Integer> _Bm;
   private final GraphPanel _gp;

   public Hungarian(BipartiteGraph graph, GraphPanel gp) {
      _graph = graph;
      _gp = gp;
      _M = new ArrayList<>();
      _Am = graph.getGroup(Node.GroupEnum.GROUP_A);
      _Bm = graph.getGroup(Node.GroupEnum.GROUP_B);
   }

   public ArrayList<Edge> getM() {
      return _M;
   }

   /* finding the max match using the Hungarian Method */
   public void theHungarianMethod(boolean stepByStep) {
      _Am = _graph.getGroup(Node.GroupEnum.GROUP_A);
      _Bm = _graph.getGroup(Node.GroupEnum.GROUP_B);
      _M.clear();
      new SwingWorker() {
         @Override
         protected Object doInBackground() throws Exception {
            ArrayList<Edge> newMatch = mAugmentingPath();
            while (newMatch != null) {
               if (stepByStep) {
                  _gp.repaint();
                  Thread.sleep(1500);
               }
               addMatchToM(newMatch);
               _Am = setUnsaturatedGroup(_graph.getGroup(Node.GroupEnum.GROUP_A));
               _Bm = setUnsaturatedGroup(_graph.getGroup(Node.GroupEnum.GROUP_B));
               newMatch = mAugmentingPath();
            }
            System.out.println(_M);
            _gp.repaint();
            System.out.println("end of algorithm");
            return null;
         }
      }.execute();
   }

   /* setting a new group of all nodes that don't have any match in a specific group */
   private Set<Integer> setUnsaturatedGroup(Set<Integer> group) {
      Set<Integer> unsaturatedGroup = new HashSet<>();
      for (Integer node : group) {
         if (!isSaturated(node)) {
            unsaturatedGroup.add(node);
         }
      }
      return unsaturatedGroup;
   }

   /* check if specific node have a match */
   private boolean isSaturated(int node) {
      for (Edge edge : _M) {
         if (node == edge.getNode1().getKey() || node == edge.getNode2().getKey()) return true;
      }
      return false;
   }


   /* adding or replacing the new matches that found */
   private void addMatchToM(ArrayList<Edge> newMatch) {
      for (Edge edge : newMatch) {
         if (_M.contains(edge)) {
            _M.remove(edge);
         } else {
            _M.add(edge);
         }
      }
   }

   /* check if there is a path from group A to group B and that both nodes don't have a match on the diGraph*/
   private ArrayList<Edge> mAugmentingPath() {
      BipartiteDiGraph diGraph = toDiGraph();
      for (Integer src : _Am) {
         for (Integer dest : _Bm) {
            ArrayList<Edge> path = BFS(diGraph, _graph.getNode(src), _graph.getNode(dest));
            if (path != null) {
               return path;
            }
         }
      }
      return null;
   }

   /* Take BipartiteGraph and rebuild him as BipartiteDiGraph in specific order.
   *  If the edge is part of the match, the direction is from group B to group A,
   *  else the direction is from A to B*/
   private BipartiteDiGraph toDiGraph() {
      BipartiteDiGraph diGraph = new BipartiteDiGraph();
      for (Node node : _graph.getV()) {
         diGraph.addNode(node);
      }
      for (Node node : _graph.getV()) {
         for (Edge edge : _graph.getE(node.getKey())) {
            if (_M.contains(edge)) {
               diGraph.connect(edge.getNode2().getKey(), edge.getNode1().getKey());
            } else {
               diGraph.connect(edge.getNode1().getKey(), edge.getNode2().getKey());
            }
         }
      }
      return diGraph;
   }

   private ArrayList<Edge> BFS(BipartiteDiGraph diGraph, Node src, Node dest) {
      resetVisited(diGraph);
      HashMap<Node, Node> path = new HashMap<>();
//      temp queue
      Queue<Node> queue = new LinkedList<>();
      queue.add(src);

//      set src to visited
      src.setVisited(true);

//      looping while there is nodes in the queue
      while (!queue.isEmpty()) {
         Node node = queue.remove();
         if (node.equals(dest))
            break;
         for (Edge edge : diGraph.getE(node.getKey())) {
            if (!edge.getNode2().isVisited()) {
               /*
                checking if we visited this neighbor
                if not we adding him to the queue and setting to visited
                and add the edge to the path
                */
               queue.add(edge.getNode2());
               edge.getNode2().setVisited(true);
               path.put(edge.getNode2(), edge.getNode1());
            }
         }
      }
      return shortestPath(path, src, dest);
   }

   private ArrayList<Edge> shortestPath(HashMap<Node, Node> _path, Node src, Node dest) {
//      checking that the dest is connected to src
      if (_path.get(dest) == null) return null;
//      setting n to the parent of dest

      Node n = _path.get(dest);
      ArrayList<Edge> shortestPath = new ArrayList<>();

//      adding the edge to the set
      Edge e = new Edge(n, dest);
      shortestPath.add(e);

//      looping while the parent isn't the src
      while (!n.equals(src)) {
         e = new Edge(_path.get(n), n);
         shortestPath.add(e);
//         setting n to the parent of parent
         n = _path.get(n);
      }
      return shortestPath;
   }

   private void resetVisited(BipartiteDiGraph graph) {
//      reset all the visited values to false
      for (Node node : graph.getV()) {
         node.setVisited(false);
      }
   }
}
